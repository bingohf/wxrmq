package wxrmq.data.remote;

import wxrmq.domain.WxContact;
import wxrmq.domain.WxUser;

public class ContactList {
	private WxContact[] memberList;

	public WxContact[] getMemberList() {
		return memberList;
	}

	public void setMemberList(WxContact[] memberList) {
		this.memberList = memberList;
	}

}
