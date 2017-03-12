package wxrmq.data.remote;

import wxrmq.domain.WxUser;

public class ContactList {
	private WxUser[] memberList;

	public WxUser[] getMemberList() {
		return memberList;
	}

	public void setMemberList(WxUser[] memberList) {
		this.memberList = memberList;
	}

}
