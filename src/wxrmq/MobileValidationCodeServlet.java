package wxrmq;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
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
import wxrmq.domain.SmSLog;
import wxrmq.exceptions.SmsException;
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
		String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$"; 
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(mobile);
		if (!matcher.matches()){
			message = "手机号码不正确";
		}else{
			String mobileCode = String.format("%04d",(Math.abs(new Random(System.currentTimeMillis()).nextInt() %10000)));
			if (sessionCode.equals(validateCode)){
				try {
					sendSMS(mobile, mobileCode, req.getRemoteAddr(), NetWork.buildClient(req));
					req.getSession().setAttribute("mobileCode",mobile +"_"+ mobileCode);
					message = "手机验证码已经发送";
					System.out.println(mobileCode);
				}
				catch (SmsException e) {
					resp.setStatus(401);
					message = e.getMessage();
				}
			}else{
				resp.setStatus(401);
				message = "动态验证码不正确";
			}
		}
		resp.getWriter().write(message);
	}
	
	private void sendSMS(String mobile,String mobileCode,String ip,OkHttpClient okHttpClient) throws IOException, SmsException{
		SmSLog smSLog = RmqDB.getById(SmSLog.class, mobile);
		if (smSLog == null || System.currentTimeMillis() - smSLog.getLastSentDate().getTime() > 60000){
			String smsURL = "http://106.ihuyi.com/webservice/sms.php?method=Submit&account=cf_ddkeji008&password=5e6588394cffbfbab9113b3a10c61726&mobile="+ mobile+"&format=json&content=%E6%82%A8%E7%9A%84%E9%AA%8C%E8%AF%81%E7%A0%81%E6%98%AF%EF%BC%9A"+ mobileCode+"%E3%80%82%E8%AF%B7%E4%B8%8D%E8%A6%81%E6%8A%8A%E9%AA%8C%E8%AF%81%E7%A0%81%E6%B3%84%E9%9C%B2%E7%BB%99%E5%85%B6%E4%BB%96%E4%BA%BA%E3%80%82";
			Builder requestBuilder = new Request.Builder().url(smsURL);
			Response response = okHttpClient.newCall(requestBuilder.build()).execute();
		}else{
			throw new SmsException("请求太频繁，请稍后再试");
		}
		smSLog = new SmSLog();
		smSLog.setIp(ip);
		smSLog.setLastSentDate(new Date());
		smSLog.setMobile(mobile);
		RmqDB.saveOrUpdate(smSLog);
		
	}
	

}
