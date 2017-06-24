package wxrmq.domain;

import java.math.BigInteger;
import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import wxrmq.RmqDB;

public class Account {
	private String mobile;
	private String password;
	private Date created;
	private Date updated;
	public static Account createAccount(String mobile, String password) {
		Account account = new Account();
		account.mobile = mobile;
		account.password = password;
		account.setCreated(new Date());
		account.setUpdated(new Date());
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

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}


}
