package wxrmq.domain;

import com.google.gson.Gson;

import wxrmq.data.remote.WxUserInfo;

public class MMapping {
   public static WxUser toWxUser(WxUserInfo wxUserInfo) {
	   WxUser wxUser = new WxUser();
		wxUser.setUin(wxUserInfo.getUin());
		wxUser.setNickName(wxUserInfo.getNickName());
		wxUser.setFriendsCount(wxUserInfo.getFriendInfo().getFriendsCount());
		wxUser.setSex(wxUserInfo.getSex());
	   wxUser.setInfoJson(new Gson().toJson(wxUserInfo));
	   wxUser.setMalePercent(wxUserInfo.getMalePercent());
		return wxUser;
	
   }
}
