package com.data.warehouse.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
/**
 * 连接池配置
 * 
 * @Title: SwaggerConfig
 * @Description:
 * @author: 尹雄标
 * @date 2018年8月4日
 */
@Configuration
public class DruidConfig {
	
	@Autowired
	private Environment environment;
	
	@Bean
	@Qualifier("hiveJdbcDataSource")
	public DataSource dataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(environment.getProperty("hive.jdbc"));
		dataSource.setDriverClassName(environment.getProperty("hive.driver-class-name"));
		dataSource.setUsername(environment.getProperty("hive.user"));
		dataSource.setPassword(environment.getProperty("hive.password"));
		dataSource.setTestWhileIdle(Boolean.valueOf(environment.getProperty("hive.testWhileIdle")));
		dataSource.setMaxActive(Integer.valueOf(environment.getProperty("hive.max-active")));
		dataSource.setInitialSize(Integer.valueOf(environment.getProperty("hive.initialSize")));
		dataSource.setRemoveAbandoned(Boolean.valueOf(environment.getProperty("hive.removeAbandoned")));
		dataSource.setRemoveAbandonedTimeout(Integer.valueOf(environment.getProperty("hive.removeAbandonedTimeout")));
		return dataSource;
	}

	@Bean(name = "hiveJdbcTemplate")
	public JdbcTemplate hiveJdbcTemplate(@Qualifier("hiveJdbcDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
}
