package wxrmq.domain;

import java.io.Serializable;
import java.math.BigInteger;

public class WxContact implements Serializable{


	private Long uin;
	private int seq;
	private String nickName;
	private int sex;
	private String signature;
	private int starFriend;
	private String province;
	private String city;
	


	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public int getStarFriend() {
		return starFriend;
	}
	public void setStarFriend(int starFriend) {
		this.starFriend = starFriend;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Long getUin() {
		return uin;
	}
	public void setUin(Long uin) {
		this.uin = uin;
	}


}
