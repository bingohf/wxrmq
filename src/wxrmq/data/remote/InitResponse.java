package wxrmq.data.remote;


import wxrmq.domain.WxUser;

public class InitResponse {
	private WxUser user;

	public WxUser getUser() {
		return user;
	}

	public void setUser(WxUser user) {
		this.user = user;
	}
}
