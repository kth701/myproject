package com.spring.myproject.service;


import com.spring.myproject.dto.*;
import com.spring.myproject.entity.Board;

import java.util.List;
import java.util.stream.Collectors;

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

  // 7. 게시글의 이미지와 댓글의 숫자 처리
  PageResponseDTO<BoardListAllDTO> listWithAll(PageResponseDTO pageResponseDTO);

  // 8. List<Setring> fileName -> Board에서 Set<boardImage>타입으로 변환
  default Board dtoToEntity(BoardDTO boardDTO){
    Board board = Board.builder()
        .bno(boardDTO.getBno())
        .title(boardDTO.getTitle())
        .content(boardDTO.getContent())
        .email(boardDTO.getEmail())
        .writer(boardDTO.getWriter())
        .build();

    // 첨부파일이 있을 경우
    if (boardDTO.getFileNames() != null){
      boardDTO.getFileNames().forEach(fileName ->{
        String[] arr = fileName.split("_");
        board.addImage(arr[0], arr[1]);
      });
    }

    return board;
  } // end dtoToEntity


  // entity -> dto
  default BoardDTO entityToDto(Board board){
    BoardDTO boardDTO = BoardDTO.builder()
        .bno(board.getBno())
        .title(board.getTitle())
        .email(board.getEmail())
        .content(board.getContent())
        .regDate(board.getRegDate())
        .modDate(board.getModDate())
        .build();

    List<String> fileNames =
        board.getImageSet()
            .stream()
            .sorted()
            .map(boardImage -> boardImage.getUuid()+"_"+boardImage.getFileName())
            .collect(Collectors.toList());

    return boardDTO;
  }
}
