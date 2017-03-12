package wxrmq;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
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
import wxrmq.data.remote.UUIDResponse;
import wxrmq.domain.WxUser;
import wxrmq.utils.NetWork;

public class RecordRMServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		WxUser currentUser = NetWork.getGson().fromJson(req.getReader(), WxUser.class);
		OkHttpClient client = NetWork.buildClient(req);
		if (currentUser.getHeadImgUrl() != null){
			currentUser.setHeadImgBase64(getImageBase64("https://wx.qq.com" +currentUser.getHeadImgUrl(), client));
		}
		Builder requestBuilder = new Request.Builder().url("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxgetcontact?r=1489192482320&seq=0");
		Response response = client.newCall(requestBuilder.build()).execute();
		if (response.isSuccessful()){
			ContactList contactList =NetWork.getGson().fromJson(response.body().charStream(), ContactList.class);
			recordRmq(currentUser,contactList.getMemberList());
		}
	}

	private void recordRmq(WxUser currentUser, WxUser[] wxUsers) {
		SessionFactory dbfactory = WxRmqDB.getDBFactory();
		Session session = dbfactory.openSession();
		Transaction tx = session.beginTransaction();
		try{
		session.saveOrUpdate(currentUser);
		tx.commit();
		}catch(HibernateException e){
			e.printStackTrace();
			tx.rollback();
		}
		session.close();
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
	
	

}
