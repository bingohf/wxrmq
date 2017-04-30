package wxrmq.data.remote;

import wxrmq.domain.WxContact;
import wxrmq.domain.WxUser;

public class WxUserInfo {
	private WxUser wxUser;
	private WxContact[] memberList;
	
	public WxContact[] getMemberList() {
		return memberList;
	}
	public void setMemberList(WxContact[] memberList) {
		this.memberList = memberList;
	}
	public WxUser getWxUser() {
		return wxUser;
	}
	public void setWxUser(WxUser wxUser) {
		this.wxUser = wxUser;
	}
}
