# SpringBoot+druid操作hive

## 1.依赖的添加

```xml
<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-jdbc</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.data</groupId>
			<artifactId>spring-data-hadoop</artifactId>
			<version>${hadoop.version}</version>
		</dependency>

		<dependency>
			<groupId>com.alibaba</groupId>
			<artifactId>druid</artifactId>
			<version>${druid.version}</version>
		</dependency>

		<dependency>
			<groupId>org.slf4j</groupId>
			<artifactId>slf4j-nop</artifactId>
		</dependency>

		<dependency>
			<groupId>org.apache.hive</groupId>
			<artifactId>hive-jdbc</artifactId>
			<version>${hive.version}</version>
			<exclusions>
				<exclusion>
					<groupId>org.eclipse.jetty.aggregate</groupId>
					<artifactId>*</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
		<dependency>
			<groupId>jdk.tools</groupId>
			<artifactId>jdk.tools</artifactId>
			<version>1.8</version>
		</dependency>

		<!-- swagger2 -->
		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger2</artifactId>
			<version>${swagger.version}</version>
		</dependency>

		<!-- swagger2-UI -->
		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger-ui</artifactId>
			<version>${swagger.version}</version>
		</dependency>

	</dependencies>
```

在添加依赖时，要注意jdk.tools这个依赖，hadoop依赖引用了此依赖，所以需要用到这个依赖，但是这个依赖不在中央仓库中，这个依赖是jdk中的一个jar包，如果直接这样引用不做任何操作，pom文件会报错，解决办法是从jdk中复制这个jar出来放到自己的本地仓库中，并对jar包进行重命名，是的本地仓库中该依赖能被引入

![1559614076177](C:\Users\84644\AppData\Roaming\Typora\typora-user-images\1559614076177.png)

![1559614211939](C:\Users\84644\AppData\Roaming\Typora\typora-user-images\1559614211939.png)

## 2.yml配置

### 2.1application.yml配置

```yaml
spring:
  profiles:
    active: dev
```

### 2.2application-dev.yml配置

```yml
server: 
  # 端口号
  port: 8084
  # 是否启用压缩
  compression: 
    enabled: true
  # tomcat配置
  tomcat: 
    uri-encoding: UTF-8

spring:
  mvc:
    favicon:
      enabled: false
  main:
    allow-bean-definition-overriding: true

# hive配置
hive: 
  jdbc: jdbc:hive2://10.10.11.11:10000/pre_middle
  type: com.alibaba.druid.pool.DruidDataSource
  driver-class-name: org.apache.hive.jdbc.HiveDriver
#  user: 
#  password:
  # 最大连接池数量
  max-active: 5
  # 初始化时建立物理连接的个数
  initialSize: 3
  # 获取连接时最大等待时间，单位毫秒。配置了maxWait之后，缺省启用公平锁，并发效率会有所下降，如果需要可以通过配置useUnfairLock属性为true使用非公平锁。
  maxWait: 60000
  # 最小连接池数量
  minIdle: 1
  # 有两个含义： 1)Destroy线程会检测连接的间隔时间       2)testWhileIdle的判断依据，详细看testWhileIdle属性的说明
  timeBetweenEvictionRunsMillis: 60000
  minEvictableIdleTimeMillis: 300000
  # 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
  testWhileIdle: true
  # 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
  testOnBorrow: false
  # 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
  testOnReturn: false
  # 是否缓存preparedStatement，也就是PSCache。PSCache对支持游标的数据库性能提升巨大，比如说oracle。在mysql下建议关闭。
  poolPreparedStatements: true
  # 要启用PSCache，必须配置大于0，当大于0时，poolPreparedStatements自动触发修改为true。在Druid中，不会存在Oracle下PSCache占用内存过多的问题，可以把这个数值配置大一些，比如说100
  maxOpenPreparedStatements: 50
  # 是否自动回收超时连接 
  removeAbandoned: true
  # 超时时间(以秒数为单位)
  removeAbandonedTimeout: 180
```

## 3.java配置类

### 3.1druid配置

```java
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
 * @Title: DruidConfig
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

```

### 3.2Swagger配置

这里的配置要注意扫描的包，如果扫描的包没有配置好，swagger页面讲看不到接口信息

```java
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
				.apis(RequestHandlerSelectors.basePackage("com.data.warehouse"))
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
				.contact(new Contact("尹雄标", "大数据平台服务接口", null))
				.version("版本号:" + "1.0.0").build();
	}
}
```

```java

```

3.4启动类

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(value = { "com.data.warehouse" })
@SpringBootApplication
public class DataWarehouseApplication {
	public static void main(String[] args) {
		SpringApplication.run(DataWarehouseApplication.class, args);
	}
}
```

## 4.测试

### 4.1接口测试

```java
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


/**
 * 测试hive连接
 * @Title: HiveController
 * @Description:
 * @author:Administrator
 * @date 2018年8月4日
 */
@RestController
@RequestMapping("/hive")
public class HiveController {
	@Autowired
	@Qualifier("hiveJdbcTemplate")
	private JdbcTemplate hiveJdbcTemplate;

	@ApiOperation(value="测试查询", notes = "测试查询接口")
	//@ApiImplicitParams({
		//@ApiImplicitParam(name = "id", value = "图书ID", required = true, dataType = "Long",paramType = "path")
	//})
	@RequestMapping(value = "/select", method = RequestMethod.GET)
	public List select() {
		long startTime = System.currentTimeMillis();
		List rows = hiveJdbcTemplate.queryForList("SELECT T_CSX_QRWB.name, count(T_HR_A010101.hdsd0001006) AS total_nums "
				+ "FROM T_HR_A010101 "
				+ "LEFT JOIN T_CSX_QRWB ON (T_HR_A010101.exhdsd0001018 = T_CSX_QRWB.exhdsd0001018)"
				+ "GROUP BY T_CSX_QRWB.name");
		long endTime = System.currentTimeMillis();
		System.out.println("查询执行时间:" + (endTime - startTime));
		return rows;
	}
}
```

![1559620604889](C:\Users\84644\AppData\Roaming\Typora\typora-user-images\1559620604889.png)

### 4.2Swagger测试

![1559627267734](C:\Users\84644\AppData\Roaming\Typora\typora-user-images\1559627267734.png)





