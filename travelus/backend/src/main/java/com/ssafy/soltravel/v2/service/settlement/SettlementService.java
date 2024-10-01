package com.ssafy.soltravel.v2.service.settlement;

import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import com.ssafy.soltravel.v2.domain.Enum.SettlementType;
import com.ssafy.soltravel.v2.domain.Enum.TransactionType;
import com.ssafy.soltravel.v2.domain.Enum.TransferType;
import com.ssafy.soltravel.v2.dto.account.AccountDto;
import com.ssafy.soltravel.v2.dto.group.GroupDto;
import com.ssafy.soltravel.v2.dto.group.ParticipantDto;
import com.ssafy.soltravel.v2.dto.settlement.request.SettlementParticipantRequestDto;
import com.ssafy.soltravel.v2.dto.settlement.request.SettlementRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.request.MoneyBoxTransferRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.request.TransactionRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.response.TransactionResponseDto;
import com.ssafy.soltravel.v2.dto.transaction.response.TransferHistoryResponseDto;
import com.ssafy.soltravel.v2.exception.group.GroupMasterNotFoundException;
import com.ssafy.soltravel.v2.exception.participant.ParticipantNotFoundException;
import com.ssafy.soltravel.v2.service.account.AccountService;
import com.ssafy.soltravel.v2.service.group.GroupService;
import com.ssafy.soltravel.v2.service.transaction.TransactionService;
import com.ssafy.soltravel.v2.util.LogUtil;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SettlementService {

  private final TransactionService transactionService;
  private final AccountService accountService;
  private final GroupService groupService;

  public String executeSettlement(SettlementRequestDto settlementRequestDto) {

    SettlementType type = settlementRequestDto.getSettlementType();
    AccountDto account = accountService.getByAccountNo(settlementRequestDto.getAccountNo());

    GroupDto group = groupService.getGroupInfo(settlementRequestDto.getGroupId());
    List<ParticipantDto> participants = group.getParticipants();
    long masterUserId = participants.stream().filter(ParticipantDto::isMaster).findFirst()
        .orElseThrow(() -> new GroupMasterNotFoundException(group.getGroupId())).getUserId();

    String response = "success";
    switch (type) {
      case G -> {
        settleOnlyKRW(settlementRequestDto, masterUserId);
      }
      case F -> {
        settleOnlyForeign(settlementRequestDto, account.getMoneyBoxDtos().get(1).getCurrencyCode(),
            masterUserId);
      }
      case BOTH -> response = settleBoth(settlementRequestDto, account.getMoneyBoxDtos().get(1).getCurrencyCode(),
          masterUserId);
    }

    paySettlementToMembers(group.getGroupName(), participants,
        settlementRequestDto.getParticipants());

    return response;
  }

  /**
   * 1. 원화만 정산
   */
  public void settleOnlyKRW(SettlementRequestDto settlementRequestDto, long masterUserId) {

    /**
     * 2.원화 출금
     * 3.개인 입금
     */
    transactionService.postAccountWithdrawal(
        new TransactionRequestDto(settlementRequestDto.getAccountNo(),
            settlementRequestDto.getAccountPassword(), CurrencyType.KRW, TransactionType.SW,
            settlementRequestDto.getAmounts().get(0), "자동 정산 출금"), masterUserId);
  }

  /**
   * 2. 외화만 정산
   */
  public void settleOnlyForeign(SettlementRequestDto settlementRequestDto,
      CurrencyType currencyCode, long masterUserId) {

    /**
     * 2.외화 정산출금
     * 3.개인 정산입금
     */
    transactionService.postAccountWithdrawal(
        new TransactionRequestDto(settlementRequestDto.getAccountNo(),
            settlementRequestDto.getAccountPassword(), currencyCode, TransactionType.SW,
            settlementRequestDto.getAmounts().get(1), "자동 정산 출금"), masterUserId);
  }

  /**
   * 모두 정산
   */
  public String settleBoth(SettlementRequestDto settlementRequestDto, CurrencyType currencyCode,
      long masterUserId) {

    /**
     * 1.재환전
     * 2.원화 정산출금
     * 3.개인 정산입금
     */

    List<TransferHistoryResponseDto> response = transactionService.postMoneyBoxTransfer(
        MoneyBoxTransferRequestDto.create(TransferType.M, settlementRequestDto.getAccountNo(),
            settlementRequestDto.getAccountPassword(), currencyCode, CurrencyType.KRW,
            settlementRequestDto.getAmounts().get(1)), false, -1).getBody();

    BigDecimal transactionAmount = new BigDecimal(response.get(1).getTransactionAmount()); // 재환전된 원화 금액
    BigDecimal krwAmount = new BigDecimal(settlementRequestDto.getAmounts().get(0)); // 원화 금액
    BigDecimal transactionBalance = transactionAmount.add(krwAmount);

    LogUtil.info("재환전된 원화 금액", transactionAmount);
    LogUtil.info("정산신청 원화 금액", krwAmount);
    LogUtil.info("정산될 총액", transactionBalance.toString());

    transactionService.postAccountWithdrawal(
        new TransactionRequestDto(settlementRequestDto.getAccountNo(),
            settlementRequestDto.getAccountPassword(), CurrencyType.KRW, TransactionType.SW,
            transactionBalance.toString(), "자동 정산 출금"),
        masterUserId);

    return response.get(0).getTransactionSummary();
  }

  /**
   * 개인 정산금 지급
   */
  private void paySettlementToMembers(String groupName, List<ParticipantDto> participants,
      List<SettlementParticipantRequestDto> requestDtos) {

    for (SettlementParticipantRequestDto requestDto : requestDtos) {

      long participantId = requestDto.getParticipantId();
      ParticipantDto participant = participants.stream()
          .filter(p -> p.getParticipantId().equals(participantId)).findFirst()
          .orElseThrow(() -> new ParticipantNotFoundException(participantId));

      String accountNo = participant.getPersonalAccountNo();

//      transactionService.postAccountDeposit(
//          new TransactionRequestDto(accountNo, null, CurrencyType.KRW, TransactionType.SD,
//              String.valueOf(requestDto.getAmount()), String.format("[%s] 자동 정산 입금", groupName)),
//          participant.getUserId());

      // TODO:디버깅용으므로 위의 버전으로 바꿀 것
      TransactionResponseDto response = transactionService.postAccountDeposit(
          new TransactionRequestDto(accountNo, null, CurrencyType.KRW, TransactionType.SD,
              String.valueOf(requestDto.getAmount()), String.format("[%s] 자동 정산 입금", groupName)),
          participant.getUserId()).getBody();

      LogUtil.info(String.format("개별정산금: %s", response.getTransactionAmount()));
    }
  }
}
