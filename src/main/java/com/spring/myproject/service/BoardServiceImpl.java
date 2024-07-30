package com.spring.myproject.service;

import com.spring.myproject.dto.BoardDTO;
import com.spring.myproject.dto.PageRequestDTO;
import com.spring.myproject.dto.PageResponseDTO;
import com.spring.myproject.entity.Board;
import com.spring.myproject.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

  private final ModelMapper modelMapper;
  private final BoardRepository boardRepository;

  @Override
  public long register(BoardDTO boardDTO) {
    Board board = modelMapper.map(boardDTO, Board.class);

    Long bno = boardRepository.save(board).getBno();

    return bno;
  }

  @Override
  public BoardDTO readOne(Long bno) {
    Optional<Board> result = boardRepository.findById(bno);
    // optional -> entity
    Board board = result.orElseThrow();
    // entity -> dto 맵핑
    BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);

    return boardDTO;
  }

  @Override
  public Board modify(BoardDTO boardDTO) {
    // 수정할 글번호 읽어오기
    Optional<Board> result = boardRepository.findById(boardDTO.getBno());
    Board board = result.orElseThrow();
    // entity값을 dto값으로 변경
    board.change(boardDTO.getTitle(), boardDTO.getContent());
    // 저장하기
    Board modBoard = boardRepository.save(board);

    return modBoard;// 저장된 entity
  }

  @Override
  public void remove(Long bno) {
    boardRepository.deleteById(bno);
  }

  @Override
  public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
    // 검색 조건에 대한 처리
    String[] types = pageRequestDTO.getTypes();
    String keyword = pageRequestDTO.getKeyword();
    Pageable pageable = pageRequestDTO.getPageable("bno");

    // 조건 검색 및 페이징한 결과값 가져오기
    Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

    // Page객체 있는 내용을 List구조 가져오기
    List<BoardDTO> dtoList =
        result.getContent()
            .stream()
            // collection구조에 있는 entity를 하나씩 dto으로 변화하여 List구조에 저장
            .map( board -> modelMapper.map(board,BoardDTO.class ) )
            .collect(Collectors.toList());

    return PageResponseDTO.<BoardDTO>withAll()
        .pageRequestDTO(pageRequestDTO)
        .dtoList(dtoList)
        .total((int)result.getTotalElements())
        .build();
  }
}
