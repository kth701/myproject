package com.spring.myproject.service;

import com.spring.myproject.dto.PageRequestDTO;
import com.spring.myproject.dto.PageResponseDTO;
import com.spring.myproject.dto.ReplyDTO;
import com.spring.myproject.entity.Reply;
import com.spring.myproject.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Log4j2
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

  private final ReplyRepository replyRepository;
  private final ModelMapper modelMapper;

  // 1. 댓글 등록 구현
  @Override
  public Long register(ReplyDTO replyDTO) {

    // 1.1  dto -> entity
    Reply reply = modelMapper.map(replyDTO, Reply.class);
    Long rno = replyRepository.save(reply).getRno();

    return rno;
  }

  @Override
  public ReplyDTO read(Long rno) {
    Optional<Reply> replyOptional = replyRepository.findById(rno);
    Reply reply = replyOptional.orElseThrow();

    return modelMapper.map(reply, ReplyDTO.class);
  }

  @Override
  public void modify(ReplyDTO replyDTO) {
    Optional<Reply> replyOptional = replyRepository.findById(replyDTO.getRno());
    Reply reply = replyOptional.orElseThrow();

    // 댓글 내용 수정
    reply.changeText(replyDTO.getReplyText());
    // db 반영
    replyRepository.save(reply);
  }

  @Override
  public void remove(Long rno) {
    replyRepository.deleteById(rno);
  }

  @Override
  public PageResponseDTO<ReplyDTO> getListOBoard(Long bno,
                                                 PageRequestDTO pageRequestDTO) {
    //  PageRequest.of(0, 10, 정렬옵션)
    Pageable pageable = PageRequest.of(
              pageRequestDTO.getPage() <= 0 ? 0: pageRequestDTO.getPage() -1,
              pageRequestDTO.getSize(),
              Sort.by("rno").ascending());

    // 조회된 결과를 Optional타입으로 반환됨.
    Page<Reply> result = replyRepository.listOfBoard(bno, pageable);

    // Optional객체 내에 있는 내용을 .getContent()의해 추출하여
    // entity->dto전환하여 List구조에 저장
    List<ReplyDTO> dtoList =
        result.getContent()
            .stream()
            .map(reply -> modelMapper.map(reply, ReplyDTO.class))
            .collect(Collectors.toList());

    return PageResponseDTO.<ReplyDTO>withAll()
        .pageRequestDTO(pageRequestDTO)
        .dtoList(dtoList)
        .total((int)result.getTotalPages())
        .build();

  }
}
