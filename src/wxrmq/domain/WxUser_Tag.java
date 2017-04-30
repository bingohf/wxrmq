package wxrmq.domain;

import java.io.Serializable;
import java.math.BigInteger;

public class WxUser_Tag implements Serializable {
	
	public static final int TYPE_INTEREST = 0;
	public static final int TYPE_AGE = 1;
	
	private Long uin;
	private int type;
	private String label;
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
	public Long getUin() {
		return uin;
	}
	public void setUin(Long uin) {
		this.uin = uin;
	}


}
