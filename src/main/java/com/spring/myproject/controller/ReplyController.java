package com.spring.myproject.controller;


import com.spring.myproject.dto.PageRequestDTO;
import com.spring.myproject.dto.PageResponseDTO;
import com.spring.myproject.dto.ReplyDTO;
import com.spring.myproject.service.ReplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.naming.Binding;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// REST방식 : 주로 XML, JSON형태의 문자열을 전송하고 이를 컨트롤러에서 처리하는 방식
@Tag(name = "예제 API", description = "Swagger 테스트용 API")
@RestController
@RequestMapping("/replies")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {

  private  final ReplyService replyService; // 댓글 서비스 객체

  /*  RestController Test
  @PostMapping(value = "/test3", consumes = MediaType.APPLICATION_JSON_VALUE)
  public Map<String,Long> register2(
      @Valid @RequestBody ReplyDTO replyDTO,
      BindingResult bindingResult)throws BindException{

    log.info(replyDTO);

    if(bindingResult.hasErrors()){
      throw new BindException(bindingResult);
    }


    Map<String, Long> resultMap = Map.of("rno", 222L);

    return resultMap;
  }
 */

  /* 더미 데이터로 테스트
  @PostMapping(value="/", consumes = MediaType.APPLICATION_JSON_VALUE)// 전송받은 data 종류 명시
  public ResponseEntity<Map<String, Long>> register(@RequestBody ReplyDTO replyDTO){
      log.info("=> replyDTO: "+replyDTO);

      Map<String, Long> resultMap = Map.of("rno", 222L);

    return ResponseEntity.ok(resultMap);
    // or => return new ResponseEntity(resultMap, HttpStatus.OK);
  }
  */



  // ---------------------------------------------------------------------------------- //
  // RestController예외처리 consumes = MediaType.APPLICATION_JSON_VALUE
  //@Parameter(name = "replyDTO", description = "@Valid어노테이션으로 replyDTO 유효성 검사")
  // ---------------------------------------------------------------------------------- //

  // 1. 댓글 등록 : url => /replies/
  @Operation(summary="Replies POST", description="POST방식으로 댓글 등록")
  @PostMapping(value="/",consumes = MediaType.APPLICATION_JSON_VALUE )// 전송받은 data 종류 명시
  public Map<String, String> register(
                    @Valid @RequestBody ReplyDTO replyDTO,
                    BindingResult bindingResult // 객체값 검증
                    ) throws BindException {

    log.info("=> replyDTO: "+replyDTO);
    //log.info("=> bindingResult: "+bindingResult.toString());

    // 에러가 존재하면 BindException예외 발생시킴
    // => RestController예외처리를 해주는  @RestControllerAdvice어노테이션
    if (bindingResult.hasErrors()){
      throw new BindException(bindingResult);
    }


    Long rno = replyService.register(replyDTO);

    Map<String, String> resultMap = new HashMap<>();
    resultMap.put("rno", rno+"번 댓글 등록되었습니다.");

    return resultMap;
  }



  // 2. 특정 게시물의 댓글 목록
  @Operation(summary="Replies of Board", description="Post방식으로 특정 게시물의 댓글 목록")
  @GetMapping(value="/list/{bno}")
//  public   Map<String, List<ReplyDTO>> getList(
  public  PageResponseDTO<ReplyDTO> getList(
                                        @PathVariable("bno") Long bno,
                                        PageRequestDTO pageRequestDTO){

    PageResponseDTO<ReplyDTO> responseDTO = replyService.getListOBoard(bno, pageRequestDTO);

    //responseDTO.getDtoList().stream().forEach( reply -> log.info("=> "+reply));
    //Map<String, List<ReplyDTO>> resultMap = Map.of("list", responseDTO.getDtoList());
    //return resultMap;

    // 서버쪽에 클라이언체 보내는 데이터 : 페이징 객체, 댓글 list,...
    return responseDTO;
  }



  // 3. 댓글 수정
  @Operation(summary="Modify Reply", description="PUT방식으로 특정 댓글 수정")
  @PutMapping(value="/{rno}",consumes = MediaType.APPLICATION_JSON_VALUE )// 전송받은 data 종류 명시
  public Map<String, Long> modify(@PathVariable("rno") Long rno,
                                  @RequestBody ReplyDTO replyDTO){

    replyDTO.setRno(rno);// 매개변수로 넘어온 댓글번호을 replyDTO에 설정
    replyService.modify(replyDTO);

    // 클라이언트에게 보낼 data 정보 및 메시지
    Map<String, Long> resultMap = new HashMap<>();
    resultMap.put("rno",rno);

    return resultMap;
  }

  // 4. 댓글 삭제
  @Operation(summary="Delete Reply", description="DELETE방식으로 특정 댓글 삭제")
  @DeleteMapping(value="/{rno}" )// 전송받은 data 종류 명시
  public Map<String, Long> remove(@PathVariable("rno") Long rno){


    replyService.remove(rno);

    // 클라이언트에게 보낼 data 정보 및 메시지
    Map<String, Long> resultMap = new HashMap<>();
    resultMap.put("rno",rno);

    return resultMap;
  }

  // 5. 댓글 조회
  @Operation(summary="Read Reply", description="GET방식으로 특정 댓글 조회")
  @GetMapping(value="/{rno}" )// 전송받은 data 종류 명시
  public ReplyDTO getReplyDTO(@PathVariable("rno") Long rno){

    ReplyDTO replyDTO = replyService.read(rno);
    return replyDTO;
  }


}


/*
Springdoc 공식 가이드에서 설명하는 어노테이션의 변화는 다음과 같다.

@Api → @Tag
@ApiIgnore
  → @Parameter(hidden = true) or @Operation(hidden = true) or @Hidden
@ApiImplicitParam
  → @Parameter
@ApiImplicitParams
  → @Parameters
@ApiModel
  → @Schema
@ApiModelProperty(hidden = true)
  → @Schema(accessMode = READ_ONLY)
@ApiModelProperty
  → @Schema
@ApiOperation(value = "foo", notes = "bar")
  → @Operation(summary = "foo", description = "bar")
@ApiParam
  → @Parameter
@ApiResponse(code = 404, message = "foo")
  → @ApiResponse(responseCode = "404", description = "foo")

 */