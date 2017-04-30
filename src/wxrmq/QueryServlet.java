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

import com.oracle.webservices.internal.api.databinding.DatabindingFactory;
import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;
import com.sun.net.httpserver.Headers;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

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
import wxrmq.utils.NetWork;
import wxrmq.utils.TextUtils;

public class QueryServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		req.setCharacterEncoding("UTF-8");
		String quota = req.getParameter("quota");
		String friendsCount = req.getParameter("friendsCount");
		String city = req.getParameter("city");
		String interest = req.getParameter("interest");
		
	    String sql = "select uin,nickName,headImgBase64,Sex,FriendsCount,quota from WxUser a where 1=1 ";
	    if(!TextUtils.isEmpty(quota)){
	    	sql += " and " + quota;
	    }
	
	    if(!TextUtils.isEmpty(interest)){
	    	sql += " and  uin in (select uin from WxUser_Tag where " + interest +")" ;
	    }
	    

		
		int start = 0;
		int count = 100;
		resp.setCharacterEncoding("UTF-8");
		RmqValue rmqValue = new RmqValue();
		List<Object[]> result = RmqDB.sqlQuery(sql);
		QueryReturn queryReturn = new QueryReturn();
		
		for (Object[] row : result) {
			QueryReturn.Item item = new QueryReturn.Item();
			item.setUin(((BigInteger)row[0]).longValue());
			item.setNickName((String)row[1]);
			item.setHeadImgBase64((String)row[2]);
			item.setSex(((BigInteger)row[3]).intValue());
			item.setFriendsCount(((BigInteger)row[4]).intValue());
			if(row[5] != null){
				item.setQuota(((BigDecimal)row[5]).floatValue());
			}
			queryReturn.getItems().add(item);
		}

		resp.setHeader("Content-type", "text/html;charset=UTF-8");
		resp.getWriter().write(NetWork.getGson().toJson(queryReturn));
	}

}
