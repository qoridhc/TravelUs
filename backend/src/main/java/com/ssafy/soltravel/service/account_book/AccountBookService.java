package com.ssafy.soltravel.service.account_book;

import com.ssafy.soltravel.domain.Enum.OrderByType;
import com.ssafy.soltravel.dto.account_book.AccountHistoryReadRequestDto;
import com.ssafy.soltravel.dto.account_book.AccountHistoryReadResponseDto;
import com.ssafy.soltravel.dto.account_book.AccountHistorySaveRequestDto;
import com.ssafy.soltravel.dto.account_book.AccountHistorySaveResponseDto;
import com.ssafy.soltravel.dto.account_book.ReceiptAnalysisDto;
import com.ssafy.soltravel.dto.account_book.ReceiptUploadRequestDto;
import com.ssafy.soltravel.dto.transaction.TransactionHistoryDto;
import com.ssafy.soltravel.dto.transaction.request.ForeignTransactionRequestDto;
import com.ssafy.soltravel.dto.transaction.request.TransactionHistoryRequestDto;
import com.ssafy.soltravel.exception.LackOfBalanceException;
import com.ssafy.soltravel.mapper.AccountBookMapper;
import com.ssafy.soltravel.mapper.TransactionMapper;
import com.ssafy.soltravel.repository.ForeignAccountRepository;
import com.ssafy.soltravel.service.AwsFileService;
import com.ssafy.soltravel.service.GPTService;
import com.ssafy.soltravel.service.transaction.TransactionService;
import com.ssafy.soltravel.util.SecurityUtil;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountBookService {

  // 외부 API 호출 Service
  private final AwsFileService fileService;
  private final ClovaOcrService ocrService;
  private final GPTService gptService;

  // 내부 API
  private final CashHistoryService cashHistoryService;
  private final TransactionService transactionService;

  // Repository
  private final ForeignAccountRepository foreignAccountRepository;

  /*
   * 영수증 업로드 & 정보 파싱해서 반환
   */
  public ReceiptAnalysisDto uploadReceipt(ReceiptUploadRequestDto requestDto)
      throws IOException {

    // 필요 변수 정의
    MultipartFile file = requestDto.getFile();
    Long userId = SecurityUtil.getCurrentUserId();

    // userId로 파일 저장(S3)
    String uploadUrl = fileService.saveReciept(file, userId);

    // Clova OCR 사용
    ResponseEntity<Map<String, Object>> response = ocrService.execute(requestDto, uploadUrl);

    // 챗지피티한테 정리시키기
    String receiptInfoString = gptService.askChatGPT(response.getBody().toString());

    // String(JSON) -> 객체 변환후 return
    return AccountBookMapper.convertJSONToItemAnalysisDto(receiptInfoString);
  }


  /*
   * 영수증 정보(+사용자 입력 정보)로 가계부 등록
   */
  public AccountHistorySaveResponseDto saveAccountHistory(AccountHistorySaveRequestDto requestDto)
      throws LackOfBalanceException {

    // 현금 사용으로 출금 신청
    ForeignTransactionRequestDto withRequest = ForeignTransactionRequestDto.builder()
        .transactionBalance(requestDto.getPaid())
        .transactionSummary("현금 사용")
        .build();
    transactionService.postForeignWithdrawal(false, requestDto.getAccountNo(), withRequest);

    // 현금 사용 가계 등록
    // Double newBalance = cashHistoryService.payCash(
    //     foreignAccount, requestDto.getPaid()
    // );

    return AccountHistorySaveResponseDto.builder()
        .message("가계부가 등록되었습니다.")
        .build();
  }


  /*
   * 가계부 조회
   */
  public AccountHistoryReadResponseDto findAccountHistory(String accountNo, AccountHistoryReadRequestDto request) {

    AccountHistoryReadResponseDto response = AccountHistoryReadResponseDto.builder()
        .accountNo(accountNo)
        .build();
    response.initHistoryList();

    // (가계부 요청 데이터)를 (외화 통장 이체 기록 요청 데이터)로 변환
    TransactionHistoryRequestDto transactionDto =
        TransactionMapper.convertaccountToTransaction(request);
    transactionDto.setOrderByType(OrderByType.ASC);

    // 변환한 데이터로 이체 기록 요청
    List<TransactionHistoryDto> transactionHistoryList =
        transactionService.getForeignHistoryByAccountNo(accountNo, transactionDto);

    // 이체 기록을 가계부에 저장
    updateAccountHistoryFromTransactions(
        response.getMonthHistoryList(),
        transactionHistoryList
    );

    //현금 가계 기록 조회

    return response;
  }

  /*
   * findAccountHistory에서 사용(이체 기록을 가계 기록으로 변경)
   */
  private void updateAccountHistoryFromTransactions(
      List<AccountHistoryReadResponseDto.DayAccountHistory> monthAccountBook,
      List<TransactionHistoryDto> transactionHistoryList
  ) {

    // 외화 통장 이체 기록을 순회하면서 응답 DTO에 저장
    for (TransactionHistoryDto transaction : transactionHistoryList) {

      // 일자를 인덱스로 사용
      int date = Integer.parseInt(transaction.getTransactionDate().substring(6, 8));

      // 거래 가격 저장
      Double amount = Double.parseDouble(transaction.getTransactionBalance());

      // 입/출금에 따라 저장
      switch (transaction.getTransactionType()) {
        case "1": // 입금
          monthAccountBook.get(date).addTotalIncome(amount);
          break;

        case "2": // 출금
          monthAccountBook.get(date).addTotalExpenditure(amount);
          break;
      }
    }
  }

  /*
   * findAccountHistory에서 사용(현금 기록을 가계 기록으로 변경)
   */
//  private void updateAccountHistoryFromCash(
//      List<AccountHistoryReadResponseDto.DayAccountHistory> monthAccountBook,
//      List<CashHistory> cashHistoryList
//  ) {
//
//    // 현금 사용 기록을 순회하면서 응답 DTO에 저장
//    for(CashHistory history : cashHistoryList) {
//
//      // 일자를 인덱스로 사용
//      int date = history.getTransactionAt().getDayOfMonth();
//
//      // 현금 사용 기록만 있어서 그대로 빼주기
//      monthAccountBook.get(date).addTotalExpenditure(history.getAmount());
//    }
//  }

}


























