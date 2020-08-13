package com.scm.product.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EntityScan(basePackages = "com.scm.product.management.entity")
@EnableFeignClients
@SpringBootApplication
public class MsProductManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsProductManagementApplication.class, args);
	}

}
