package com.ssafy.soltravel.controller;

import com.ssafy.soltravel.dto.account_book.ReceiptUploadRequestDto;
import com.ssafy.soltravel.dto.account_book.ReceiptUploadResponseDto;
import com.ssafy.soltravel.service.account_book.AccountBookService;
import com.ssafy.soltravel.util.LogUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account-book")
@RequiredArgsConstructor
@Tag(name = "AccountBook", description = "가계부 관련 API")
public class AccountBookController {

  private final AccountBookService accountBookService;

  @Operation(summary = "영수증 업로드", description = "jpg, png, pdf 형식의 영수증을 업로드하고 그 정보를 받아옵니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "업로드 완료", content = @Content(schema = @Schema(implementation = ReceiptUploadResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
  })
  @PostMapping(value = "/upload/receipt", consumes = "multipart/form-data")
  public ResponseEntity<?> uploadReceipt(@ModelAttribute ReceiptUploadRequestDto requestDto) throws IOException {

    LogUtil.info("requested", requestDto.toString());
    ReceiptUploadResponseDto response = accountBookService.uploadReceipt(requestDto);
    return ResponseEntity.ok().body(response);
  }

}
