package wxrmq.utils;

import org.apache.commons.lang3.StringUtils;

public class TextUtils {
	public static boolean isEmpty(String str){
		return str == null || str.isEmpty();
	}
	
	public static String getPathParam(String url) {
		String[] ss = url.split("[\\/\\?]");
		return ss[ss.length -1];
	}
	
	public static String getPathParam(String url, int index) {
		String[] ss = url.split("[\\/\\?]");
		return ss[ss.length -1 + index];
	}
	
	public static String markText(String text) {
		if (text.length()>2){
			return text.substring(0, 2) + StringUtils.repeat("©~", text.length()-4) + text.substring(text.length()-2);
		}
		return text;
	}
}
