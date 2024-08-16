package com.spring.myproject.repository;

import com.spring.myproject.dto.BoardListReplyCountDTO;
import com.spring.myproject.entity.Board;
import com.spring.myproject.entity.Reply;
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
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;


@SpringBootTest
@Log4j2
//@TestPropertySource(locations = {"classpath:application-test.properties"})
class BoardRepositoryTest {

  @Autowired
  private BoardRepository boardRepository;
  @Autowired
  private ReplyRepository replyRepository;

  @Test
  @DisplayName("insert board data ")
  public void testInsertBoard(){
    // 1. 테스트를 위한 더미 데이터 생성
    /*
    IntStream.rangeClosed(1,100).forEach( i->{
      Board board = Board.builder()
          .title("title..."+i)
          .content("content..."+i)
          .writer("user"+(i%10))
          .build();

      Board result = boardRepository.save(board);
      log.info("BNO: "+result.getBno());

    });
     */

    // 2. 테스트를 위한 더미 데이터 생성(게시글 이미지포함)
    for( int i=1; i<=100; i++){
      Board board = Board.builder()
          .title("title..."+i)
          .content("content..."+i)
          .writer("user"+(i%10))
          .build();

      // 게시물 1개당 3개의 첨부파일 생성
      for(int j=1; j<=3; j++){
        if (i%5==0) continue;

        board.addImage(UUID.randomUUID().toString(), i+"file"+j+".jpg");
        log.info("i="+i+", j="+j);

      }; // inner for

      boardRepository.save(board);//board 저장

    }; // outer for




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


  //-----------------------------------------//
  // Board와 BoardImage 연관 관계 테스트
  //-----------------------------------------//
  @Test
  @DisplayName("Board save시,BoardImage 자동으로 save: 영속성 전이 테스트")
  public void testInsertWidthImage(){

    // Board Entity 생성
    Board board = Board.builder()
        .title("Image Test")
        .content("첨부파일테스트")
        .writer("tester")
        .email("test@test.com")
        .build();

    for (int i=1; i<=3; i++){

      // 부모객체 내에서 하위객체 생성
      // board객체에서 BoardImage 객체를 생성
      board.addImage(UUID.randomUUID().toString(), "file"+i+".jpg");

    };

    // board entity 저장(영속성 컨텍스트에 반영)
    // board entity를 저장할 하면 board속성중에  boardimage객체를 보관하고 있는
    /*
     set객체 정보를 기반으로 boardimage객체가 자동으로 save동작이 발생됨.
     BoardImageRepository.save(boardImage); 묵시적으로 수행됨.
     BoardImageRepository 구현 생략 => 자동 생성됨.
     */

    Board savedBoard = boardRepository.save(board);
    log.info("==> BoardImage:"+savedBoard);

  }

  @Test
  @DisplayName("Board read시,BoardImage read: 영속성 전이 테스트")
  @Transactional
  public void testReadWidthImage(){
    // 반드시 존재하는 bno로 확인
    Optional<Board> result = boardRepository.findById(1L);
    Board board = result.orElseThrow();

    log.info("==> board");
    log.info("-------");
    log.info(board.getImageSet());// board안에 있는 boardimage엔티티 조회(select...)
    // 에러가 발생 됨.
    // board연결하여 출력한 후 select를 다시실행하면 db가 연결이 끝난 상태이므로
    // proxy - 'no session' 에러 메시지가 발생 => @Transactional 해결됨.

  }


  @Test
  @DisplayName("Board,BoardImage(EntityGraph): 영속성 전이 테스트")
  public void testReadWidthImageEntityGraph(){
    // 반드시 존재하는 bno로 확인
    Optional<Board> result = boardRepository.findByIdWidthImages(1L);
    Board board = result.orElseThrow();

    log.info("==> board와 boardImage EntityGraph:");
    log.info("-------");

    board.getImageSet().forEach( boardImage -> {
      log.info(boardImage);
    });

  }



  @Test
  @DisplayName("Board,BoardImage remove: 고아객체 테스트")
  @Transactional@Commit
  public void testBoardAndBoardImageRemove(){
    // 1번 게시물 가져오기
    Optional<Board> result = boardRepository.findByIdWidthImages(1L);
    Board board = result.orElseThrow();

    // 기존 게시물에 첨부파일들 삭제:
    // 1. orphanRemoval = true 설정하지 않을 경우: null 값으로 업데이트 기능 처리됨.
    // 2. orphanRemoval = true 설정 경우: null값이 존재하는 고아객체 제거됨.
    board.clearImage();

    // 새로운 첨부파일 추가
    for (int i=1; i<=3; i++){
      board.addImage(UUID.randomUUID().toString(), "file"+i+".jpg");
    };
    boardRepository.save(board);

  }


  @Test
  @DisplayName("특정 게시글에 대한 댓글 삭제 테스트")
  @Transactional@Commit
  public void testBoardReplyRemoveAll(){
    Long bno = 1L;

    /*
    // 1. 특정 게시글에 대한 댓글 생성
    Board board = boardRepository.findById(bno).orElseThrow();
    for (int i=1; i<=2; i++) {
      Reply reply = Reply.builder()
          .board(board)
          .replyText("댓글...")
          .replyer("replyer1")
          .build();

      replyRepository.save(reply);
    }//end for
     */


    // Reply 존재하는 경우 댓글 삭제 -> BoardImage 삭제(imageSet.clear())-> Board 삭제
    // 2. 댓글 삭제
    replyRepository.deleteByBoard_Bno(bno);
    // 3. 게시글 삭제
    boardRepository.deleteById(bno);
  }

  @Test
  @DisplayName("특정 게시글에 대한 댓글 조회 테스트")
  @Transactional@Commit
  public void testsearchImageReplyCount(){
    // 페이징 초기값 설정
    Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

    // 게시물 1개당 이미지첩부파일 조회
    boardRepository.searchWithAll(null, null, pageable);

  }



} // end Test Class


