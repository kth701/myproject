package com.spring.myproject.service;

import com.spring.myproject.dto.*;
import com.spring.myproject.entity.Board;
import com.spring.myproject.entity.BoardImage;
import com.spring.myproject.repository.ReplyRepository;
import lombok.extern.log4j.Log4j2;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
//@TestPropertySource(locations = {"classpath:application-test.properties"})
class BoardServiceTest {

  @Autowired
  private BoardService boardService;
  @Autowired
  private ReplyRepository replyRepository;

  @Test@DisplayName("게시글 등록 서비스 테스트")
  public void testBoardReggisterTest(){
    log.info("=> "+boardService.getClass().getName());

    // 게시글 더미 데이터 생성
    BoardDTO boardDTO = BoardDTO.builder()
        .title("Sample Title...0000")
        .content("Sample Content...0000")
        .writer("user00")
        .email("test@gmail.com")
        .build();

    // 첨부파일 추가
    boardDTO.setFileNames(
        Arrays.asList(
            UUID.randomUUID()+"_aaa0.jpg",
            UUID.randomUUID()+"_bbb0.jpg",
            UUID.randomUUID()+"_ccc0.jpg"
        )
    );

    log.info("=> 게시글, 게시글이미지 첨부파일 등록하기--");
    log.info("=> board: "+boardDTO);
    boardDTO.getFileNames().forEach( filename ->{
      log.info("=> "+filename);
      String[] arr = filename.split("_");
      log.info("=> arr[0]:"+arr[0]+",arr[1]:"+arr[1]);
    });


    Long bno = boardService.register(boardDTO);
    log.info("=> saved board bno: "+bno);

  }

  @Test@DisplayName("게시글 조회 서비스 테스트")
  public void testBoardBnoAndBoardImageRead(){
    Long bno = 1L;
    BoardDTO boardDTO = boardService.readOne(bno);

    log.info("=> "+boardDTO);
    // 이미지 첨부파일 확인
    for(String filename: boardDTO.getFileNames()){
      log.info("=> "+filename);
    }
  }

  @Test@DisplayName("게시글 수정 서비스 테스트")
  public void testBoardModifyTest(){
    // 게시글 더미 데이터 생성
    BoardDTO boardDTO = BoardDTO.builder()
        .bno(1L)
        .title("Sample Title...11")
        .content("Sample Content...11")
        .build();

    //----------------------------------------- //
    // 첨부파일 있을 경우
    boardDTO.setFileNames(
        Arrays.asList(
            UUID.randomUUID()+"_zz1.jpg",
            UUID.randomUUID()+"_zz2.jpg",
            UUID.randomUUID()+"_zz3.jpg"
            ));
    //----------------------------------------- //

    Board modBoard = boardService.modify(boardDTO);

    // Assertions.assertThat(a).isEqualTo(b): 대상 a가  기대값 b와 같은지 확인
    Assertions.assertThat(modBoard.getTitle()).isEqualTo(boardDTO.getTitle());
    Assertions.assertThat(modBoard.getContent()).isEqualTo(boardDTO.getContent());
    // test 일치하지 않을 경우
    //Assertions.assertThat("100").isEqualTo(boardDTO.getContent());

  }

  @Test@DisplayName("게시글 삭제 서비스 테스트")
  @Transactional@Commit
  public void testBoardDeleteTest(){
    // 댓글이 없는 게시글 삭제시
    long bno = 102L;
    boardService.remove(bno);

  }

  // 페이징 처리 List
  @Test@DisplayName("게시글 페이징 목록 서비스 테스트")
  public void testBoardListTest(){
    // 더미 페이징 설정
    PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
        .type("tcw")
        .keyword("1")
        .page(1)
        .size(10)
        .build();

    // 조건 검색 및 페이징 서비스
    PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

    log.info("\n=> "+responseDTO);
  }

  @Test@DisplayName("게시글,게시글이미지, 조건검색 페이징 조회 테스트")
  public void testBoardListAllDTOTest(){
    // 더미 페이징 설정
    PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
        .type("tcw")
        .keyword("1")
        .page(1)
        .size(10)
        .build();

    // 조건 검색 및 페이징 서비스
    PageResponseDTO<BoardListAllDTO>
        responseDTO = boardService.listWithAll(pageRequestDTO);

    List<BoardListAllDTO> dtoList = responseDTO.getDtoList();
    dtoList.forEach(boardListAllDTO -> {
      log.info("=> "+boardListAllDTO.getBno()+":"+boardListAllDTO.getTitle());

      if (boardListAllDTO.getBoardImages() != null){
        for (BoardImageDTO boardImageDTO: boardListAllDTO.getBoardImages()){
          log.info("=> "+boardImageDTO);
        }
      }
      log.info("-----------------------");
    });

  }



}

