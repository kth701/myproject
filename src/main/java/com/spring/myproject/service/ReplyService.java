package com.spring.myproject.service;

import com.spring.myproject.dto.PageRequestDTO;
import com.spring.myproject.dto.PageResponseDTO;
import com.spring.myproject.dto.ReplyDTO;
import com.spring.myproject.entity.Board;
import com.spring.myproject.entity.Reply;

public interface ReplyService {

  // 1. 댓글 등록
  Long register(ReplyDTO replyDTO);
  // 2. 댓글 조회
  ReplyDTO read(Long rno);
  // 3. 댓글 수정
  void modify(ReplyDTO replyDTO);
  // 4. 댓글 삭제
  void remove(Long rno);

  // 5. 댓글 목록(페이징 기능이 있는 List)
  PageResponseDTO<ReplyDTO> getListOBoard(Long bno, PageRequestDTO pageRequestDTO);


  // ReplyDTO -> Entity : Board entity 객체 처리
  default Reply dtoToEntity(ReplyDTO replyDTO){
    // DTO에 있는 게시글 bno -> board객체를 생성
    Board board = Board.builder().bno(replyDTO.getBno()).build();

    Reply reply = Reply.builder()
        .rno(replyDTO.getRno())
        .replyText(replyDTO.getReplyText())
        .replyer(replyDTO.getReplyer())
        .board(board)
        .build();

    return reply;
  }

  // Entity -> ReplyDTO: Board 객체가 필요하지 않으므로 게시물 번호만 처리
  default ReplyDTO entityToDTO(Reply reply){
    ReplyDTO dto = ReplyDTO.builder()
        .rno(reply.getRno())

        .replyText(reply.getReplyText())
        .replyer(reply.getReplyer())
        .bno(reply.getBoard().getBno())

        .regDate(reply.getRegDate())
        .modDate(reply.getModDate())
        .build();
    return dto;
  }
}
