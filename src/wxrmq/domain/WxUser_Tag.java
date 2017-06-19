package wxrmq.domain;

import java.io.Serializable;
import java.math.BigInteger;

public class WxUser_Tag implements Serializable {
	
	public static final int TYPE_INDUSTRY = 1;
	public static final int TYPE_AGE = 1;
	public static final int TYPE_CITY = 2;
	
	private String uin;
	private int type;
	private String label;
	private String subLabel;
	private int count;
	

	public int getType() {
		return type;
	}
	public void setType(int tag_type) {
		this.type = tag_type;
	}
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
	public String getUin() {
		return uin;
	}
	public void setUin(String uin) {
		this.uin = uin;
	}
	public String getSubLabel() {
		return subLabel;
	}
	public void setSubLabel(String subLabel) {
		this.subLabel = subLabel;
	}


}
