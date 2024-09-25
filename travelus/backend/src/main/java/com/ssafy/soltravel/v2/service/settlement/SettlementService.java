package com.ssafy.soltravel.v2.service.settlement;

import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import com.ssafy.soltravel.v2.domain.Enum.SettlementType;
import com.ssafy.soltravel.v2.domain.Enum.TransactionType;
import com.ssafy.soltravel.v2.domain.Enum.TransferType;
import com.ssafy.soltravel.v2.dto.account.AccountDto;
import com.ssafy.soltravel.v2.dto.account.request.InquireAccountRequestDto;
import com.ssafy.soltravel.v2.dto.group.GroupDto;
import com.ssafy.soltravel.v2.dto.group.ParticipantDto;
import com.ssafy.soltravel.v2.dto.settlement.request.SettlementParticipantRequestDto;
import com.ssafy.soltravel.v2.dto.settlement.request.SettlementRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.request.MoneyBoxTransferRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.request.TransactionRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.response.TransferHistoryResponseDto;
import com.ssafy.soltravel.v2.exception.group.GroupMasterNotFoundException;
import com.ssafy.soltravel.v2.exception.participant.ParticipantNotFoundException;
import com.ssafy.soltravel.v2.service.account.AccountService;
import com.ssafy.soltravel.v2.service.exchange.ExchangeService;
import com.ssafy.soltravel.v2.service.group.GroupService;
import com.ssafy.soltravel.v2.service.transaction.TransactionService;
import com.ssafy.soltravel.v2.util.LogUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    public void executeSettlement(SettlementRequestDto settlementRequestDto) {

        SettlementType type = settlementRequestDto.getSettlementType();
        AccountDto account = accountService.getByAccountNo(settlementRequestDto.getAccountNo());

    GroupDto group = groupService.getGroupInfo(settlementRequestDto.getGroupId());
    List<ParticipantDto> participants = group.getParticipants();
    long masterUserId = participants.stream().filter(ParticipantDto::isMaster).findFirst()
        .orElseThrow(() -> new GroupMasterNotFoundException(group.getGroupId())).getUserId();

    switch (type) {
      case G -> {
        settleOnlyKRW(settlementRequestDto, masterUserId);
      }
      case F -> {
        settleOnlyForeign(settlementRequestDto, account.getMoneyBoxDtos().get(1).getCurrencyCode(),
            masterUserId);
      }
      case BOTH ->
          settleBoth(settlementRequestDto, account.getMoneyBoxDtos().get(1).getCurrencyCode(),
              masterUserId);
    }

    paySettlementToMembers(group.getGroupName(), participants,
        settlementRequestDto.getParticipants());
  }

  /**
   * 1. 원화만 정산
   */
  public void settleOnlyKRW(SettlementRequestDto settlementRequestDto, long masterUserId) {

    /**
     * 1. 원화만 정산
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
     * 2. 외화만 정산
     */
    transactionService.postAccountWithdrawal(
        new TransactionRequestDto(settlementRequestDto.getAccountNo(),
            settlementRequestDto.getAccountPassword(), currencyCode, TransactionType.SW,
            settlementRequestDto.getAmounts().get(1), "자동 정산 출금"), masterUserId);
  }

  /**
   * 모두 정산
   */
  public void settleBoth(SettlementRequestDto settlementRequestDto, CurrencyType currencyCode,
      long masterUserId) {

    /**
     * 모두 정산
     */
    public void settleBoth(SettlementRequestDto settlementRequestDto, CurrencyType currencyCode) {

        /**
         * 1.재환전
         * 2.원화 정산출금
         * 3.개인 정산입금
         */

        List<TransferHistoryResponseDto> response = transactionService.postMoneyBoxTransfer(
            MoneyBoxTransferRequestDto.create(TransferType.M, settlementRequestDto.getAccountNo(),
                settlementRequestDto.getAccountPassword(), currencyCode, CurrencyType.KRW,
                settlementRequestDto.getAmounts().get(1)), false, -1).getBody();

    transactionService.postAccountWithdrawal(
        new TransactionRequestDto(settlementRequestDto.getAccountNo(),
            settlementRequestDto.getAccountPassword(), CurrencyType.KRW, TransactionType.SW,
            settlementRequestDto.getAmounts().get(0) + transactionAmount, "자동 정산 출금"),
        masterUserId);
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

      transactionService.postAccountDeposit(
          new TransactionRequestDto(accountNo, null, CurrencyType.KRW, TransactionType.SD,
              String.valueOf(requestDto.getAmount()), String.format("[%s] 자동 정산 입금", groupName)),
          participant.getUserId());
    }

    /**
     * 정산금 계산
     */
    private double divideBalance(double amount, int N, boolean isKRW) {
        BigDecimal amountBD = BigDecimal.valueOf(amount);
        BigDecimal dividedAmount = amountBD.divide(BigDecimal.valueOf(N), RoundingMode.DOWN);

        if (isKRW) {
            // 원화일 경우 소수점 0자리에서 내림
            return dividedAmount.setScale(0, RoundingMode.DOWN).doubleValue();
        } else {
            // 외화일 경우 소수점 둘째 자리에서 내림
            return dividedAmount.setScale(2, RoundingMode.DOWN).doubleValue();
        }
    }
}
