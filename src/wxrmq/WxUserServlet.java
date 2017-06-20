package wxrmq;

import java.io.IOException;import java.util.Date;
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

import com.google.gson.Gson;
import com.oracle.webservices.internal.api.databinding.DatabindingFactory;
import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;
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
import wxrmq.data.remote.RmqValue;
import wxrmq.data.remote.UUIDResponse;
import wxrmq.data.remote.WxUserInfo;
import wxrmq.domain.Account;
import wxrmq.domain.WxContact;
import wxrmq.domain.WxUser;
import wxrmq.utils.NetWork;
import wxrmq.utils.TextUtils;

public class WxUserServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Long wxid = Long.parseLong(req.getParameter("wxid"));
		resp.setHeader("Content-type", "text/html;charset=UTF-8");
		resp.setCharacterEncoding("UTF-8");
		WxUser wxUser = RmqDB.getById(WxUser.class, wxid);
		resp.getWriter().write(wxUser.getInfoJson());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setHeader("Content-type", "text/html;charset=UTF-8");
		resp.setCharacterEncoding("UTF-8");

		
		
		String uin = req.getPathInfo().substring(1);
		WxUser wxUser = RmqDB.getById(WxUser.class, uin);
		Account account = (Account) req.getSession(true).getAttribute("account");
		if(account == null || !account.getMobile().equals(wxUser.getMobile())){
			resp.setStatus(403);
			resp.getWriter().write("没有权限，请重新登录");
			return;
		}
		WxUserInfo wxUserInfo = new Gson().fromJson(wxUser.getInfoJson(),WxUserInfo.class);
		String wxid = req.getParameter("wxId");
		String city = req.getParameter("city");
		String age = req.getParameter("age");
		String quota = req.getParameter("quota");
		String industry = req.getParameter("industry");
		wxUserInfo.setCity(city);
		wxUserInfo.setWxId(wxid);
		wxUserInfo.getIndustry().clear();
		wxUserInfo.getIndustry().add(industry);
		if(!TextUtils.isEmpty(quota)){
			wxUserInfo.setQuota(Float.parseFloat(quota));
		}
		if(!TextUtils.isEmpty(age)){
			wxUserInfo.setAge(Integer.parseInt(age));
		}
		wxUser.setUpdated(new Date());
		RmqDB.saveWxUserInfo(wxUserInfo, wxUser);
		
		
	}
	
	

}
