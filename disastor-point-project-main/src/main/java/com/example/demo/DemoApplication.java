package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// 💡 불필요한 import 제거: import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

/**
 * [스프링 부트 메인 실행 클래스]
 * - MySQL/MariaDB 데이터베이스(application.properties)와 정상 연동되어 전체 서비스를 구동합니다.
 */
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}