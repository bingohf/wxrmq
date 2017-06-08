package wxrmq.data.remote;

import java.util.ArrayList;

import wxrmq.domain.TagValue;
import wxrmq.domain.WxContact;
import wxrmq.domain.WxUser;

public class WxUserInfo {
	private Long uin;
	private String nickName;
	private Integer sex;
	private String province;
	private String city;
	private String mobile;
	private Float quota;
	private Integer age;
	private String memo;
	private ArrayList<String> industry;
	private FriendInfo friendInfo = new FriendInfo();
	private Float malePercent;
	
	public Long getUin() {
		return uin;
	}


	public void setUin(Long uin) {
		this.uin = uin;
	}



	public String getNickName() {
		return nickName;
	}


	public void setNickName(String nickName) {
		this.nickName = nickName;
	}


	public Integer getSex() {
		return sex;
	}


	public void setSex(Integer sex) {
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


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
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


	public ArrayList<String> getIndustry() {
		return industry;
	}


	public void setIndustry(ArrayList<String> industry) {
		this.industry = industry;
	}


	public FriendInfo getFriendInfo() {
		return friendInfo;
	}


	public void setFriendInfo(FriendInfo friendInfo) {
		this.friendInfo = friendInfo;
	}


	public Float getMalePercent() {
		return malePercent;
	}


	public void setMalePercent(Float malePercent) {
		this.malePercent = malePercent;
	}


	public class FriendInfo{
		private ArrayList<TagValue> sexs = new ArrayList<>();
		private ArrayList<TagValue> citys = new ArrayList<>();
		private Integer friendsCount ;
		
		public ArrayList<TagValue> getSexs() {
			return sexs;
		}
		public void setSexs(ArrayList<TagValue> sexs) {
			this.sexs = sexs;
		}
		public ArrayList<TagValue> getCitys() {
			return citys;
		}
		public void setCitys(ArrayList<TagValue> citys) {
			this.citys = citys;
		}
		public Integer getFriendsCount() {
			return friendsCount;
		}
		public void setFriendsCount(Integer friendsCount) {
			this.friendsCount = friendsCount;
		}
	}
}
