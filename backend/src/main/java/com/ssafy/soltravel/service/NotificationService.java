package com.ssafy.soltravel.service;

import com.ssafy.soltravel.dto.notification.ExchangeNotificationDto;
import com.ssafy.soltravel.dto.exchange.ExchangeResponseDto;
import com.ssafy.soltravel.dto.notification.TransactionNotificationDto;
import com.ssafy.soltravel.dto.settlement.SettlementResponseDto;
import com.ssafy.soltravel.service.account.AccountService;
import com.ssafy.soltravel.util.SecurityUtil;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

  public static Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
  private final AccountService accountService;
  /**
   * 메시지 알림 구독
   */
  public SseEmitter subscribe() {

    Long userId = SecurityUtil.getCurrentUserId();

    //sseEmitter 객체 생성
    SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

    //연결
    try {
      sseEmitter.send(SseEmitter.event().name("connect"));
    } catch (IOException e) {
      e.printStackTrace();
    }

    emitters.put(userId, sseEmitter);

    sseEmitter.onCompletion(() -> emitters.remove(userId));  // sseEmitter 연결 완료
    sseEmitter.onTimeout(() -> emitters.remove(userId));    // sseEmitter 연결 타임아웃
    sseEmitter.onError((e) -> emitters.remove(userId));    // sseEmitter 연결 오류

    return sseEmitter;
  }

  /**
   * 환전 알림
   */
  public void notifyExchangeMessage(ExchangeResponseDto exchangeResponseDto) {

    String accountNo=exchangeResponseDto.getAccountInfoDto().getAccountNo();

    List<Long> participants=accountService.findUserIdsByGeneralAccountId(exchangeResponseDto.getAccountInfoDto().getAccountId());

    for(long userId:participants){

      if (emitters.containsKey(userId)) {
        SseEmitter sseEmitterReceiver = emitters.get(userId);

        //알림 전송
        try {
          String message = String.format("고객님의 모임계좌[%s]에 환전이 실행되었습니다.", accountNo);

          ExchangeNotificationDto dto = new ExchangeNotificationDto(exchangeResponseDto.getAccountInfoDto().getAccountId(),accountNo, exchangeResponseDto.getExchangeCurrencyDto()
              .getExchangeRate().toString(), message);
          sseEmitterReceiver.send(SseEmitter.event().name("Exchange").data(dto));
        } catch (Exception e) {
          emitters.remove(userId);
        }
      }
    }
  }

  /**
   * 정산 알림
   */
  public void notifySettlementMessage(SettlementResponseDto settlementResponseDto) {

    long userId=settlementResponseDto.getUserId();

    if (emitters.containsKey(userId)) {
      SseEmitter sseEmitterReceiver = emitters.get(userId);

      //알림 전송
      try {
        sseEmitterReceiver.send(SseEmitter.event().name("Settlement").data(settlementResponseDto));
      } catch (Exception e) {
        emitters.remove(userId);
      }
    }
  }

  /**
   * 입금 알림
   */
  public void notifyTransactionMessage(TransactionNotificationDto transactionNotificationDto) {

    long userId=transactionNotificationDto.getUserId();

    if (emitters.containsKey(userId)) {
      SseEmitter sseEmitterReceiver = emitters.get(userId);

      //알림 전송
      try {
        sseEmitterReceiver.send(SseEmitter.event().name("Transaction").data(transactionNotificationDto));
      } catch (Exception e) {
        emitters.remove(userId);
      }
    }
  }
}
