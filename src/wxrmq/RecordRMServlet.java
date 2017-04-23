package wxrmq;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional.TxType;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.oracle.webservices.internal.api.databinding.DatabindingFactory;
import com.sun.net.httpserver.Headers;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Call;
import wxrmq.data.remote.ContactList;
import wxrmq.data.remote.RecordRmq;
import wxrmq.data.remote.UUIDResponse;
import wxrmq.domain.Account;
import wxrmq.domain.WxUser;
import wxrmq.domain.WxUser_Tag;
import wxrmq.utils.NetWork;

public class RecordRMServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf8");
		//System.out.println(req.getReader().readLine());
		RecordRmq recordRmq = NetWork.getGson().fromJson(req.getReader(), RecordRmq.class);
		OkHttpClient client = NetWork.buildClient(req);
		if (recordRmq.getCurUser().getHeadImgUrl() != null){
			recordRmq.getCurUser().setHeadImgBase64(getImageBase64("https://wx.qq.com" +recordRmq.getCurUser().getHeadImgUrl(), client));
		}
		HttpSession sess = req.getSession(true);
		Account account = (Account) sess.getAttribute("account");
		//account.setWx_unid(123L);
		
		
		ArrayList<WxUser_Tag> tags = createTagList(recordRmq.getCurUser().getWx_id(), 
				recordRmq.getMemberList());
		
		SessionFactory dbfactory = RmqDB.getDBFactory();
		Session session = dbfactory.openSession();
		Transaction tx = session.beginTransaction();
		try{
			Query query=session.createQuery("delete WxUser_Tag s where s.unid=" + recordRmq.getCurUser().getWx_id());
			query.executeUpdate();

			for(WxUser_Tag tag: tags){
				session.saveOrUpdate(tag);
			}
			session.saveOrUpdate(account);
			recordRmq.getCurUser().setFriendsCount(getFriendsCount(recordRmq.getMemberList()));
			session.saveOrUpdate(recordRmq.getCurUser());
		tx.commit();
		}catch(HibernateException e){
			e.printStackTrace();
			tx.rollback();
		}
		session.close();
		
	}

	
	private ArrayList<WxUser_Tag> createTagList(Long unid, WxUser[] wxUsers){
		 ArrayList<WxUser_Tag> list = new ArrayList<>();
		 WxUser_Tag maleTag = new WxUser_Tag();
		 maleTag.setId(String.format("%d_%d_%s", unid,0, "ÄÐ"));
		 maleTag.setUnid(unid);
		 maleTag.setType(0);
		 maleTag.setLabel("ÄÐ");
		 
		 WxUser_Tag femaleTag = new WxUser_Tag();
		 femaleTag.setId(String.format("%d_%d_%s", unid,0, "Å®"));
		 femaleTag.setType(0);
		 femaleTag.setUnid(unid);
		 femaleTag.setLabel("Å®");
		 
		 HashMap<String, WxUser_Tag> map = new HashMap<>();
		 for(WxUser user: wxUsers){
			 if(user.getSex() ==0){
				 continue;
			 }
			 if(user.getSex() ==2){
				 femaleTag.setCount(femaleTag.getCount() +1); 
			 }else if(user.getSex() == 1){
				 maleTag.setCount(maleTag.getCount() +1); 
			 }
			 String city = user.getCity();
			 WxUser_Tag cityTag = map.get(city);
			 if(cityTag == null){
				 cityTag = new WxUser_Tag();
				 cityTag.setId(String.format("%d_%d_%s", unid,1, city));
				 cityTag.setType(1);
				 cityTag.setUnid(unid);
				 cityTag.setLabel(city);
				 map.put(city, cityTag);
			 }
			 cityTag.setCount(cityTag.getCount() +1);
			 
			 
		 }
		 list.add(maleTag);
		 list.add(femaleTag);
		 list.addAll(map.values());
		 return list;
	}
	
	
	private Account queryAccount(String mobile, String password) {
		String hql ="select a.mobile from Account a where mobile =? and password =? ";
		List<Object[]> data = RmqDB.query(hql, mobile, password);
		if(data != null && data.size()>0){
			Account account = new Account();
			account.setMobile(mobile);
			account.setPassword(password);
			return account;
		}
		return null;
	}
	private String getImageBase64(String url,OkHttpClient client) throws IOException {
		Builder requestBuilder = new Request.Builder().url(url);
		Response response = client.newCall(requestBuilder.build()).execute();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		String base64 ="";
		if(response.isSuccessful()){
			BufferedSink sink = Okio.buffer(Okio.sink(byteArrayOutputStream));
			sink.writeAll(response.body().source());
			sink.close();
			base64 = Base64.encode(byteArrayOutputStream.toByteArray());
		}
		return base64;
	}
	
	private int getFriendsCount(WxUser[] wxUsers){
		int count =0;
		for(WxUser wxUser:wxUsers){
			if(wxUser.getSex() == 0){
				continue;
			}
			++count;
		}
		return count;
		
	}
	

}
