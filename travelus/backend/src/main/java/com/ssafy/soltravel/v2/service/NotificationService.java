package com.ssafy.soltravel.v2.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import com.ssafy.soltravel.v2.domain.User;
import com.ssafy.soltravel.v2.domain.redis.RedisFcm;
import com.ssafy.soltravel.v2.dto.ResponseDto;
import com.ssafy.soltravel.v2.dto.exchange.ExchangeResponseDto;
import com.ssafy.soltravel.v2.dto.notification.PushNotificationRequestDto;
import com.ssafy.soltravel.v2.dto.notification.RegisterNotificationRequestDto;
import com.ssafy.soltravel.v2.dto.notification.TransactionNotificationDto;
import com.ssafy.soltravel.v2.dto.settlement.response.SettlementResponseDto;
import com.ssafy.soltravel.v2.exception.notification.FcmTokenNotFound;
import com.ssafy.soltravel.v2.repository.redis.FcmTokenRepository;
import com.ssafy.soltravel.v2.util.LogUtil;
import com.ssafy.soltravel.v2.util.SecurityUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    // Redis에서 구독 정보의 키에 사용할 접두사
    private static final String EMITTER_PREFIX = "EMITTER_";
    private final FcmTokenRepository fcmTokenRepository;
    private final RedisTemplate<String, SseEmitter> redisTemplate; // RedisTemplate을 사용하여 Redis에 접근
    private final SecurityUtil securityUtil;

    /**
     * 메시지 알림 구독
     */
    public SseEmitter subscribe(long userId) {

        LogUtil.info("알림구독요청", userId);
        //sseEmitter 객체 생성
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        //연결
        try {
            sseEmitter.send(SseEmitter.event().name("connect"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Redis에 저장
        redisTemplate.opsForValue().set(EMITTER_PREFIX + userId, sseEmitter, 2400, TimeUnit.HOURS); // 24시간 동안 유효

        sseEmitter.onCompletion(() -> redisTemplate.delete(EMITTER_PREFIX + userId));  // sseEmitter 연결 완료 시 제거
        sseEmitter.onTimeout(() -> redisTemplate.delete(EMITTER_PREFIX + userId));    // sseEmitter 연결 타임아웃 시 제거
        sseEmitter.onError((e) -> redisTemplate.delete(EMITTER_PREFIX + userId));    // sseEmitter 연결 오류 시 제거

        return sseEmitter;
    }

    /**
     * Redis에서 SseEmitter 가져오기
     */
    private SseEmitter getEmitter(long userId) {
        return redisTemplate.opsForValue().get(EMITTER_PREFIX + userId);
    }

    /**
     * 환전 알림
     */
    public void notifyExchangeMessage(ExchangeResponseDto exchangeResponseDto) {
//
//    String accountNo=exchangeResponseDto.getAccountInfoDto().getAccountNo();
//
//    List<Long> participants=accountService.findUserIdsByGeneralAccountId(exchangeResponseDto.getAccountInfoDto().getAccountId());
//
//    for(long userId : participants) {
//
//      SseEmitter sseEmitterReceiver = getEmitter(userId);
//
//      if (sseEmitterReceiver != null) {
//        //알림 전송
//        try {
//          String message = String.format("고객님의 모임계좌[%s]에 환전이 실행되었습니다.", accountNo);
//
//          ExchangeNotificationDto dto = new ExchangeNotificationDto(
//              exchangeResponseDto.getAccountInfoDto().getAccountId(),
//              accountNo,
//              exchangeResponseDto.getExchangeCurrencyDto().getExchangeRate().toString(),
//              message
//          );
//          sseEmitterReceiver.send(SseEmitter.event().name("Exchange").data(dto));
//        } catch (Exception e) {
//          redisTemplate.delete(EMITTER_PREFIX + userId);
//        }
//      }
//    }
    }

    /**
     * 정산 알림
     */
    public void notifySettlementMessage(SettlementResponseDto settlementResponseDto) {

        long userId = settlementResponseDto.getUserId();

        SseEmitter sseEmitterReceiver = getEmitter(userId);

        if (sseEmitterReceiver != null) {
            //알림 전송
            try {
                sseEmitterReceiver.send(SseEmitter.event().name("Settlement").data(settlementResponseDto));
            } catch (Exception e) {
                redisTemplate.delete(EMITTER_PREFIX + userId);
            }
        }
    }

    /**
     * 입금 알림
     */
    public void notifyTransactionMessage(TransactionNotificationDto transactionNotificationDto) {

        long userId = transactionNotificationDto.getUserId();

        SseEmitter sseEmitterReceiver = getEmitter(userId);

        if (sseEmitterReceiver != null) {
            //알림 전송
            try {
                sseEmitterReceiver.send(SseEmitter.event().name("Transaction").data(transactionNotificationDto));
            } catch (Exception e) {
                redisTemplate.delete(EMITTER_PREFIX + userId);
            }
        }
    }


    public void notifyAllUser() {

        for (String uId : redisTemplate.keys(EMITTER_PREFIX + "*")) {
            Long userId = Long.valueOf(uId.substring(8, uId.length()));
            LogUtil.info("for userId", userId);

            SseEmitter sseEmitterReceiver = getEmitter(userId);

            if (sseEmitterReceiver != null) {
                LogUtil.info("for SseEmitter", sseEmitterReceiver.toString());

                try {
                    sseEmitterReceiver.send(SseEmitter.event().name("all").data("notify!!!!!!"));
                } catch (Exception e) {
                    redisTemplate.delete(EMITTER_PREFIX + userId);
                }
            }
        }
    }

    // 토큰 레디스 저장
    public ResponseDto saveFcmToken(RegisterNotificationRequestDto requestDto) {

        LogUtil.info("requestDto", requestDto);

        User user = securityUtil.getUserByToken();

        fcmTokenRepository.save(new RedisFcm(user.getUserId(), requestDto.getFcmToken()));

        return new ResponseDto();
    }

    // 사용자에게 push 알림
    public ResponseEntity<?> pushNotification(PushNotificationRequestDto requestDto) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        try {

            // FCM 토큰 조회
            RedisFcm redisFcm = fcmTokenRepository.findById(requestDto.getTargetUserId())
                .orElseThrow(() -> new FcmTokenNotFound(requestDto.getTargetUserId()));

            // FCM 메시지 생성
            Message message = Message.builder()
                .setToken(redisFcm.getFcmToken())
                .setWebpushConfig(WebpushConfig.builder()
                    .putHeader("ttl", "300")
                    .setNotification(new WebpushNotification(requestDto.getTitle(), requestDto.getMessage()))
                    .build())
                .build();

            String response = FirebaseMessaging.getInstance().sendAsync(message).get();
            status = HttpStatus.OK;
            resultMap.put("response", response);
        } catch (Exception e) {
            resultMap.put("message", "요청 실패");
            resultMap.put("exception", e.getMessage());
        }

        return new ResponseEntity<>(resultMap, status);
    }

}


