package wxrmq.data.remote;

import java.awt.datatransfer.FlavorTable;
import java.util.ArrayList;

public class QueryReturn {
	private int totalCont;
	private ArrayList<Item> items = new ArrayList<>();
	public int getTotalCont() {
		return totalCont;
	}
	public void setTotalCont(int totalCont) {
		this.totalCont = totalCont;
	}

	public ArrayList<Item> getItems() {
		return items;
	}
	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

	public static class Item{
		private String uin;
		private String nickName;
		private int sex;
		private int friendsCount;
		private Float quota;
		private Integer age;
		private String city;
		private String industry ;
		private Float malePercent;
		private String memo;
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
		public int getFriendsCount() {
			return friendsCount;
		}
		public void setFriendsCount(int friendsCount) {
			this.friendsCount = friendsCount;
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
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public Integer getAge() {
			return age;
		}
		public void setAge(Integer age) {
			this.age = age;
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

	}
}
