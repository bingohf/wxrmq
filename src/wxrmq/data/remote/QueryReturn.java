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
		private Long unid;
		private String nickName;
		private int sex;
		private String queryTag;
		private int tagCount;
		private int FriendsCount;
		public Long getUnid() {
			return unid;
		}
		public void setUnid(Long unid) {
			this.unid = unid;
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
		public String getQueryTag() {
			return queryTag;
		}
		public void setQueryTag(String queryTag) {
			this.queryTag = queryTag;
		}
		public int getTagCount() {
			return tagCount;
		}
		public void setTagCount(int tagCount) {
			this.tagCount = tagCount;
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
		private String headImgBase64;
	}
}
