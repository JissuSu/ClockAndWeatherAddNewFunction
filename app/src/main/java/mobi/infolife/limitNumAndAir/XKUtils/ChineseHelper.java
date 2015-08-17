package mobi.infolife.limitNumAndAir.XKUtils;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

public class ChineseHelper {
	private static final Properties CHINESE_TABLE = PinyinResource
			.getChineseTable();

	public static char convertToSimplifiedChinese(char c) {
		if (isTraditionalChinese(c)) {
			return CHINESE_TABLE.getProperty(String.valueOf(c)).charAt(0);
		}
		return c;
	}

	public static char convertToTraditionalChinese(char c) {
		String hanzi = String.valueOf(c);
		if (CHINESE_TABLE.containsValue(hanzi)) {
			Iterator<Entry<Object, Object>> itr = CHINESE_TABLE.entrySet()
					.iterator();
			while (itr.hasNext()) {
				Entry<Object, Object> e = itr.next();
				if (e.getValue().toString().equals(hanzi)) {
					return e.getKey().toString().charAt(0);
				}
			}
		}
		return c;
	}

	public static String convertToSimplifiedChinese(String str) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0, len = str.length(); i < len; i++) {
			char c = str.charAt(i);
			sb.append(convertToSimplifiedChinese(c));
		}
		return sb.toString();
	}

	public static String convertToTraditionalChinese(String str) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0, len = str.length(); i < len; i++) {
			char c = str.charAt(i);
			sb.append(convertToTraditionalChinese(c));
		}
		return sb.toString();
	}

	public static boolean isTraditionalChinese(char c) {
		return CHINESE_TABLE.containsKey(String.valueOf(c));
	}

	public static boolean isChinese(char c) {
		String regex = "[\\u4e00-\\u9fa5]";
		return String.valueOf(c).matches(regex);
	}
}
