package wxrmq;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.MailcapCommandMap;
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

import com.fasterxml.classmate.util.ResolvedTypeCache.Key;
import com.google.gson.Gson;
import com.mysql.fabric.xmlrpc.base.Array;
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
import wxrmq.data.remote.InitResponse;
import wxrmq.data.remote.UUIDResponse;
import wxrmq.data.remote.WxUserInfo;
import wxrmq.domain.TagValue;
import wxrmq.domain.WxContact;
import wxrmq.domain.WxUser;
import wxrmq.utils.NetWork;
import wxrmq.utils.TextUtils;

public class GetContactServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String redirectUrl = req.getParameter("redirect_url");
		String hostName = NetWork.getDomain(redirectUrl);
		
		Builder requestBuilder = new Request.Builder().url(String.format("https://%s/cgi-bin/mmwebwx-bin/webwxgetcontact?r=1489192482320&seq=0",hostName));
		OkHttpClient client = NetWork.buildClient(req);
		Response response = client.newCall(requestBuilder.build()).execute();
		resp.setHeader("Content-type", "text/html;charset=UTF-8");
		resp.setCharacterEncoding("UTF-8");
		String json = response.body().string();
		resp.setStatus(response.code());
		if(response.code() == 200){
			ContactList contactList = NetWork.getGson().fromJson(json, ContactList.class);
			WxUserInfo wxUserInfo = (WxUserInfo) req.getSession(true).getAttribute("wxUserInfo");
			buildTagValueList(contactList,wxUserInfo);
			resp.getWriter().write(new Gson().toJson(wxUserInfo));
		}
		
		
	}

	private void buildTagValueList(ContactList contactList, WxUserInfo wxUserInfo) {
		
		TagValue male = new TagValue();
		male.setTag("��");
		male.setY(0);
		wxUserInfo.getFriendInfo().getSexs().add(male);
		
		TagValue female = new TagValue();
		female.setTag("Ů");
		female.setY(0);
		wxUserInfo.getFriendInfo().getSexs().add(female);
		
		TagValue unkownSex = new TagValue();
		unkownSex.setTag("δ֪");
		unkownSex.setY(0);

		HashMap<String, TagValue> hashCitys = new HashMap<>();
		
		for(WxContact wxContact: contactList.getMemberList()){
			switch (wxContact.getSex()) {
			case 0:
				unkownSex.setY(unkownSex.getY() + 1);
				break;
			case 1:
				male.setY(male.getY() + 1);
				break;
			case 2:
				female.setY(female.getY() + 1);
				break;
			}
			String cityKey = wxContact.getProvince() + "|" +  wxContact.getCity();
			TagValue cityTag = hashCitys.get(cityKey);
			if(cityTag == null){
				cityTag = new TagValue();
				cityTag.setTag(wxContact.getProvince());
				cityTag.setSubTag(wxContact.getCity());
				cityTag.setY(0);
				hashCitys.put(cityKey, cityTag);
				if (TextUtils.isEmpty(wxContact.getProvince()) && TextUtils.isEmpty(wxContact.getCity())){
					cityTag.setTag("δ֪");
				}
			}
			cityTag.setY(cityTag.getY() +1);
		}
		if(male.getY() + female.getY() > 0){
			wxUserInfo.setMalePercent((100f * male.getY()) / (male.getY() + female.getY()));
		}
		if(unkownSex.getY() >0){
			wxUserInfo.getFriendInfo().getSexs().add(unkownSex);
		}
		Collection<TagValue> temp = hashCitys.values();
		TagValue[] citys = temp.toArray(new TagValue[temp.size()]);
		Arrays.sort(citys, new Comparator<TagValue>() {
			@Override
			public int compare(TagValue o1, TagValue o2) {
				return o2.getY() -o1.getY();
			}
		});
		wxUserInfo.getFriendInfo().setFriendsCount(contactList.getMemberList().length);
		wxUserInfo.getFriendInfo().getCitys().clear();
		wxUserInfo.getFriendInfo().getCitys().addAll(Arrays.asList(citys));
	}
	


}
