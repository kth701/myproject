package com.spring.myproject.controller;


import com.spring.myproject.dto.BoardDTO;
import com.spring.myproject.dto.PageRequestDTO;
import com.spring.myproject.dto.PageResponseDTO;
import com.spring.myproject.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller@Log4j2
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
  private final BoardService boardService;


  @GetMapping("/root")
  public String root(){
    return "index";
  }

  // 1. 게시글 목록
  @GetMapping("/list")
  public String list(PageRequestDTO pageRequestDTO, Model model){
    // PageRequestDTO 객체 생성만 했을 경우 기본값 설정

    PageResponseDTO responseDTO = boardService.list(pageRequestDTO);
    log.info("=> "+responseDTO);

    model.addAttribute("responseDTO", responseDTO);
    return "board/list";
  }
  // 2. 게시글 등록
  @PostMapping("/register")
  public String registerPost(@Valid BoardDTO boardDTO,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes){

    if (bindingResult.hasErrors()){
      log.info("=> has errors...");

      // 1회용 정보유지 : redirect방식으로 요청시 정보관리하는 객체
      redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
      return "redirect:/board/register";
    }
    log.info("=> "+boardDTO);
    // 게시글 등록 서비스 호출
    Long bno = boardService.register(boardDTO);

    redirectAttributes.addFlashAttribute("result", bno);

    return "redirect:/board/list";
  }

  // 3. 게시글 조회

  // 4. 게시글 수정

  // 4. 게시글 삭제

}
