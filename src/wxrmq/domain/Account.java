package wxrmq.domain;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import wxrmq.RmqDB;

public class Account {
	private String mobile;
	private String password;
	private int wx_unid;

	public static Account createAccount(String mobile, String password) {
		Account account = new Account();
		account.mobile = mobile;
		account.password = password;
		RmqDB.save(account);
		return account;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getWx_unid() {
		return wx_unid;
	}

	public void setWx_unid(int wx_unid) {
		this.wx_unid = wx_unid;
	}
}
