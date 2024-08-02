package com.spring.myproject.repository.search;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.spring.myproject.dto.BoardListReplyCountDTO;
import com.spring.myproject.entity.Board;

import com.spring.myproject.entity.QBoard;
import com.spring.myproject.entity.QReply;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Log4j2
public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

  // 인자가 있을 경우
  /*
  public BoardSearchImpl(Class<?> domainClass) {
    super(domainClass);
  }*/
  public BoardSearchImpl() {
    super(Board.class);
  }

  // search1()메서드 자체가 수행안됨 => search2()재작성 후 인식
  @Override
  public Page<Board> search1(Pageable pageable) {
    log.info("=> Page...");
    // Entity -> QDomain
//    QBoard board = QBoard.board;

//    // JPQLQuery<Board> => jsp: PreParseStatement 역할
//    // 1. where 추가
//    JPQLQuery<Board> query = from(board);                      // select .. from board
//    query.where(board.title.contains("1"));   // where title like ...

    // 2. BooleanBuilder: 조건문을 작성하는 클래스
    //BooleanBuilder booleanBuilder = new BooleanBuilder();

    // where title like '%11%' or content like '%11%'
    //booleanBuilder.or(board.title.contains("1"));
    //booleanBuilder.or(board.content.contains("1"));

//    JPQLQuery<Board> query = from(board);
//    query.where(booleanBuilder);
//    query.where(board.bno.gt(0L));//and 연결

    // 3. paging 추가: QuerydslRepositorySupport에서 상속받은
    // getQuerydsl()와 applyPagination()적용
    //this.getQuerydsl().applyPagination(pageable,query);
    //List<Board> list = getQuerydsl().applyPagination(pageable,query).fetch();  // 3)

    // 1.쿼리문 수행하여 결과 값을 List구조 반환
//    List<Board> list = query.fetch();
//    long count = query.fetchCount();
//    log.info("=> "+count);


    // 페이지 처리의 최종 결과: Page<T>타입
    // JPA에서 제공하는 PageImpl클래스는 3개의 파리미터로 Page<T>를 생성
//    return new PageImpl<>(list, pageable, count);
    return null;
  }


  @Override
  public Page<Board> search2(Pageable pageable) {
    log.info("=> 페이징2 ");

    QBoard board = QBoard.board;  // Entity -> QDomain

    // JPQLQuery<Board> => jsp: PreParseStatement 역할
    // 1. where 추가
    JPQLQuery<Board> query = from(board);                      // select .. from board
    query.where(board.title.contains("1"));

    // 2. paging 추가
    this.getQuerydsl().applyPagination(pageable,query);
    // 3. 쿼리문 수행하여 결과 값을 List구조 반환
    List<Board> list = query.fetch();
    long count = query.fetchCount();
    log.info("=> "+count);

    //return new PageImpl<>(list, pageable, count);

  // ------------------------------------------------------------- //

    // 2-1. BooleanBuilder: 조건문을 작성하는 클래스
    BooleanBuilder booleanBuilder = new BooleanBuilder();

    // where title like '%11%' or content like '%11%'
    booleanBuilder.or(board.title.contains("1"));
    booleanBuilder.or(board.content.contains("1"));

    // query
    JPQLQuery<Board>  query2 = from(board);
                      query2.where(booleanBuilder);
                      query2.where(board.bno.gt(0L));//and 연결

    // paging
    this.getQuerydsl().applyPagination(pageable,query);
    // query
    List<Board> list2 = query.fetch();
    long count2 = query2.fetchCount();
    log.info("=> "+count2);


    return new PageImpl<>(list2, pageable, count);

  }

  @Override
  public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {

    QBoard board = QBoard.board;  // Entity -> QDomain

    // 1. query 작성
    JPQLQuery<Board>  query = from(board);

    if ( (types != null && types.length > 0) && keyword != null){// 검색 키워드가 있으면
      //  BooleanBuilder: 조건문을 작성하는 클래스
      BooleanBuilder booleanBuilder = new BooleanBuilder();

      for (String type : types){
          switch (type){
            case "t":
              booleanBuilder.or(board.title.contains(keyword));break;
            case "c":
              booleanBuilder.or(board.content.contains(keyword));break;
            case "w":
              booleanBuilder.or(board.writer.contains(keyword));break;
          }
      } // end for

      query.where(booleanBuilder);
    }// end if


    // 2. paging 추가
    this.getQuerydsl().applyPagination(pageable,query);
    // 3. query 실행
    List<Board> list = query.fetch();
    long count = query.fetchCount();
    //또는 long count = query.fetch().size();


    return new PageImpl<>(list, pageable, count);
  }

  // 특정 게시글에 대한 댓글 개수 계산하는 인터페이스 구현
  @Override
  public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {

    QBoard board = QBoard.board;
    QReply reply = QReply.reply;

    // 1. query 작성
    JPQLQuery<Board>  query = from(board);
    query.leftJoin(reply).on(reply.board.eq(board)); // reply.board_bno == board.bno

    query.groupBy(board); // group by board.bno

    // 조건문 추가 : where 문 작성
    if ( (types != null && types.length > 0) && keyword != null){// 검색 키워드가 있으면
      //  BooleanBuilder: 조건문을 작성하는 클래스
      BooleanBuilder booleanBuilder = new BooleanBuilder();

      for (String type : types){
        switch (type){
          case "t":
            booleanBuilder.or(board.title.contains(keyword));break;
          case "c":
            booleanBuilder.or(board.content.contains(keyword));break;
          case "w":
            booleanBuilder.or(board.writer.contains(keyword));break;
        }
      } // end for

      query.where(booleanBuilder);
    }// end if

    // bno > 0 : board테이블에 자료가 존재 하면이라는 조건
    query.where(board.bno.gt(0L));

    // Projection.bean(): JPA에서 프로젝션: JPQL의 결과를 바로 DTO로 처리하는 기능
    JPQLQuery<BoardListReplyCountDTO> dtoQuery =
        query.select(Projections.bean(
            BoardListReplyCountDTO.class,// DTO 타입 선언

            board.bno,
            board.title,
            board.writer,
            board.regDate,

            reply.count().as("replyCount")

        ));



    return null;
  }

}
