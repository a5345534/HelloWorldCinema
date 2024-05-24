package com;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.config","com.filter","com.service","templates","com.controller","com.util","com.dao","com.websocket"})
public class HelloWorldCinemaApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(HelloWorldCinemaApplication.class, args);
	}
}
