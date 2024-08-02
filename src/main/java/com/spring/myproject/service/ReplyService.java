package com.spring.myproject.service;

import com.spring.myproject.dto.PageRequestDTO;
import com.spring.myproject.dto.PageResponseDTO;
import com.spring.myproject.dto.ReplyDTO;

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

}
