package wxrmq.data.remote;

import java.util.ArrayList;

import wxrmq.domain.WxUser;

public class RmqValue {
	private WxUser wxUser;
	private ArrayList<Item> sexValue = new ArrayList<>();
	private ArrayList<Item>  cityValue = new ArrayList<>();

	
	
	public ArrayList<Item> getSexValue() {
		return sexValue;
	}



	public void setSexValue(ArrayList<Item> sexValue) {
		this.sexValue = sexValue;
	}



	public ArrayList<Item> getCityValue() {
		return cityValue;
	}



	public void setCityValue(ArrayList<Item> cityValue) {
		this.cityValue = cityValue;
	}



	public WxUser getWxUser() {
		return wxUser;
	}



	public void setWxUser(WxUser wxUser) {
		this.wxUser = wxUser;
	}



	public  static class Item{
		private String label;
		private int count;
		
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
		}
		
	}
}
