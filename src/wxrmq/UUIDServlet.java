package wxrmq;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import wxrmq.data.remote.UUIDResponse;
import wxrmq.utils.NetWork;

public class UUIDServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.setProperty ("jsse.enableSNIExtension", "false");
		WxLoginApi api = NetWork.getOkhttpClient(req);
		Call<ResponseBody> call = api.getUUID();
		retrofit2.Response<ResponseBody> response = call.execute();
		resp.getWriter().write(response.body().string());
		resp.setStatus(response.code());
		resp.setContentLength((int) response.body().contentLength());
	}

}
