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

public class MobileValidationCodeServlet extends HttpServlet {

	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf8");
		String mobile = req.getParameter("mobile");
		String validateCode = req.getParameter("validateCode");
		String sessionCode = (String) req.getSession().getAttribute("validateCode");
		resp.setHeader("Content-type", "text/html;charset=UTF-8");
		String message ="";
		if (sessionCode.equals(validateCode)){
			req.getSession().setAttribute("mobileCode", "1111");
			message = "手机验证码已经发送（1111）";
		}else{
			resp.setStatus(401);
			message = "动态验证码不正确";
		}
		resp.getWriter().write(message);
		
	}
	

}
