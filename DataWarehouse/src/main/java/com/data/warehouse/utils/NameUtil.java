package com.data.warehouse.utils;
/**
 * 名称命名（驼峰）转换
 * 
 * @Title: NameUtil
 * @Description:
 * @author: 尹雄标
 * @date 2019年6月4日
 */
public class NameUtil {

	public static final char UNDERLINE = '_';

	public static String getUnderlineName(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (Character.isUpperCase(c)) {
				sb.append(UNDERLINE);
				sb.append(Character.toLowerCase(c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString().substring(1);
	}

	public static String getCamelName(String param) {
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (c == UNDERLINE) {
				if (++i < len) {
					sb.append(Character.toUpperCase(param.charAt(i)));
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		System.out.println(getUnderlineName("AbcDeft"));
		System.out.println(getCamelName("abc_deft"));
	}

}
