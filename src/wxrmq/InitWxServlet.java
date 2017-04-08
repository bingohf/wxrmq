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
import wxrmq.utils.NetWork;

public class InitWxServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		String wxuin = (String) session.getAttribute("wxuin");
		String wxsid = (String) session.getAttribute("wxsid");
		OkHttpClient client = NetWork.buildClient(req);
		String host = req.getHeader("host");

		Builder requestBuilder = new Request.Builder()
				.url("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxinit?r=11600029842")
				.post(RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),
						"{\"BaseRequest\":{\"Uin\":\"xuin=" + wxuin + "\",\"Sid\":\"" + wxsid
								+ "\",\"Skey\":\"\",\"DeviceID\":\"e095989469409771\"}}"));

		Response response = client.newCall(requestBuilder.build()).execute();
		okhttp3.Headers headers = response.headers();
		for (int i = 0; i < headers.size(); ++i) {
			String name = headers.name(i);
			List<String> values = headers.values(name);
			for (String value : values) {
				if (name.equals("Set-Cookie")) {
					value = value.replace("Domain=wx.qq.com;", "Domain=" + host + ";");
				}
				resp.addHeader(name, value);
			}
		}
		resp.setHeader("Content-type", "text/html;charset=UTF-8");
		resp.setCharacterEncoding("UTF-8");
		resp.getWriter().write(response.body().string());
		resp.setStatus(response.code());

	}

}
