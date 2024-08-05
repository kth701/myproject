package com.spring.myproject.repository;

import com.spring.myproject.dto.BoardDTO;
import com.spring.myproject.entity.Board;
import com.spring.myproject.entity.Reply;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
//@TestPropertySource(locations = {"classpath:application-test.properties"})
class ReplyRepositoryTest {

  @Autowired
  private ReplyRepository replyRepository;

  @Autowired
  private BoardRepository boardRepository;

  @Test@DisplayName("Reply객체 생성하기")
  public void testInsertReply(){

    /* 1. 댓글 생성 하기 */
    // 특정 게시글 가져오기
    Long bno = 100L;

//    Optional<Board> result = boardRepository.findById(bno);
//    Board board = result.orElseThrow();

    Board board = boardRepository.findById(bno).orElseThrow();
    log.info("=> findById(n):"+board);

    for (int i=100; i<=201; i++) {
      // 특정 게시글에 대한 댓글 생성(특정 게시글과 댓글과 연관관계 설정후 생성)
      Reply reply = Reply.builder()
          // board_bno필드만 생성하여 board의 pk필드 bno값을 설정하고 join상태 설정
          .board(board)   //=> .board_bno(board.getBno())
          .replyText("댓글...")
          .replyer("replyer1")
          .build();

      // db처리
      replyRepository.save(reply);
    }//end for

    /*
    // 2. 댓글 300개 생성하기
    IntStream.rangeClosed(1, 300).forEach(i -> {

      // 게시글 번호 무작위 선정
      long bno = (long) (Math.random()*100)+1;
      Board board = Board.builder().bno(bno).build();

      // 특정 게시글에 대한 댓글 생성(특정 게시글과 댓글과 연관관계 설정후 생성)
      Reply reply = Reply.builder()
          // board_bno필드만 생성하여 board의 pk필드 bno값을 설정하고 join상태 설정
          .board(board)   //=> .board_bno(board.getBno())
          .replyText("댓글..."+i)
          .replyer("replyer")
          .build();

      // db처리
      replyRepository.save(reply);

    });

     */


  }

  @Test
  @DisplayName("특정 게시글 댓글조회")
  @Transactional
  public void testBoardReplies(){
    Long bno = 1L;

    Pageable pageable = PageRequest.of(0, 10, Sort.by("rno").descending());
    Page<Reply> result = replyRepository.listOfBoard(bno, pageable);

    log.info("=> result: "+result);
    result.getContent().forEach(reply -> {
      log.info(reply);
    });
  }

}