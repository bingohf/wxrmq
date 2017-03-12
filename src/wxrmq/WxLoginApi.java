package wxrmq;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WxLoginApi {
	@GET("jslogin?appid=wx782c26e4c19acffb&redirect_uri=https%3A%2F%2Fwx.qq.com%2Fcgi-bin%2Fmmwebwx-bin%2Fwebwxnewloginpage&fun=new&lang=zh_CN")
	Call<ResponseBody> getUUID();

	@GET("cgi-bin/mmwebwx-bin/login")
	Call<ResponseBody> listenState(@Query("loginicon") boolean loginicon, @Query("uuid") String uuid,
			@Query("tip") int tip);
}
