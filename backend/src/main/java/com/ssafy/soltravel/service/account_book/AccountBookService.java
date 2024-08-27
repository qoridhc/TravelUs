package com.ssafy.soltravel.service.account_book;

import com.ssafy.soltravel.dto.account_book.ReceiptUploadRequestDto;
import com.ssafy.soltravel.dto.account_book.ReceiptUploadResponseDto;
import com.ssafy.soltravel.service.AwsFileService;
import com.ssafy.soltravel.util.LogUtil;
import com.ssafy.soltravel.util.SecurityUtil;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountBookService {

  private final AwsFileService fileService;
  private final ClovaOcrService ocrService;

  public ReceiptUploadResponseDto uploadReceipt(ReceiptUploadRequestDto requestDto) 
      throws IOException {
    
    // 필요 변수 정의
    MultipartFile file = requestDto.getFile();
    Long userId = SecurityUtil.getCurrentUserId();
    
    // userId로 파일 저장(S3)
    String uploadUrl = fileService.saveReciept(file, userId);
    LogUtil.info("upload", uploadUrl);
    
    // Clova OCR 사용
    ocrService.execute(requestDto, uploadUrl);
    
    //TODO: 챗 지피티한테 파싱 시키기
    return ReceiptUploadResponseDto.builder()
        .message("영수증 사진 업로드 완료")
        .uploadUrl(uploadUrl)
        .build();
  }

}
