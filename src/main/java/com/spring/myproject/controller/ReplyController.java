package com.spring.myproject.controller;


import com.spring.myproject.dto.ReplyDTO;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.naming.Binding;
import java.util.Map;

// REST방식 : 주로 XML, JSON형태의 문자열을 전송하고 이를 컨트롤러에서 처리하는 방식
@RestController
@RequestMapping("/replies")
@Log4j2
public class ReplyController {

  /*
  @PostMapping(value="/", consumes = MediaType.APPLICATION_JSON_VALUE)// 전송받은 data 종류 명시
  public ResponseEntity<Map<String, Long>> register(@RequestBody ReplyDTO replyDTO){
      log.info("=> replyDTO: "+replyDTO);

      Map<String, Long> resultMap = Map.of("rno", 222L);

    return ResponseEntity.ok(resultMap);
    // or => return new ResponseEntity(resultMap, HttpStatus.OK);
  }
  */

  // RestController예외처리 consumes = MediaType.APPLICATION_JSON_VALUE
  @PostMapping(value="/",consumes = MediaType.APPLICATION_JSON_VALUE )// 전송받은 data 종류 명시
  public Map<String, Long> register(
                    @Valid @RequestBody ReplyDTO replyDTO,
                    BindingResult bindingResult // 객체값 검증
                    ) throws BindException {

    log.info("=> replyDTO: "+replyDTO);
    log.info("=> bindingResult: "+bindingResult.toString());

    if (bindingResult.hasErrors()){ // 에러가 존재하면 BindException예외 발생시킴
      throw new BindException(bindingResult);

    }

    Map<String, Long> resultMap = Map.of("rno", 222L);

    return resultMap;
  }


  @PostMapping(value = "/test3", consumes = MediaType.APPLICATION_JSON_VALUE)
  public Map<String,Long> register2(
      @Valid @RequestBody ReplyDTO replyDTO,
      BindingResult bindingResult)throws BindException{

    log.info(replyDTO);

    if(bindingResult.hasErrors()){
      throw new BindException(bindingResult);
    }


    Map<String, Long> resultMap = Map.of("rno", 222L);

    return resultMap;
  }


}
