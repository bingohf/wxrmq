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
		resp.getWriter().write(json);
		
		if(response.code() == 200){
			ContactList contactList = NetWork.getGson().fromJson(json, ContactList.class);
			req.getSession(true).setAttribute("contactList",contactList);
		}
	}

}
