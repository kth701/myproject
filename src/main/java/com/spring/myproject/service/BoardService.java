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


  // 7. DTO -> entity 변환 : 등록 기능
  // List<Setring> fileName -> Board에서 Set<boardImage>타입으로 변환
  default Board dtoToEntity(BoardDTO boardDTO){
    // getter dto -> setter entity -> db table 저장
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
        String[] arr = fileName.split("_");   // 첨부파일 이름 구성 : "UUID값"+"_"+"파일이름.확장자"
        board.addImage(arr[0], arr[1]);
      });
    }

    return board;
  } // end dtoToEntity


  //  8. entity -> dto : 조회 기능
  default BoardDTO entityToDto(Board board){
    // getter entity -> setter dto
    BoardDTO boardDTO = BoardDTO.builder()
        .bno(board.getBno())
        .title(board.getTitle())
        .email(board.getEmail())
        .writer(board.getWriter())
        .content(board.getContent())
        .regDate(board.getRegDate())
        .modDate(board.getModDate())
        .build();

    // boardImage entity getter -> List 저장
    List<String> fileNames =
        board.getImageSet()
            .stream()
            .sorted()
            .map(boardImage ->
                boardImage.getUuid()+"_"+boardImage.getFileName()
            )
            .collect(Collectors.toList());

    // 첨부파일 fileNames객체를  boardDTO fileNames에 setter()
    boardDTO.setFileNames(fileNames);

    return boardDTO;
  }

  // 9. 게시글의 이미지와 댓글의 숫자 처리
  PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);

}
