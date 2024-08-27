package com.spring.myproject.controller;


import com.google.gson.Gson;
import com.spring.myproject.dto.MemberDTO;
import com.spring.myproject.entity.Member;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
@RequestMapping("/test")
public class SampleJSONController {

  @Autowired
  private Gson gson;

  @GetMapping("/helloArr")
  public String[] helloArr(){
    log.info("RestController => helloArr => 배열구조 자료 반환(전송)... ");

    return new String[] {"홍길동","홍길순","동길이"};
  }

  @PostMapping(value="/test",produces = "application/json")
  public ResponseEntity<MemberDTO> getMember(@RequestBody MemberDTO memberDTO){
    log.info("\n=> RestController url='/test/test' receive data: "+memberDTO);

    return ResponseEntity.status(HttpStatus.CREATED).body(memberDTO);
  }

  @PostMapping(value="/test2",produces = "application/json")
  public ResponseEntity<List<MemberDTO>> getMembers(@RequestBody List<MemberDTO> memberDTO){
    log.info("\n=> RestController url='/test2' receive data: "+memberDTO);

    return ResponseEntity.status(HttpStatus.CREATED).body(memberDTO);
  }

  @PostMapping(value="/test3",produces = "application/json")
  public ResponseEntity<MemberDTO> getMembers(@ModelAttribute MemberDTO memberDTO){
    log.info("\n=> RestController url='/test3' receive data: "+memberDTO);

    return ResponseEntity.status(HttpStatus.CREATED).body(memberDTO);
  }
}
