package wxrmq;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional.TxType;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.google.gson.Gson;
import com.oracle.webservices.internal.api.databinding.DatabindingFactory;
import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;
import com.sun.java_cup.internal.runtime.virtual_parse_stack;
import com.sun.net.httpserver.Headers;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.sun.org.apache.xpath.internal.operations.And;
import com.sun.swing.internal.plaf.basic.resources.basic;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Text;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Call;
import wxrmq.data.remote.ContactList;
import wxrmq.data.remote.QueryReturn;
import wxrmq.data.remote.RmqValue;
import wxrmq.data.remote.UUIDResponse;
import wxrmq.domain.Account;
import wxrmq.domain.WxUser;
import wxrmq.domain.WxUser_Tag;
import wxrmq.utils.NetWork;
import wxrmq.utils.TextUtils;

public class QueryServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		req.setCharacterEncoding("UTF-8");
		String quota =  req.getParameter("quota");
		String friendsCount = req.getParameter("friendsCount");
		String city = req.getParameter("city");
		String sex = req.getParameter("sex");
	    String sql = "select uin,nickName,Sex,FriendsCount,city,age,industry,malePercent "
	            + " from WxUser a  where 1=1 ";
	    if(!TextUtils.isEmpty(quota)){
	    	sql += " and " + replaceParam(quota, "quota");
	    }
	    if(!TextUtils.isEmpty(city)){
	    	city = replaceParam(city, "label") + " or " + replaceParam(city, "subLabel");
	    	if (!TextUtils.isEmpty(friendsCount)){
	    		sql += " and  exists (select 1 from wxUser_Tag b where a.uin= b.uin and " +  replaceParam(friendsCount, "count") + " and (" + city +"))";
	    	} else{
	    		sql += " and  exists (select 1 from wxUser_Tag b where a.uin= b.uin and (" + city +"))";
	    	}
	    }else{
	    	if (!TextUtils.isEmpty(friendsCount)){
	    		sql += " and  " + replaceParam(friendsCount, "friendsCount") ;
	    	}
	    }	
	    if(!TextUtils.isEmpty(sex)){
	    	sql += " and " + replaceParam(sex, "malePercent");
	    }
		resp.setCharacterEncoding("UTF-8");
		List<Object[]> result = RmqDB.sqlQuery(sql);
		QueryReturn queryReturn = new QueryReturn();
		
		for (Object[] row : result) {
			QueryReturn.Item item = new QueryReturn.Item();
			item.setUin((String) row[0]);
			item.setNickName((String)row[1]);
			item.setSex(((BigInteger)row[2]).intValue());
			item.setFriendsCount(((BigInteger)row[3]).intValue());
			item.setCity((String)row[4]);
			item.setAge((Integer)row[5]);
			item.setIndustry((String)row[6]);
			item.setMalePercent(((BigDecimal )row[7]).floatValue());
			queryReturn.getItems().add(item);
		}

		resp.setHeader("Content-type", "text/html;charset=UTF-8");
		resp.getWriter().write(new Gson().toJson(queryReturn));
	}
	
	private String replaceParam(String qString,String paramName){
		return qString.replaceAll("\\(param\\)", paramName);
	}

}
