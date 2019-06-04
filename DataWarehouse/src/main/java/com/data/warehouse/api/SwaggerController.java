package com.data.warehouse.api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.data.warehouse.controller.BaseController;


/**
 * swagger 接口
 * 
 * @Title: SwaggerController
 * @Description:
 * @author:Administrator
 * @date 2018年8月4日
 */
@Controller
@RequestMapping("/tool/swagger")
public class SwaggerController extends BaseController {
	@GetMapping()
	public String index() {
		return redirect("/swagger-ui.html");
	}
}
