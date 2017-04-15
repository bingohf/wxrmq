package wxrmq.domain;

import java.math.BigInteger;

public class WxUser_Tag {
	private String id;
	private Long unid;
	private int type;
	private String label;
	private int count;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Long getUnid() {
		return unid;
	}
	public void setUnid(Long unid) {
		this.unid = unid;
	}
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


}
