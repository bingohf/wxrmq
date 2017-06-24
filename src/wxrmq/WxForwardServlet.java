package wxrmq;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wxrmq.domain.Account;
import wxrmq.domain.WxUser;

public class WxForwardServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String uin = req.getParameter("wxid");
		if(isAuth(uin, req)){
			RequestDispatcher disp = req.getRequestDispatcher("wx-admin.html");
			disp.forward(req, resp);
		}else{
			RequestDispatcher disp = req.getRequestDispatcher("wx-user.html");
			disp.forward(req, resp);
		}
		
	}
	private boolean isAuth(String uin, HttpServletRequest req){
		WxUser wxUser = RmqDB.getById(WxUser.class, uin);
		Account account = (Account) req.getSession(true).getAttribute("account");
		if(account == null || !account.getMobile().equals(wxUser.getMobile())){
			return false;
		}
		return true;
	}
}
