package com.data.warehouse.controller;

import com.data.warehouse.utils.StringUtils;

/**
 * @Title: BaseController
 * @Description:
 * @author: 尹雄标
 * @date 2019年6月4日
 */
public class BaseController {
	public String redirect(String url) {
		return StringUtils.format("redirect:{}", url);
	}
}
