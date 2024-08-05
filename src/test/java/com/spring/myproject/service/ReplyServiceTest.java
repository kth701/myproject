package com.spring.myproject.service;

import com.spring.myproject.dto.PageRequestDTO;
import com.spring.myproject.dto.PageResponseDTO;
import com.spring.myproject.dto.ReplyDTO;
import com.spring.myproject.entity.Board;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class ReplyServiceTest {

  @Autowired
  private ReplyService replyService;

  @Test
  @DisplayName("Reply 등록")
  public void testRegisterReply(){

//    Board board = Board.builder().bno(513L).build();

  // 클라이언트에 넘겨받은 값을 ReplyDTO객체에 저장
    ReplyDTO replyDTO = ReplyDTO.builder()
        .replyText("ReplyDTO text2")
        .replyer("replyer2")
        .bno(513L)
//        .board(board)
        .build();

    // 댓글 등록 서비스 호출하여 실행
    log.info(replyService.register(replyDTO));
  }

  @Test
  @DisplayName("특정 게시글에 대한 댓글 조회")
  public void testReplyListOBoard(){
    Long bno = 513L;

    PageRequestDTO pageRequestDTO = new PageRequestDTO();

    PageResponseDTO<ReplyDTO> responseDTO = replyService.getListOBoard(bno, pageRequestDTO);
    responseDTO.getDtoList().stream().forEach( reply -> log.info("=> "+reply));


  }
}