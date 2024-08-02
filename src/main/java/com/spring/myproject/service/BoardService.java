package com.spring.myproject.service;


import com.spring.myproject.dto.BoardDTO;
import com.spring.myproject.dto.BoardListReplyCountDTO;
import com.spring.myproject.dto.PageRequestDTO;
import com.spring.myproject.dto.PageResponseDTO;
import com.spring.myproject.entity.Board;

public interface BoardService {

  // 1. 게시글 등록 서비스 인터페이스
  long register(BoardDTO boardDTO);
  // 2. 게시글 조회
  BoardDTO readOne(Long bno);
  // 3. 게시글 수정
  Board modify(BoardDTO boardDTO);
  // 4. 게시글 삭제
  void remove(Long bno);
  // 5. 게시글 목록: 페이징 처리를 한 게시글 목록
  PageResponseDTO<BoardDTO> list (PageRequestDTO pageRequestDTO);


  // 6. 댓글의 숫자 처리하는 인터페이스 : 조회 결과를 List구조에 저장 및 페이징 처리
  PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);

}
