package wxrmq.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.hql.internal.ast.ErrorReporter;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.org.apache.regexp.internal.recompile;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Request.Builder;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Logger;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Retrofit;
import retrofit2.http.HTTP;
import sun.util.logging.resources.logging;
import wxrmq.WxLoginApi;
import wxrmq.data.remote.ErrorResponse;

public class NetWork {
	private static Gson gson;
	
	public static synchronized WxLoginApi getOkhttpClient(HttpServletRequest req){
		HttpSession session = req.getSession(true);
		WxLoginApi api = (WxLoginApi) session.getAttribute("loginApi");
		if(api == null){
			System.setProperty ("jsse.enableSNIExtension", "false");
			api = new Retrofit.Builder().baseUrl("https://login.wx.qq.com/").client(buildClient(req))
			.build().create(WxLoginApi.class);
			session.setAttribute("loginApi", api);
		
		}
		return api;
	}
	
	public static synchronized Gson getGson() {
		if(gson == null){
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE);
			gson = gsonBuilder.create();
		}
		return gson;
		
	}
	
	public static OkHttpClient buildClient(HttpServletRequest req) {
		HttpSession session = req.getSession(true);
		OkHttpClient client = (OkHttpClient) session.getAttribute("okhttp");
		if (client!= null){
			return client;
		}
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.cookieJar(new CookieJar() {
			ArrayList<Cookie> cookies = new ArrayList<>();
			@Override
			public void saveFromResponse(HttpUrl arg0, List<Cookie> cookies) {
				// TODO Auto-generated method stub
				for(Cookie cookie: cookies){
					if (cookie.name().equals("wxuin")){
						session.setAttribute("wxuin", cookie.value());
					}else if(cookie.name().equals("wxsid")){
						session.setAttribute("wxsid", cookie.value());
					}
					if(!cookie.name().equals("JSESSIONID")){
						removeCookie(cookie.name());
						this.cookies.add(cookie);
					}
					
				}
			
			}
			
			@Override
			public List<Cookie> loadForRequest(HttpUrl arg0) {
				// TODO Auto-generated method stub
				return cookies;
			}
			private void removeCookie(String name) {
				for(Cookie cookie: cookies){
					if(cookie.name().equals(name)){
						cookies.remove(cookie);
						return;
					}
				}
				
			}
		});
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new Logger(){

			@Override
			public void log(String arg0) {
				// TODO Auto-generated method stub
				System.out.println(arg0);
			}});
		logging.setLevel(HttpLoggingInterceptor.Level.BODY);
		builder.addInterceptor(logging);
		builder.connectTimeout(100, TimeUnit.SECONDS).readTimeout(100, TimeUnit.SECONDS);
		client = builder.build();
		session.setAttribute("okhttp", client);
		return client;
	}
	
	public static String getErrorJson(Throwable e){
		ErrorResponse response = new ErrorResponse();
		response.setErrorMessage(e.getMessage());
		return getGson().toJson(response);
	}
	
	public static String getDomain(String url){
		String domain = url.replace("https://", "");
		int index = domain.indexOf("/");
		return domain.substring(0, index);
	}
	
	
	public static String getImageBase64(String url,OkHttpClient client) throws IOException {
		Builder requestBuilder = new Request.Builder().url(url);
		Response response = client.newCall(requestBuilder.build()).execute();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		String base64 ="";
		if(response.isSuccessful()){
			BufferedSink sink = Okio.buffer(Okio.sink(byteArrayOutputStream));
			sink.writeAll(response.body().source());
			sink.close();
			base64 = Base64.encode(byteArrayOutputStream.toByteArray());
		}
		return base64;
	}
}
