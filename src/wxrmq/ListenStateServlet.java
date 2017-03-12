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

public class ListenStateServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		WxLoginApi api = NetWork.getOkhttpClient(req);
		Call<ResponseBody> call = api.listenState(false, req.getParameter("uuid"), 0);
		retrofit2.Response<ResponseBody> response = call.execute();
		resp.getWriter().write(response.body().string());
	}

}
