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

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.sun.net.httpserver.Headers;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import wxrmq.data.remote.ContactList;
import wxrmq.data.remote.InitResponse;
import wxrmq.data.remote.UUIDResponse;
import wxrmq.domain.Account;
import wxrmq.domain.WxContact;
import wxrmq.domain.WxUser;
import wxrmq.domain.WxUser_Tag;
import wxrmq.utils.NetWork;

public class JoinServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		String mobile = req.getParameter("mobile");
		String agreement = req.getParameter("agreement");
		String password = req.getParameter("password");
		String hostName = NetWork.getDomain(req.getParameter("redirect_uri"));
		
		String mobileCode = mobile + "_" + req.getParameter("mobileCode");
		resp.setHeader("Content-type", "text/html;charset=UTF-8");
		
		if (agreement == null || agreement.isEmpty()){
			 resp.setStatus(403);
			 resp.getWriter().write("请接收协议");
			return;
		}
		if(mobile.isEmpty() || password.isEmpty()){
			 resp.setStatus(403);
			 resp.getWriter().write("手机号码或密码不合法");
			return;
		}
		if(!mobileCode.equals(req.getSession().getAttribute("mobileCode"))){
			 resp.setStatus(403);
			 resp.getWriter().write("手机验证码不正确");
			return;
		}
		
		
		Account account = RmqDB.getById(Account.class, mobile);
		try{
			if (account == null){
				account = Account.createAccount(mobile, password);
				req.getSession(true).setAttribute("account", account);
		
			InitResponse initResponse = (InitResponse) req.getSession().getAttribute("initResponse");
			ContactList contactList= (ContactList) req.getSession().getAttribute("contactList");
			initResponse.getUser().setMobile(account.getMobile());
			SessionFactory dbfactory = RmqDB.getDBFactory();
			Session session = dbfactory.openSession();
			Transaction tx = session.beginTransaction();
			try{
				initResponse.getUser().setHeadImgBase64(NetWork.getImageBase64("https://" +hostName
			+initResponse.getUser().getHeadImgUrl(), NetWork.buildClient(req)));
				session.save(account);
				Query query=session.createQuery("delete WxContact s where s.uin=" + initResponse.getUser().getUin());
				query.executeUpdate();
				session.saveOrUpdate(initResponse.getUser());
				int i =0;
				for(WxContact wxContact : contactList.getMemberList()){
						wxContact.setSeq(i ++);
						wxContact.setUin(initResponse.getUser().getUin());
						session.save(wxContact);
				
				}
			tx.commit();
			}catch(HibernateException e){
				e.printStackTrace();
				tx.rollback();
			}
			session.close();
			
			}else{
				 resp.setStatus(403);
				 resp.getWriter().write("此用户已经存在");
			}
			
		}catch(Exception e){
		   resp.setStatus(500);
		   resp.getWriter().write(e.getMessage());
		}
		
	}
	

}
