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
		String mobile = req.getParameter("mobile");
		String password = req.getParameter("password");
		Account account = null;
		try{
			account = Account.createAccount(mobile, password);
			HttpSession session = req.getSession(true);
			session.setAttribute("account", account);
		}catch(Exception e){
			resp.setStatus(500);
		   resp.getWriter().write(e.getMessage());
		}
		
	}
	

}
