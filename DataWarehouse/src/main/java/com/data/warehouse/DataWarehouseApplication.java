package com.data.warehouse;

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
