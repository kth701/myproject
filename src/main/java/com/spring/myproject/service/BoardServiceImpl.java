package com.spring.myproject.service;

import com.spring.myproject.dto.*;
import com.spring.myproject.entity.Board;
import com.spring.myproject.repository.BoardRepository;
import com.spring.myproject.repository.ReplyRepository;
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
  private final ReplyRepository replyRepository;

  private final ModelMapper modelMapper;
  private final BoardRepository boardRepository;

  @Override
  public long register(BoardDTO boardDTO) {
    // 1. DTO -> Entity : (entity와 dto 동일 구조일 경우 ) 첨부파일 없는 경우
    //Board board = modelMapper.map(boardDTO, Board.class);

    // 2. DTO -> Entity  첨부파일 추가한 경우
    Board board = dtoToEntity(boardDTO);

    // 3. board entity 저장
    Long bno = boardRepository.save(board).getBno();
    //Board savedBoard = boardRepository.save(board);

    return bno;
  }

  @Override
  public BoardDTO readOne(Long bno) {
    // 1. fetch = FetchType.LAZY 상태일 경우 boardImage 즉시로딩안됨
    // Optional<Board> result = boardRepository.findById(bno);

    // 2. fetch = FetchType.LAZY 상태인 경우에도 즉시로딩 (@EntityGraph)
    Optional<Board> result = boardRepository.findByIdWidthImages(bno);

    // optional -> entity
    Board board = result.orElseThrow();

    // entity -> dto 맵핑 (entity와 dto 동일 구조일 경우 )
    // BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);
    BoardDTO boardDTO = entityToDto(board);

    return boardDTO;
  }

  @Override
  public Board modify(BoardDTO boardDTO) {
    // 수정할 글번호 읽어오기
    Optional<Board> result = boardRepository.findById(boardDTO.getBno());
    Board board = result.orElseThrow();
    // entity값을 dto값으로 변경
    board.change(boardDTO.getTitle(), boardDTO.getContent());

    // --------------------------------------------------------//
    // 첨부파일 있을 경우 처리 : 기존에 첨부파일 삭제후 추가하는 형식
    board.clearImage();
    // 수정된 첨부파일이 있을 경우
    if (boardDTO.getFileNames() != null) {
      for (String fileName : boardDTO.getFileNames()){
        String[] arr = fileName.split("_");
        board.addImage(arr[0],arr[1]);
      }
    }
    // --------------------------------------------------------//

    // 저장하기
    Board modBoard = boardRepository.save(board);

    return modBoard;// 저장된 entity
  }

  @Override
  public void remove(Long bno) {

    // --------------------------------------------------------------  //
    // 댓글이 있는 경우 댓글 먼저삭제
    long count = replyRepository.replyCount(bno);
    //log.info("=> bno reply count: "+count);

    if (count>0) //  댓글이 있는 경우
        replyRepository.deleteByBoard_Bno(bno);
    // --------------------------------------------------------------  //

    // 게시물 삭제 => 영속성의 전이 => (부모엔티티 삭제시, 자식엔티티 삭제됨)
    // cascade = {CascadeType.ALL}
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

    // 매개변수로 전달받은 객체(pageRequestDTO)를 가지고 PageResponseDTO.Builder()를 통해
    // PageRequestDTO객체 생성되어 필요시 스프링이 필요시점에 주입 시켜줌(list에서 pageRequestDTO객체 사용가능함 )
    return PageResponseDTO.<BoardDTO>withAll()
        .pageRequestDTO(pageRequestDTO)
        .dtoList(dtoList)
        .total((int)result.getTotalElements())
        .build();
  }



  //댓글의 숫자 처리하는 인터페이스 구현 : 조회 결과를 List구조에 저장 및 페이징 처리
  @Override
  public PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO) {

    // 검색 조건에 대한 처리
    String[] types = pageRequestDTO.getTypes();
    String keyword = pageRequestDTO.getKeyword();
    Pageable pageable = pageRequestDTO.getPageable("bno");

    // 조건 검색 및 페이징한 결과값 가져오기
    Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);

    // 매개변수로 전달받은 객체(pageRequestDTO)를 가지고 PageResponseDTO.Builder()를 통해
    // PageRequestDTO객체 생성되어 필요시 스프링이 필요시점에 주입 시켜줌(list에서 pageRequestDTO객체 사용가능함 )
    return PageResponseDTO.<BoardListReplyCountDTO>withAll()
        .pageRequestDTO(pageRequestDTO)
        .dtoList(result.getContent()) //Projection.bean():  JPQL의 결과를 바로 DTO로 처리된 결과를 인자로 전달
        .total((int)result.getTotalElements())
        .build();

  }

  // 게시글의 이미지와 댓글의 숫자 처리기능 구현
  @Override
  public PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO) {

    String[] types = pageRequestDTO.getTypes();     // 검색 타입(글제목, 글내용, 작성자)
    String keyword = pageRequestDTO.getKeyword();   // 검색 키워드
    Pageable pageable = pageRequestDTO.getPageable("bno");

    // BoardSearch클래스로 부터 상속받은 boardRepository는 searchWithAll()사용 가능
    Page<BoardListAllDTO> result = boardRepository.searchWithAll(types, keyword, pageable);

    return PageResponseDTO.<BoardListAllDTO>withAll()
        .pageRequestDTO(pageRequestDTO)
        .dtoList(result.getContent())
        .total((int)result.getTotalElements())
        .build();







//    String[] types = pageRequestDTO.getTypes();
//    String keyword = pageRequestDTO.getKeyword();
//    Pageable pageable = pageRequestDTO.getPageable("bno");
//
//    Page<BoardListAllDTO> result = boardRepository.searchWithAll(types, keyword, pageable);
//
//    return PageResponseDTO.<BoardListAllDTO>withAll()
//        .pageRequestDTO(pageRequestDTO)
//        .dtoList(result.getContent())
//        .total((int)result.getTotalElements())
//        .build();

  }
}
