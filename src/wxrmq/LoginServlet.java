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
import javax.websocket.Session;

import org.hibernate.SessionFactory;

import com.mysql.fabric.xmlrpc.base.Data;
import com.oracle.webservices.internal.api.databinding.DatabindingFactory;
import com.sun.net.httpserver.Headers;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import wxrmq.data.remote.UUIDResponse;
import wxrmq.domain.Account;
import wxrmq.utils.NetWork;

public class LoginServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf8");
		resp.setHeader("Content-type", "text/html;charset=UTF-8");
		String mobile = req.getParameter("mobile");
		String password = req.getParameter("password");
		Account account = queryAccount(mobile, password);
		HttpSession session = req.getSession(true);
		if(account != null){
			session.setAttribute("account", account);
		}else {
			resp.setStatus(401);
			resp.getWriter().write("手机号码，密码不正确");
		}
	}
	
	private Account queryAccount(String mobile, String password) {
		Account account = RmqDB.getById(Account.class, mobile);
		if(account == null){
			return null;
		}
		if (account.getPassword().equals(password)){
			return account;
		}
		return null;
	}
	

}
