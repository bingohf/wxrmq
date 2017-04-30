package wxrmq.data.remote;

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
		private Long uin;
		private String nickName;
		private int sex;
		private int FriendsCount;
		private Float quota;
		private Integer age;
		private String city;
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
			return FriendsCount;
		}
		public void setFriendsCount(int friendsCount) {
			FriendsCount = friendsCount;
		}
		public String getHeadImgBase64() {
			return headImgBase64;
		}
		public void setHeadImgBase64(String headImgBase64) {
			this.headImgBase64 = headImgBase64;
		}
		public Long getUin() {
			return uin;
		}
		public void setUin(Long uin) {
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
		private String headImgBase64;
	}
}
