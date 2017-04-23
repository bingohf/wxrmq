package wxrmq;

import java.io.ByteArrayOutputStream;
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
import wxrmq.data.remote.RmqValue;
import wxrmq.data.remote.UUIDResponse;
import wxrmq.domain.Account;
import wxrmq.domain.WxUser;
import wxrmq.utils.NetWork;

public class RmqValueServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Account account = (Account) session.getAttribute("account");
		RmqValue rmqValue = new RmqValue();
		Long wxid = 0L;
		if (account != null) {
			wxid = 123L;
			rmqValue.setMobile(account.getMobile());
		} else {
			wxid = Long.parseLong(req.getParameter("wxid"));
		}
		resp.setHeader("Content-type", "text/html;charset=UTF-8");
		resp.setCharacterEncoding("UTF-8");

		if(wxid != null){
	
			List<Object[]> result = RmqDB.query(
					"select  a.label,a.count from WxUser_Tag a where a.type=0 and a.unid=? order by a.count desc",
					wxid);
			for (Object[] row : result) {
				RmqValue.Item item = new RmqValue.Item();
				item.setLabel((String) row[0]);
				item.setCount((int) row[1]);
				rmqValue.getSexValue().add(item);
			}
			WxUser wxUser = RmqDB.getById(WxUser.class, wxid);
			rmqValue.setWxUser(wxUser);
			
			result = RmqDB.query(
					"select  a.label,a.count from WxUser_Tag a where a.type=1 and a.unid=?  order by a.count desc",
					wxid);
			for (Object[] row : result) {
				RmqValue.Item item = new RmqValue.Item();
				item.setLabel((String) row[0]);
				item.setCount((int) row[1]);
				rmqValue.getCityValue().add(item);
			}
		}
		resp.getWriter().write(NetWork.getGson().toJson(rmqValue));
	}

}
