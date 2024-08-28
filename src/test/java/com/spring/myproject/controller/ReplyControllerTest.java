package com.spring.myproject.controller;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@Commit
//@TestPropertySource(locations = {"classpath:application-test.properties"})
@Log4j2
class ReplyControllerTest {
  @Autowired
  protected MockMvc mockMvc;


  @Test
  void register() {
  }

  @Test
  void getList() throws Exception {

  }

  @Test
  void modify() {
  }

  @Test
  void remove() {
  }

  @Test
  void getReplyDTO() {
  }
}