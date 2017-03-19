package wxrmq.domain;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import wxrmq.RmqDB;

public class Account {
	private String mobile;
	private String password;
	
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
}