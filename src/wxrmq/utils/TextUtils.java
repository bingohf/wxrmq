package wxrmq.utils;

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
}
