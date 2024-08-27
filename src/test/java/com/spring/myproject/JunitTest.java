package com.spring.myproject;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
//import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@Log4j2
//@TestPropertySource(locations = {"classpath:application-test.properties"})
public class JunitTest {

  @DisplayName("1+2는 3이다")
  @Test
  public void junitTest01(){
    int a = 1;
    int b = 2;
    int sum = 3;

    // import org.junit.jupiter.api.Assertions;

    // 첫번째 인자: 기대값, 두번째 인자: 실제 검증할 값
    Assertions.assertEquals(a+b, sum);

    // import org.assertj.core.api.Assertions;
    // 값이 같은지를 검사
    //Assertions.assertThat(a+b).isEqualTo(sum);
    // 값이 다른지를 검사
    //Assertions.assertThat(a+b).isNotEqualTo(sum);
  }
}



/*
JUnit: 자바 프로그래밍 언어요 단위 테스트 프레임워크
  - Spring Test & Spring Boot Test : 스프링 부트 애플리케이션을 위한 통합 테스트 지원
AssertJ : 검증문인 어설션을 작성하는 데 사용되는 라이브러리

Hamcrest: 표현식을 보다 이해하기 쉽게 만드는 데 사용되는 Matcher라이브러리
Mockito: 텍스트에 사용할 가짜 객체인 목 객체를 쉽게 생성,관리하고, 검증할 수 있게 지원하는 테스트 프레임워크
JSONasser: JSON용 어설션  라이브러리
JsonPath: JSON데이터에서 특정 데이터를 선택하고 검색하기 위한 라이브러리

단위 테스트 - 작성코드가 의도대로 작동하는지 작은 단위로 검증
   - 보통 메서드 단위로 실행


 */