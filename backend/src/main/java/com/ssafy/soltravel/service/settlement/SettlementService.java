package com.ssafy.soltravel.service.settlement;

import com.ssafy.soltravel.domain.ForeignAccount;
import com.ssafy.soltravel.domain.GeneralAccount;
import com.ssafy.soltravel.domain.Participant;
import com.ssafy.soltravel.dto.settlement.SettlementRequestDto;
import com.ssafy.soltravel.dto.settlement.SettlementResponseDto;
import com.ssafy.soltravel.dto.transaction.request.ForeignTransactionRequestDto;
import com.ssafy.soltravel.dto.transaction.request.TransactionRequestDto;
import com.ssafy.soltravel.repository.ForeignAccountRepository;
import com.ssafy.soltravel.repository.ParticipantRepository;
import com.ssafy.soltravel.service.NotificationService;
import com.ssafy.soltravel.service.exchange.ExchangeService;
import com.ssafy.soltravel.service.transaction.TransactionService;
import com.ssafy.soltravel.util.LogUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SettlementService {

  private final ExchangeService exchangeService;
  private final TransactionService transactionService;
  private final NotificationService notificationService;
  private final ForeignAccountRepository foreignAccountRepository;
  private final ParticipantRepository participantRepository;

  public ResponseEntity<?> executeSettlement(SettlementRequestDto requestDto) {

    ForeignAccount foreignAccount = foreignAccountRepository.findById(requestDto.getAccountId())
        .orElseThrow();

    GeneralAccount generalAccount = foreignAccount.getGeneralAccount();
    long amount = exchangeService.convertUsdToKrwWithoutFee(foreignAccount.getBalance(),
        exchangeService.getExchangeRate(foreignAccount.getCurrency().getCurrencyCode())
            .getExchangeRate());

    LogUtil.info("모임통장에 입금될 amount: ", amount);

    //외화 계좌에서 출금
    ForeignTransactionRequestDto foreignTransactionRequestDto = new ForeignTransactionRequestDto();
    foreignTransactionRequestDto.setTransactionBalance(foreignAccount.getBalance());
    foreignTransactionRequestDto.setTransactionSummary("정산 출금");
    transactionService.postForeignWithdrawal(requestDto.getAccountNo(),
        foreignTransactionRequestDto);

    //일반 통장에 입금
    TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
    transactionRequestDto.setTransactionBalance(amount);
    transactionRequestDto.setTransactionSummary("정산 입금");
    transactionService.postAccountDeposit(generalAccount.getAccountNo(), transactionRequestDto);

    //정산
    divideBalance(generalAccount);

    return ResponseEntity.status(HttpStatus.OK).body("정산 완료");
  }

  public void divideBalance(GeneralAccount generalAccount) {

    long accountId= generalAccount.getId();
    String accountNo = generalAccount.getAccountNo();
    List<Participant> participants=participantRepository.findAllByGeneralAccountId(accountId);

    //나눠진 잔액
    double originAmount=generalAccount.getBalance();
    long amountPerPerson=Math.round(originAmount/participants.size());

    /**
     * 각 모임원에게 입금
     */
    LogUtil.info("각 모임원 입금 금액:",amountPerPerson);
    for(Participant participant:participants) {

      TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
      transactionRequestDto.setTransactionBalance(amountPerPerson);
      transactionRequestDto.setTransactionSummary("정산 입금");

      transactionService.postAccountDeposit(participant.getPersonalAccount().getAccountNo(),transactionRequestDto);

      //알림 전송
      SettlementResponseDto responseDto = new SettlementResponseDto();
      responseDto.setUserId(participant.getUser().getUserId());
      responseDto.setAccountId(accountId);
      responseDto.setAccountNo(accountNo);
      responseDto.setAmountPerPerson(amountPerPerson);
      responseDto.setMessage(String.format("고객님의 모임계좌[%s]에 정산이 실행되었습니다.", accountNo));

      notificationService.notifySettlementMessage(responseDto);
    }
  }
}
