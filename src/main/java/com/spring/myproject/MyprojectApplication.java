package com.spring.myproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing	// AuditingEntityListener 기능 활성화
public class MyprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyprojectApplication.class, args);
	}

}



/*
JPA 와 DB 설치(MariaDB, Oracle)
DB Test => Entity작성 (DB설계), Repository설계(DB동작,저장,수정,삭제,조회), DB 드라이브 설정


 */