package com.spring.myproject.repository.search;

import com.spring.myproject.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


// 단순 페이지 처리 기능 설게
public interface BoardSearch {

  // search1()메서드 자체가 수행안됨 => search2()재작성 후 인식
  Page<Board> search1(Pageable pageable);
  Page<Board> search2(Pageable pageable);

  // 검색어가 포함된 페이징, Pageable인자는 마지막에 위치할 것
  Page<Board> searchAll(String[] types, String keyword, Pageable pageable);
}
