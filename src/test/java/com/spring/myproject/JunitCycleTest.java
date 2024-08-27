package com.spring.myproject;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest // 테스트용 애플리케이션 컨텍스트 생성
@Log4j2
public class JunitCycleTest {

  // 전체 테스트 시작하기 전에 1회 실행, static선언
  @BeforeAll
  static void beforAll(){
    // db연결하거나, 테스트환경 초기화
    log.info("@BeforeAll()호출: 전체 테스트 시작하기전 실행 주기에서 한번 만 호출");
  }

  @BeforeEach
  public void beforeEach(){
    // 객체를 초기화 하거나 텍스트에 피룡한 값을 미리설정 할 경우
    log.info("@BeforeEach():테스트 케이스 시작하기 전에 매번 실행");
  }

  @Test
  public void test1(){
    log.info("test1");
  }
  @Test
  public void test2(){
    log.info("test2");
  }

  @AfterEach
  public void afterEach(){
    // 특정 데이터 삭제
    log.info("@AfterEach():테스트 케이스 종료하기 전에 매번 실행");
  }
  // 전체 테스트 종료하기 전에 1회 실행, static선언
  @AfterAll
  static void afterAll(){
    //  db연결 종료, 공통으로 사용하는 자원 해제
    log.info("@BeforeAll()호출: 전체 테스트 종료하기전 실행 주기에서 한번 만 호출");
  }
}
