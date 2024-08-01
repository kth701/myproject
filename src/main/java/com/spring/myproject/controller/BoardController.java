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
  // 2. 게시글 등록 입력폼 요청
  @GetMapping("/register")
  public String registerGet(){
    // 게시글 등록 입력 폼 요청
    return "board/register";
  }
  // 2.1 게시글 등록(DB) 작업 처리
  @PostMapping("/register")
  public String registerPost(@Valid BoardDTO boardDTO,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes){

    // 클라이언트로 부터 전송받은 boardDTO를 @Valid에서 문제가 발생했을 경우
    if (bindingResult.hasErrors()){
      log.info("=> has errors...");

      // 1회용 정보유지 : redirect방식으로 요청시 정보관리하는 객체
      redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
      return "redirect:/board/register"; // Get방식으로 /board/register재요청
    }
    log.info("=> "+boardDTO);


    // 게시글 등록 서비스 호출(DB에 저장)
    Long bno = boardService.register(boardDTO);

    // db처리한 후 결과값을 객체에 저장
    // 1회용 정보유지 : redirect방식으로 요청시 정보관리하는 객체
    redirectAttributes.addFlashAttribute("result", "registered");
    redirectAttributes.addFlashAttribute("bno", bno);
    return "redirect:/board/list";
  }

  // 3. 게시글 조회 및 수정 화면 => /board/read or /baord/modify 요청 처리
  @GetMapping({"/read", "/modify"})
  public void read(Long bno,
                   PageRequestDTO pageRequestDTO,
                   Model model){

    // 게시글 조회 서비스 요청
    BoardDTO boardDTO = boardService.readOne(bno);
    model.addAttribute("dto", boardDTO);

    /*
     반환값을 void할경우 url요청경로을 기준으로
     return "board/read" 형태로 자동 포워딩됨
     또는
     return "board/modify" 형태로 자동 포워딩됨
     */
  }
  // 4. 게시글 수정
  @PostMapping("/modify")
  public String modify(@Valid BoardDTO boardDTO,
                       BindingResult bindingResult,
                       PageRequestDTO pageRequestDTO,
                       RedirectAttributes redirectAttributes){

    log.info("=> "+boardDTO);

    // 클라이언트로 부터 전송받은 boardDTO를 @Valid에서 문제가 발생했을 경우
    if (bindingResult.hasErrors()){
      log.info("=> has errors...");

      // 수정 페이지에서 넘겨받은 페이징 정보
      String link = pageRequestDTO.getLink();

      // 1회용 정보유지 : redirect방식으로 요청시 정보관리하는 객체
      redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
      redirectAttributes.addFlashAttribute("bno", boardDTO.getBno());

      // Get방식으로 /board/modify+페이징정보 재요청
      return "redirect:/board/modify?"+link;
    }

    // 수정 서비스 요청
    boardService.modify(boardDTO);


    // addFlashAttribute() => 1회용 정보유지 : redirect방식으로 요청시 정보관리하는 객체
    redirectAttributes.addFlashAttribute("result", "modified");
    redirectAttributes.addFlashAttribute("bno", boardDTO.getBno());

    // redirect방식에서 파라미터 추가 기능
    redirectAttributes.addAttribute("bno", boardDTO.getBno());
    // => return "redirect:/board/read?bno=${bno}" 형식으로 인식됨.;
    return "redirect:/board/read";
  }

  // 5. 게시글 삭제
  @PostMapping("/remove")
  public String remove(BoardDTO boardDTO,
                       RedirectAttributes redirectAttributes){

    Long bno = boardDTO.getBno();
    log.info("remove bno: "+bno);

    // 게시글 삭제 서비스 요청
    boardService.remove(bno);

    // addFlashAttribute() => 1회용 정보유지 : redirect방식으로 요청시 정보관리하는 객체
    redirectAttributes.addFlashAttribute("result", "removed");
    redirectAttributes.addFlashAttribute("bno",bno);

    return "redirect:/board/list";
  }

}
