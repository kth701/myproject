package com.spring.myproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Builder
@Data
@AllArgsConstructor@NoArgsConstructor
public class PageRequestDTO {
    @Builder.Default
    private int page = 1;
    @Builder.Default
    private int size = 10;
    private String type;  // 검색 종류:t,c,w,tc,tw,twc
    private String keyword;

    // 키워드에 대한 type구분하여 배열구조로 반환
    public String[] getTypes(){
        if (type == null || type.isEmpty()) return null;

        return type.split("");
    }
    // 페이징 초기값 설정
    public Pageable getPageable(String...props){
        return PageRequest.of(this.page - 1, this.size, Sort.by(props).descending());
    }

    // 검색조건 매개변수 설정과 페이지 조건 매개변수 설정을  처리하는 문자열
    private String link;
    public String getLink(){
        if (link == null) {

            StringBuilder builder = new StringBuilder();
            builder.append("page="+this.page);
            builder.append("&size="+this.size);

            if (type != null && type.length()>0)
                builder.append("&type="+type);

            if (keyword != null){
              try {
                builder.append("&keyword="+ URLEncoder.encode(keyword, "UTF-8"));
              } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
              }
            }

            link = builder.toString();
        }
        // link = page=1&size=10&type=twc&keyword=URLEncoder.encode("홍길동").....
        return link;
    }
}
