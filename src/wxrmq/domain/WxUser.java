package wxrmq.domain;

import java.math.BigInteger;

public class WxUser {


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



	public int getFriendsCount() {
		return friendsCount;
	}

	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getUin() {
		return uin;
	}

	public void setUin(String uin) {
		this.uin = uin;
	}

	public Float getQuota() {
		return quota;
	}

	public void setQuota(Float quota) {
		this.quota = quota;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getInfoJson() {
		return infoJson;
	}

	public void setInfoJson(String infoJson) {
		this.infoJson = infoJson;
	}

	public Float getMalePercent() {
		return malePercent;
	}

	public void setMalePercent(Float malePercent) {
		this.malePercent = malePercent;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}



	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}



	public String getWxId() {
		return wxId;
	}

	public void setWxId(String wxId) {
		this.wxId = wxId;
	}



	private String uin;
	private String nickName;
	private int sex;
	private String province;
	private String city;
	private int friendsCount;
	private String mobile;
	private Float quota;
	private Integer age;
	private String memo;
	private String industry;
	private Float malePercent;
	private String infoJson;
	private String headImgUrl;
	private String wxId;

}
