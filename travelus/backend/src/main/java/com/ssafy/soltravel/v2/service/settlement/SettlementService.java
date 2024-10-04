package com.ssafy.soltravel.v2.service.settlement;

import com.ssafy.soltravel.v2.domain.Enum.CurrencyType;
import com.ssafy.soltravel.v2.domain.Enum.SettlementStatus;
import com.ssafy.soltravel.v2.domain.Enum.SettlementType;
import com.ssafy.soltravel.v2.domain.Enum.TransactionType;
import com.ssafy.soltravel.v2.domain.Enum.TransferType;
import com.ssafy.soltravel.v2.domain.Participant;
import com.ssafy.soltravel.v2.domain.PersonalSettlementHistory;
import com.ssafy.soltravel.v2.domain.TravelGroup;
import com.ssafy.soltravel.v2.dto.account.AccountDto;
import com.ssafy.soltravel.v2.dto.group.GroupDto;
import com.ssafy.soltravel.v2.dto.group.ParticipantDto;
import com.ssafy.soltravel.v2.dto.settlement.request.GroupSettlementHistoryRequestDto;
import com.ssafy.soltravel.v2.dto.settlement.request.PersonalSettlementHistoryRequestDto;
import com.ssafy.soltravel.v2.dto.settlement.request.PersonalSettlementRegisterRequestDto;
import com.ssafy.soltravel.v2.dto.settlement.request.PersonalSettlementTransferRequestDto;
import com.ssafy.soltravel.v2.dto.settlement.request.SettlementParticipantRequestDto;
import com.ssafy.soltravel.v2.dto.settlement.request.SettlementRequestDto;
import com.ssafy.soltravel.v2.dto.settlement.response.GroupSettlementResponseDto;
import com.ssafy.soltravel.v2.dto.settlement.response.PersonalSettlementDto;
import com.ssafy.soltravel.v2.dto.settlement.response.PersonalSettlementHistoryDto;
import com.ssafy.soltravel.v2.dto.transaction.request.MoneyBoxTransferRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.request.TransactionRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.request.TransferRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.response.TransferHistoryResponseDto;
import com.ssafy.soltravel.v2.exception.group.GroupMasterNotFoundException;
import com.ssafy.soltravel.v2.exception.participant.ParticipantNotFoundException;
import com.ssafy.soltravel.v2.exception.settlement.PersonalSettlementHistoryNotFoundException;
import com.ssafy.soltravel.v2.mapper.SettlementMapper;
import com.ssafy.soltravel.v2.repository.ParticipantRepository;
import com.ssafy.soltravel.v2.repository.PersonalSettlementHistoryRepository;
import com.ssafy.soltravel.v2.service.account.AccountService;
import com.ssafy.soltravel.v2.service.group.GroupService;
import com.ssafy.soltravel.v2.service.transaction.TransactionService;
import com.ssafy.soltravel.v2.util.LogUtil;
import com.ssafy.soltravel.v2.util.SecurityUtil;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SettlementService {

  private final TransactionService transactionService;
  private final AccountService accountService;
  private final GroupService groupService;
  private final PersonalSettlementHistoryRepository personalSettlementHistoryRepository;
  private final ParticipantRepository participantRepository;

  private final SecurityUtil securityUtil;
  private final SettlementMapper settlementMapper;

  @Transactional
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
        settleOnlyForeign(settlementRequestDto, account.getMoneyBoxDtos().get(1).getCurrencyCode(), masterUserId);
      }
      case BOTH -> response = settleBoth(settlementRequestDto, account.getMoneyBoxDtos().get(1).getCurrencyCode(), masterUserId);
    }

    paySettlementToMembers(group.getGroupName(), participants, settlementRequestDto.getParticipants());

    return response;
  }

  /**
   * 1. 원화만 정산
   */
  @Transactional
  public void settleOnlyKRW(SettlementRequestDto settlementRequestDto, long masterUserId) {

    /**
     * 2.원화 출금
     * 3.개인 입금
     */
    transactionService.postAccountWithdrawal(
        new TransactionRequestDto(settlementRequestDto.getAccountNo(), settlementRequestDto.getAccountPassword(),
            CurrencyType.KRW, TransactionType.SW, settlementRequestDto.getAmounts().get(0), "자동 정산 출금"), masterUserId);
  }

  /**
   * 2. 외화만 정산
   */
  @Transactional
  public void settleOnlyForeign(SettlementRequestDto settlementRequestDto, CurrencyType currencyCode, long masterUserId) {

    /**
     * 2.외화 정산출금
     * 3.개인 정산입금
     */
    transactionService.postAccountWithdrawal(
        new TransactionRequestDto(settlementRequestDto.getAccountNo(), settlementRequestDto.getAccountPassword(), currencyCode,
            TransactionType.SW, settlementRequestDto.getAmounts().get(1), "자동 정산 출금"), masterUserId);
  }

  /**
   * 모두 정산
   */
  @Transactional
  public String settleBoth(SettlementRequestDto settlementRequestDto, CurrencyType currencyCode, long masterUserId) {

    /**
     * 1.재환전
     * 2.원화 정산출금
     * 3.개인 정산입금
     */

    List<TransferHistoryResponseDto> response = transactionService.postMoneyBoxTransfer(
        MoneyBoxTransferRequestDto.create(TransferType.M, settlementRequestDto.getAccountNo(),
            settlementRequestDto.getAccountPassword(), currencyCode, CurrencyType.KRW, settlementRequestDto.getAmounts().get(1)),
        false, -1).getBody();

    BigDecimal transactionAmount = new BigDecimal(response.get(1).getTransactionAmount()); // 재환전된 원화 금액
    BigDecimal krwAmount = new BigDecimal(settlementRequestDto.getAmounts().get(0)); // 원화 금액
    BigDecimal transactionBalance = transactionAmount.add(krwAmount);

    LogUtil.info("재환전된 원화 금액", transactionAmount);
    LogUtil.info("정산신청 원화 금액", krwAmount);
    LogUtil.info("정산될 총액", transactionBalance.toString());

    transactionService.postAccountWithdrawal(
        new TransactionRequestDto(settlementRequestDto.getAccountNo(), settlementRequestDto.getAccountPassword(),
            CurrencyType.KRW, TransactionType.SW, transactionBalance.toString(), "자동 정산 출금"), masterUserId);

    return response.get(0).getTransactionSummary();
  }

  /**
   * 개인 정산금 지급
   */
  @Transactional
  public void paySettlementToMembers(String groupName, List<ParticipantDto> participants,
      List<SettlementParticipantRequestDto> requestDtos) {

    for (SettlementParticipantRequestDto requestDto : requestDtos) {

      long participantId = requestDto.getParticipantId();
      ParticipantDto participant = participants.stream().filter(p -> p.getParticipantId().equals(participantId)).findFirst()
          .orElseThrow(() -> new ParticipantNotFoundException(participantId));

      String accountNo = participant.getPersonalAccountNo();

      transactionService.postAccountDeposit(
          new TransactionRequestDto(accountNo, null, CurrencyType.KRW, TransactionType.SD, String.valueOf(requestDto.getAmount()),
              String.format("[%s] 자동 정산 입금", groupName)), participant.getUserId());
    }
  }

  /**
   * 개별 정산 알림 요청 메서드
   */
  @Transactional
  public String registerPersonalSettlement(PersonalSettlementRegisterRequestDto requestDto) {

    List<SettlementParticipantRequestDto> participants = requestDto.getParticipants();
    LocalDateTime now = LocalDateTime.now();
    Long id = getNextSettlementId();
    for (SettlementParticipantRequestDto request : participants) {

      Participant participant = participantRepository.findById(request.getParticipantId())
          .orElseThrow(() -> new ParticipantNotFoundException(request.getParticipantId()));

      TravelGroup group = participant.getGroup();
      PersonalSettlementHistory history = PersonalSettlementHistory.createPersonalSettlementHistory(id, participant, group,
          request.getAmount(), now);
      personalSettlementHistoryRepository.save(history);

      //TODO: 알림 보내기

    }

    return "success";
  }

  /**
   * 개인별 - 개별 정산 내역 조회 메서드
   */
  public List<PersonalSettlementDto> getPersonalSettlementHistory(PersonalSettlementHistoryRequestDto requestDto) {

    long userId = securityUtil.getCurrentUserId();
    List<PersonalSettlementHistory> response = personalSettlementHistoryRepository.findByUserIdAndSettlementStatus(userId,
        requestDto.getSettlementStatus()).orElseThrow(() -> new PersonalSettlementHistoryNotFoundException(userId));

    return settlementMapper.toPersonalSettlementNotificationListDto(response);
  }

  /**
   * 모임별 - 개별 정산 내역 조회
   */
  public List<GroupSettlementResponseDto> getGroupSettlementHistory(GroupSettlementHistoryRequestDto requestDto) {

    // 모든 PersonalSettlementHistory 엔티티 조회
    List<PersonalSettlementHistory> settlementHistories = personalSettlementHistoryRepository.findByGroupIdGroupedById(requestDto.getGroupId());

    // personalSettlementId로 그룹핑
    Map<Long, List<PersonalSettlementHistory>> groupedBySettlementId = settlementHistories.stream()
        .collect(Collectors.groupingBy(PersonalSettlementHistory::getId));

    // 각 그룹을 GroupSettlementResponseDto로 변환
    return groupedBySettlementId.entrySet().stream()
        .map(entry -> {
          List<PersonalSettlementHistory> groupedHistories = entry.getValue();
          // 첫 번째 항목을 기준으로 그룹 정보 설정
          PersonalSettlementHistory firstHistory = groupedHistories.get(0);

          // GroupSettlementResponseDto 생성
          GroupSettlementResponseDto responseDto = settlementMapper.toGroupSettlementResponseDto(firstHistory);

          // participants 리스트 매핑 (매퍼 사용)
          List<PersonalSettlementHistoryDto> participants = settlementMapper.toPersonalSettlementHistoryDtoList(groupedHistories);

          responseDto.setParticipants(participants);

          // 모든 participants가 COMPLETED이면 isSettled를 COMPLETED로 설정
          boolean allCompleted = participants.stream()
              .allMatch(p -> p.getIsSettled() == SettlementStatus.COMPLETED);

          responseDto.setIsSettled(allCompleted ? SettlementStatus.COMPLETED : SettlementStatus.NOT_COMPLETED);

          return responseDto;
        })
        .collect(Collectors.toList());
  }

  /**
   * 개별 정산금 이체 메서드
   */
  @Transactional
  public ResponseEntity<List<TransferHistoryResponseDto>> postPersonalSettlementTransfer(
      PersonalSettlementTransferRequestDto requestDto) {

    ResponseEntity<List<TransferHistoryResponseDto>> response = transactionService.postGeneralTransfer(
        TransferRequestDto.builder().transferType(TransferType.G).withdrawalAccountNo(requestDto.getWithdrawalAccountNo())
            .withdrawalTransactionSummary(requestDto.getWithdrawalTransactionSummary())
            .depositAccountNo(requestDto.getDepositAccountNo())
            .depositTransactionSummary(requestDto.getDepositTransactionSummary()).accountPassword(requestDto.getAccountPassword())
            .transactionBalance(requestDto.getTransactionBalance()).build());

    long historyId = requestDto.getPersonalSettlementId();
    long participantId = requestDto.getParticipantId();
    PersonalSettlementHistory history = personalSettlementHistoryRepository.findByIdAndParticipantId(historyId, participantId)
        .orElseThrow(() -> new PersonalSettlementHistoryNotFoundException(historyId, participantId));

    BigDecimal currentRemainingAmount = BigDecimal.valueOf(history.getRemainingAmount());
    BigDecimal transactionAmount = BigDecimal.valueOf(requestDto.getTransactionBalance());

    BigDecimal afterRemainingAmount = currentRemainingAmount.subtract(transactionAmount);
    history.setRemainingAmount(afterRemainingAmount.doubleValue());

    if (afterRemainingAmount.compareTo(BigDecimal.ZERO) <= 0) {

      history.setIsSettled(SettlementStatus.COMPLETED);
      personalSettlementHistoryRepository.save(history);
    }

    return response;
  }

  /**
   * 다음 정산 기록 id 조회
   */
  @Transactional(readOnly = true)
  public Long getNextSettlementId() {

    Long nextId = personalSettlementHistoryRepository.findMaxHistoryId();
    return (nextId == null) ? 1L : nextId + 1;
  }
}
