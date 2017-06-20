package wxrmq.utils;

import java.util.ArrayList;

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
		if (null != text && text.length()>2){
			return text.substring(0, 2) + StringUtils.repeat("©~", text.length()-4) + text.substring(text.length()-2);
		}
		return "";
	}
	
	public static String concat(ArrayList<String> list, String join) {
		StringBuilder sBuilder = new StringBuilder();
		for(int i =0; i<list.size(); ++i){
			if(i>0){
				sBuilder.append(join);
			}
			sBuilder.append(list.get(i));
			
		}
		return sBuilder.toString();
	}
}
