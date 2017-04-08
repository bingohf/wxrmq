package wxrmq.domain;


public class WxUser_Tag {
	private String id;
	private int unid;
	private int type;
	private String label;
	private int count;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getUnid() {
		return unid;
	}
	public void setUnid(int unid) {
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
