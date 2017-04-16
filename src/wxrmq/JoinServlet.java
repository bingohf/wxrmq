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

public class JoinServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf8");
		String mobile = req.getParameter("mobile");
		String password = req.getParameter("password");
		String mobileCode = req.getParameter("mobileCode");
		resp.setHeader("Content-type", "text/html;charset=UTF-8");
		if(mobile.isEmpty() || password.isEmpty()){
			 resp.setStatus(403);
			 resp.getWriter().write("用户名，密码不合法");
			return;
		}
		if(!mobileCode.equals(req.getSession().getAttribute("mobileCode"))){
			 resp.setStatus(403);
			 resp.getWriter().write("手机验证码不正确");
			return;
		}
		
		
		Account account = RmqDB.getById(Account.class, mobile);
		try{
			if (account == null){
				account = Account.createAccount(mobile, password);
				HttpSession session = req.getSession(true);
				session.setAttribute("account", account);
			}else{
				 resp.setStatus(403);
				 resp.getWriter().write("此用户已经存在");
			}
		}catch(Exception e){
		   resp.setStatus(500);
		   resp.getWriter().write(e.getMessage());
		}
		
	}
	

}
