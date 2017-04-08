package wxrmq.data.remote;

import wxrmq.domain.WxUser;

public class RecordRmq {
	
	private WxUser curUser;
	private WxUser[] memberList;

	public WxUser[] getMemberList() {
		return memberList;
	}

	public void setMemberList(WxUser[] memberList) {
		this.memberList = memberList;
	}

	public WxUser getCurUser() {
		return curUser;
	}

	public void setCurUser(WxUser curUser) {
		this.curUser = curUser;
	}

}
