package com.spring.myproject.repository;

import com.spring.myproject.dto.BoardListReplyCountDTO;
import com.spring.myproject.entity.Board;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;


@SpringBootTest
@Log4j2
//@TestPropertySource(locations = {"classpath:application-test.properties"})
class BoardRepositoryTest {

  @Autowired
  private BoardRepository boardRepository;

  @Test
  @DisplayName("insert board data ")
  public void testInsertBoard(){
    IntStream.rangeClosed(1,100).forEach( i->{
      Board board = Board.builder()
          .title("title..."+i)
          .content("content..."+i)
          .writer("user"+(i%10))
          .build();

      Board result = boardRepository.save(board);
      log.info("BNO: "+result.getBno());

    });
  }

  @Test
  @DisplayName("select board data ")
  public void testSelectBoard(){
    // H2 DB 테스트할 경우 적용
    //this.testInsertBoard();// data 생성후 조회 작업

    Long bno = 100L;

    // Optional : null을 허용하는 wrapper형식의 클래스
    Optional<Board> result = boardRepository.findById(bno);
    Board board = result.orElseThrow();

    log.info("=> findBy(): 100=> "+board);
  }

  @Test
  @DisplayName("update board data ")
  public void testUpdateBoard(){
    //data 생성후 , 수정할 글번호 읽기와서 수정 작업
    //this.testInsertBoard();

    Long bno = 100L;

    // 1. 수정할 내용 조회
    Optional<Board> result = boardRepository.findById(bno);
    Board board = result.orElseThrow();

    // 2.정할 내용 엔티티에 반영
    board.change("update  "+board.getTitle(), "update "+board.getContent());
    // 3.저장
    Board savedBoard = boardRepository.save(board);
    log.info("=> update: "+savedBoard);

  }

  @Test
  @DisplayName("delete board data ")
  public void testDeleteBoard(){
    //data 생성후 , 수정할 글번호 읽기와서 수정 작업
    //this.testInsertBoard();

    Long bno = 99L;
    boardRepository.deleteById(bno);// select -> delete

    Optional<Board> result = boardRepository.findById(bno);
    Board board = result.orElseThrow();

    // orElse(존재하지 않을 경우 즉 널이면 처리할 내용 기술)

    // orElseThrow(존재하지 않을 경우 즉 널이면 예외처리 기술)
    //Board board = result.orElseThrow(IllegalArgumentException::new);

    // Option타입객체.isPresent() => 값이 존재하면 즉 null아니면 true
    /*
    if (result.isEmpty())// 결과 값이 존재하지 않으면 즉 없으면(null)이면 처리
      log.info("=> 없는 글번호");
    else log.info(result);
    */

  }





  //-----------------------------------------//
  // 페이징 처리
  //-----------------------------------------//
  @Test
  @DisplayName("search and paging 테스트")
  public void testSearch1(){
    Pageable pageable = PageRequest.of(0,5, Sort.by("bno").descending());

    Page<Board> result  = boardRepository.search2(pageable);
    result.getContent().forEach( board -> log.info("=> list:"+board));

    log.info("----");

    List<Board> contents =  boardRepository.search2(pageable).getContent();
    contents.forEach( board -> log.info("=> list2:"+board));
  }

  @Test
  @DisplayName("search keyword and paging 테스트1")
  public void testSearchAll(){
    //given
    // paging 정보
    Pageable pageable = PageRequest.of(0,5, Sort.by("bno").descending());
    // 키워드 , 타입
    String[] types = {"t","c","w"};
    String keyword = "1";

    //when
    Page<Board> result  = boardRepository.searchAll(types, keyword, pageable);
    result.getContent().forEach( board -> log.info("=> list:"+board));

    log.info("----");

    List<Board> contents =  boardRepository.searchAll(types, keyword, pageable).getContent();
    contents.forEach( board -> log.info("=> list2:"+board));

    log.info("=> paging info");
    log.info("=> 총페이지:"+(result.getTotalPages())+" page");
    log.info("=> 페이지사이즈:"+result.getSize());
    log.info("=> 현재페이지:"+result.getNumber());
    log.info("=> 다음페이지:"+result.hasNext());
    log.info("=> 이전페이지:"+result.hasPrevious());

    //then
    //Assert.assertThat(result.hasPrevious()).isEqualTo(false);


  }


  //-----------------------------------------//
  // 조건검색 결과에 대한 댓글 개수
  //-----------------------------------------//
  @Test
  @DisplayName("SearchReplyCount 테스트")
  public void testSearchReplyCount(){
    //given
    Pageable pageable = PageRequest.of(0,5, Sort.by("bno").ascending());
    //Pageable pageable = PageRequest.of(0,5, Sort.by("bno").descending());
    String[] types = {"t","c","w"}; // 키워드 , 타입
    String keyword = "1";

    Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);

    log.info("=> total page:"+result.getTotalPages());
    log.info("=> page size:"+result.getSize());
    log.info("=> pageNumber:"+result.getNumber());
    log.info("=> prev next:"+result.hasPrevious()+","+result.hasNext());
    log.info("====");
    result.getContent().forEach(board-> log.info(board));
  }









} // end Test Class


