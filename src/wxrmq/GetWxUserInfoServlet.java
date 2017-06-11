package wxrmq;

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

public class GetWxUserInfoServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		String wxid = req.getParameter("wxid");
		resp.setHeader("Content-type", "text/html;charset=UTF-8");
		resp.setCharacterEncoding("UTF-8");
		WxUser wxUser = RmqDB.getById(WxUser.class, wxid);
		resp.getWriter().write(new Gson().toJson(wxUser));
	}

}
