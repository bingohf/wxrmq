package wxrmq.domain;

import java.util.Date;

public class SmSLog {
	private String mobile;
	private String ip;
	private Date lastSentDate;
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Date getLastSentDate() {
		return lastSentDate;
	}
	public void setLastSentDate(Date lastSentDate) {
		this.lastSentDate = lastSentDate;
	} 
}
