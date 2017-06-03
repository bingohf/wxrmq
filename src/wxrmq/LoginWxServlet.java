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

import com.sun.net.httpserver.Headers;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import wxrmq.data.remote.UUIDResponse;
import wxrmq.utils.NetWork;

public class LoginWxServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String redirectUrl = req.getParameter("redirect_url");
		OkHttpClient client = NetWork.buildClient(req);
		String host =req.getHeader("host");
		Builder requestBuilder = new Request.Builder().url(redirectUrl);
		Enumeration<String> headerNames = req.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String name = headerNames.nextElement();
			String value = req.getHeader(name);
			// requestBuilder.addHeader(name, value);
		}
		requestBuilder.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
		String domainName = NetWork.getDomain(redirectUrl);
		requestBuilder.header("Host", domainName);
		requestBuilder.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
	//	requestBuilder.header("host", "wx.qq.com").header("Referer", "https://wx.qq.com/").header("User-Agent",
	//			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36");
		//requestBuilder.header("Cookie", null);
		Response response = client.newCall(requestBuilder.build()).execute();
		okhttp3.Headers headers = response.headers();
		for (int i = 0; i < headers.size(); ++i) {
			String name = headers.name(i);
			List<String> values = headers.values(name);
			for(String value:values){
				if (name.equals("Set-Cookie")){
					value = value.replace(String.format("Domain=%s;",domainName), "Domain=" +host +";");
				}
				resp.addHeader(name, value);
			}
		}
		resp.getWriter().write(response.body().string());
		resp.setStatus(response.code());

	}
	


}
