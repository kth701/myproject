package com.spring.myproject.service;


import com.spring.myproject.dto.BoardDTO;
import com.spring.myproject.dto.PageRequestDTO;
import com.spring.myproject.dto.PageResponseDTO;
import com.spring.myproject.entity.Board;

public interface BoardService {

  // 게시글 등록 서비스 인터페이스
  long register(BoardDTO boardDTO);
  // 게시글 조회
  BoardDTO readOne(Long bno);
  // 게시글 수정
  Board modify(BoardDTO boardDTO);
  // 게시글 삭제
  void remove(Long bno);
  // 게시글 목록: 페이징 처리를 한 게시글 목록
  PageResponseDTO<BoardDTO> list (PageRequestDTO pageRequestDTO);

}
