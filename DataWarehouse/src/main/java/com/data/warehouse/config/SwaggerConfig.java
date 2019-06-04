package com.data.warehouse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2的接口配置
 * 
 * @Title: SwaggerConfig
 * @Description:
 * @author: 尹雄标
 * @date 2019年6月4日
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	/**
	 * 创建API
	 */
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				// 详细定制
				.apiInfo(apiInfo()).select()
				// 指定当前包路径
				.apis(RequestHandlerSelectors.basePackage("cn.sinocon.hive.api"))
				// 扫描所有 .apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build();
	}

	/**
	 * 添加摘要信息
	 */
	private ApiInfo apiInfo() {
		// 用ApiInfoBuilder进行定制
		return new ApiInfoBuilder().title("大数据平台接口文档")
				.description("描述：")
				.contact(new Contact("大数据平台服务接口", null, null))
				.version("版本号:" + "1.0.0").build();
	}
}
