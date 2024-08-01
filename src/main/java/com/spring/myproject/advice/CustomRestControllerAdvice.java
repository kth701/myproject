package com.spring.myproject.advice;



import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.HashMap;
import java.util.Map;


// 컨트롤러에서 발생하는 예외에 JSON과 같은 순수한 응답 메시지를 생성해서 보낼 수 있음.
@RestControllerAdvice
@Log4j2
public class CustomRestControllerAdvice {

  // RestControlle에서 BindException예외가 발생하면
  // handleBindException 메서드가 수행
  @ExceptionHandler(BindException.class)
  @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
  public ResponseEntity<Map<String, String>> handleBindException( BindException e ){

    log.error(e);

    Map<String, String> errorMap = new HashMap<>();

    if (e.hasErrors()){//에러가 존재하면

      BindingResult bindingResult = e.getBindingResult();
      bindingResult.getFieldErrors().forEach(fieldError -> {
        // 클라이언트에게 보낼 에러정보를 담은 Map객체
        errorMap.put(fieldError.getField(), fieldError.getCode());
      });
    }

    return ResponseEntity.badRequest().body(errorMap);
    //return new ResponseEntity<List<MemberVO>>(list, HttpStatus.INTERNAL_SERVER_ERROR); // 500 code
    //return new ResponseEntity<List<MemberVO>>(list, HttpStatus.OK);// 200 code
  }

}
