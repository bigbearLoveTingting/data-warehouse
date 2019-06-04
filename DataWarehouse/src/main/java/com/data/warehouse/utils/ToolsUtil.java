package com.data.warehouse.utils;

import java.util.Arrays;
import java.util.UUID;

/**
 * 普通工具类
 * @Title: ToolsUtil
 * @Description:
 * @author: 尹雄标
 * @date 2019年6月4日
 */
public class ToolsUtil {

	/**
	 * 获取uuid
	 *
	 * @return
	 */
	public static String getUUID() {
		String s = UUID.randomUUID().toString();
		return s.replaceAll("-", "");
	}

	/**
	 * 数组合并
	 * 
	 * @param first
	 * @param second
	 * @return
	 */
	public static Object[] concat(Object[] first, Object second) {
		Object[] result = Arrays.copyOf(first, first.length + 1);
		System.arraycopy(new Object[] { second }, 0, result, first.length, 1);
		return result;
	}

	/**
	 * 数组合并
	 * 
	 * @param first
	 * @param second
	 * @return
	 */
	public static Object[] concat(Object[] first, Object... second) {
		Object[] result = new Object[first.length + second.length];
		System.arraycopy(first, 0, result, 0, first.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

}
