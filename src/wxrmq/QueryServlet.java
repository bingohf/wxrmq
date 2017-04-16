package wxrmq;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

public class QueryServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		req.setCharacterEncoding("UTF-8");
		String query = req.getParameter("query");
		int start = 0;
		int count = 100;
		resp.setCharacterEncoding("UTF-8");
		RmqValue rmqValue = new RmqValue();
		List<Object[]> result = RmqDB.sqlQuery(
				" select a.uin, a.NickName,a.sex, b.count,a.FriendsCount, a.headImgBase64 "
               + " from WxUser a join (select  unid, count from WxUser_Tag where  label =?) b on b.unid= a.uin "
               + "  order by b.count desc, a.FriendsCount ",
               query);
		QueryReturn queryReturn = new QueryReturn();
		
		for (Object[] row : result) {
			QueryReturn.Item item = new QueryReturn.Item();
			item.setUnid(((BigInteger)row[0]).longValue());
			item.setNickName((String)row[1]);
			item.setSex(((BigInteger)row[2]).intValue());
			item.setTagCount(((BigInteger)row[3]).intValue());
			item.setFriendsCount(((BigInteger)row[4]).intValue());
			item.setHeadImgBase64((String)row[5]);
			item.setQueryTag(query);
			queryReturn.getItems().add(item);
		}

		resp.setHeader("Content-type", "text/html;charset=UTF-8");
		resp.getWriter().write(NetWork.getGson().toJson(queryReturn));
	}

}
