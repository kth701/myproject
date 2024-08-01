package com.spring.myproject.controller;


import com.spring.myproject.dto.ReplyDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.naming.Binding;
import java.util.Map;

// REST방식 : 주로 XML, JSON형태의 문자열을 전송하고 이를 컨트롤러에서 처리하는 방식
@Tag(name = "예제 API", description = "Swagger 테스트용 API")
@RestController
@RequestMapping("/replies")
@Log4j2
public class ReplyController {

  /*
  @PostMapping(value="/", consumes = MediaType.APPLICATION_JSON_VALUE)// 전송받은 data 종류 명시
  public ResponseEntity<Map<String, Long>> register(@RequestBody ReplyDTO replyDTO){
      log.info("=> replyDTO: "+replyDTO);

      Map<String, Long> resultMap = Map.of("rno", 222L);

    return ResponseEntity.ok(resultMap);
    // or => return new ResponseEntity(resultMap, HttpStatus.OK);
  }
  */

  // RestController예외처리 consumes = MediaType.APPLICATION_JSON_VALUE
  //@Parameter(name = "replyDTO", description = "@Valid어노테이션으로 replyDTO 유효성 검사")

  @Operation(summary="Replies POST", description="POST방식으로 댓글 등록")
  @PostMapping(value="/",consumes = MediaType.APPLICATION_JSON_VALUE )// 전송받은 data 종류 명시
  public Map<String, Long> register(
                    @Valid @RequestBody ReplyDTO replyDTO,
                    BindingResult bindingResult // 객체값 검증
                    ) throws BindException {

    log.info("=> replyDTO: "+replyDTO);
    log.info("=> bindingResult: "+bindingResult.toString());

    // 에러가 존재하면 BindException예외 발생시킴
    // => RestController예외처리를 해주는  @RestControllerAdvice어노테이션
    if (bindingResult.hasErrors()){
      throw new BindException(bindingResult);
    }

    Map<String, Long> resultMap = Map.of("rno", 222L);

    return resultMap;
  }


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