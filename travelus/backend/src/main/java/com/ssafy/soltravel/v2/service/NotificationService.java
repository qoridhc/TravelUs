package com.ssafy.soltravel.v2.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import com.ssafy.soltravel.v1.dto.user.UserDetailDto;
import com.ssafy.soltravel.v2.domain.Enum.AccountType;
import com.ssafy.soltravel.v2.domain.Enum.NotificationType;
import com.ssafy.soltravel.v2.domain.Notification;
import com.ssafy.soltravel.v2.domain.TravelGroup;
import com.ssafy.soltravel.v2.domain.User;
import com.ssafy.soltravel.v2.domain.redis.RedisFcm;
import com.ssafy.soltravel.v2.dto.ResponseDto;
import com.ssafy.soltravel.v2.dto.account.AccountDto;
import com.ssafy.soltravel.v2.dto.exchange.targetAccountDto;
import com.ssafy.soltravel.v2.dto.notification.NotificationDto;
import com.ssafy.soltravel.v2.dto.notification.PushNotificationRequestDto;
import com.ssafy.soltravel.v2.dto.notification.RegisterNotificationRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.request.MoneyBoxTransferRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.request.TransactionRequestDto;
import com.ssafy.soltravel.v2.dto.transaction.request.TransferRequestDto;
import com.ssafy.soltravel.v2.exception.group.InvalidGroupIdException;
import com.ssafy.soltravel.v2.exception.notification.FcmTokenNotFound;
import com.ssafy.soltravel.v2.exception.notification.NotificationNotFoundException;
import com.ssafy.soltravel.v2.exception.user.UserNotFoundException;
import com.ssafy.soltravel.v2.mapper.NotificationMapper;
import com.ssafy.soltravel.v2.repository.GroupRepository;
import com.ssafy.soltravel.v2.repository.NotificationRepository;
import com.ssafy.soltravel.v2.repository.UserRepository;
import com.ssafy.soltravel.v2.repository.redis.FcmTokenRepository;
import com.ssafy.soltravel.v2.service.account.AccountService;
import com.ssafy.soltravel.v2.util.SecurityUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    // 기본 알림 아이콘
    private static final String DEFAULT_ICON_URL = "/sol_favicon.ico";

    private final AccountService accountService;

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final FcmTokenRepository fcmTokenRepository;
    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    private final SecurityUtil securityUtil;


    /*
     * FCM 토큰 레디스 저장
     */
    public ResponseDto saveFcmToken(RegisterNotificationRequestDto requestDto) {

        User user = securityUtil.getUserByToken();

        fcmTokenRepository.save(new RedisFcm(user.getUserId(), requestDto.getFcmToken()));

        return new ResponseDto();
    }

    /*
     * ===== 알림 전송 공통 메서드 =====
     */

    // 공통 푸시 알림 생성 메서드
    private Message createMessage(String token, PushNotificationRequestDto requestDto) {
        return Message.builder()
            .setToken(token)  // 대상 사용자 토큰
            .setWebpushConfig(WebpushConfig.builder()
                .putHeader("ttl", "300")  // 타임 투 라이브 설정 (예: 300초)
                .setNotification(WebpushNotification.builder() // 알림 내용 생성
                    .setTitle(requestDto.getTitle())
                    .setBody(requestDto.getMessage())
                    .setIcon(requestDto.getIcon())
                    .build())
                .build())
            .putData("notificationType", requestDto.getNotificationType().toString()) // 알림 외 필요 데이터 추가
            .putData("groupId", String.valueOf(requestDto.getGroupId()))
            .putData("accountNo", requestDto.getAccountNo() != null ? requestDto.getAccountNo() : "")
            .putData("currencyCode",
                requestDto.getCurrencyType() != null ? requestDto.getCurrencyType().getCurrencyCode() : "")
            .build();
    }

    // 알림 전송 공통 메서드 - 사용자에게 알림 푸시
    public ResponseEntity<?> pushNotification(PushNotificationRequestDto requestDto) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        try {
            // FCM 토큰 조회
            RedisFcm redisFcm = fcmTokenRepository.findById(requestDto.getTargetUserId())
                .orElseThrow(() -> new FcmTokenNotFound(requestDto.getTargetUserId()));

            // 메시지 생성 및 전송
            Message message = createMessage(redisFcm.getFcmToken(), requestDto);

            String response = FirebaseMessaging.getInstance().sendAsync(message).get();
            status = HttpStatus.OK;
            resultMap.put("response", response);

        } catch (Exception e) {
            resultMap.put("message", "요청 실패");
            resultMap.put("exception", e.getMessage());
        }

        // 알림 저장
        saveNotification(requestDto);

        return new ResponseEntity<>(resultMap, status);
    }

    // 알림 저장 공통 메서드 - DB에 생성된 알림 저장
    public void saveNotification(PushNotificationRequestDto requestDto) {

        User user = userRepository.findByUserId(requestDto.getTargetUserId())
            .orElseThrow(() -> new UserNotFoundException(requestDto.getTargetUserId()));

        TravelGroup group = null;

        if (requestDto.getGroupId() != null) {
            group = groupRepository.findById(requestDto.getGroupId()).orElseThrow(InvalidGroupIdException::new);
        }

        Notification notification = Notification.createNotification(user, group, requestDto);

        notificationRepository.save(notification);
    }

    /*
     * ===== 거래 종류 별 알림 전송 메서드 =====
     */
    public ResponseEntity<?> sendDepositNotification(Long userId, TransactionRequestDto requestDto) {

        String title;
        String message;

        // 트랜잭션 타입에 따라 switch-case로 알림 제목 및 메시지 설정
        switch (requestDto.getTransactionType()) {
            case D -> {
                title = "입금 완료";
                message = "입금 금액: " + requestDto.getTransactionBalance() + "원이 입금되었습니다.";
            }
            case W -> {
                title = "출금 완료";
                message = "출금 금액: " + requestDto.getTransactionBalance() + "원이 출금되었습니다.";
            }
            case SD -> {
                title = "정산 입금 완료";
                message = "정산 입금 금액: " + requestDto.getTransactionBalance() + "원이 입금되었습니다.";
            }
            case SW -> {
                title = "정산 출금 완료";
                message = "정산 출금 금액: " + requestDto.getTransactionBalance() + "원이 출금되었습니다.";
            }
            default -> {
                throw new IllegalArgumentException("지원되지 않는 트랜잭션 타입입니다.");
            }
        }

        AccountDto accountDto = accountService.getByAccountNo(requestDto.getAccountNo());

        NotificationType notificationType = NotificationType.PT;

        TravelGroup group = null;

        if (accountDto.getAccountType().equals(AccountType.G)) {
            title = "모임통장 " + title;
            message = "모임통장 " + message;
            notificationType = NotificationType.GT;

            group = groupRepository.findByGroupAccountNo(requestDto.getAccountNo());
        }

        // 알림 전송을 위한 Dto 생성 및 전송
        PushNotificationRequestDto notificationRequestDto = PushNotificationRequestDto.createDto(
            userId,
            notificationType,
            title,
            message,
            requestDto.getAccountNo(),
            (group != null) ? group.getGroupId() : null
        );

        return pushNotification(notificationRequestDto);
    }

    /*
     * 이체 알림
     */
    public ResponseEntity<?> sendTransferNotification(User user, TransferRequestDto requestDto) {

        // 돈을 받는 사람 유저 정보
        UserDetailDto depositUser = accountService.getUserByAccountNo(requestDto.getDepositAccountNo());

        String title = "이체 완료";
        String message = depositUser.getName() + "님에게 : " + requestDto.getTransactionBalance() + "원을 보냈어요.";

        AccountDto senderAccount = accountService.getByAccountNo(requestDto.getWithdrawalAccountNo());

        NotificationType notificationType = NotificationType.PT;
        TravelGroup group = null;

        if (senderAccount.getAccountType().equals(AccountType.G)) {
            title = "모임통장 " + title;
            group = groupRepository.findByGroupAccountNo(requestDto.getWithdrawalAccountNo());
            notificationType = NotificationType.GT;
        }

        // 돈을 보내는 사람
        PushNotificationRequestDto notificationRequestDto = PushNotificationRequestDto.createDto(
            user.getUserId(),
            notificationType,
            title,
            message,
            requestDto.getWithdrawalAccountNo(),
            (group != null) ? group.getGroupId() : null
        );

        // 알림 전송
        pushNotification(notificationRequestDto);

        title = user.getName() + "님이 돈을 보냈어요";
        message = requestDto.getTransactionBalance() + "원이 튜나은행 계좌로 입금되었어요.";

        AccountDto depositAccount = accountService.getByAccountNo(requestDto.getDepositAccountNo());
        notificationType = NotificationType.PT;

        TravelGroup depositGroup = null;

        if (depositAccount.getAccountType().equals(AccountType.G)) {
            depositGroup = groupRepository.findByGroupAccountNo(requestDto.getDepositAccountNo());

            message = requestDto.getTransactionBalance() + "원이 " + depositGroup.getGroupName() + " 모임통장으로 입금되었어요.";
            notificationType = NotificationType.GT;
        }

        PushNotificationRequestDto depositNotificationRequestDto = PushNotificationRequestDto.createDto(
            depositUser.getId(),
            notificationType,
            title,
            message,
            requestDto.getDepositAccountNo(),
            (depositGroup != null) ? depositGroup.getGroupId() : null
        );

        // 알림 전송
        pushNotification(depositNotificationRequestDto);

        return ResponseEntity.ok().body(new ResponseDto());
    }

    /*
     * 자동 환전 알림 전송
     */
    public ResponseEntity<?> sendAutoExchangeNotification(
        targetAccountDto targetAccountDto,
        MoneyBoxTransferRequestDto requestDto
    ) {

        String title = "자동 환전 완료";
        String message = "설정 환율 " + targetAccountDto.getTargetRate() + "에 도달하여 " + requestDto.getTransactionBalance()
            + requestDto.getSourceCurrencyCode() + " 이 성공적으로 환전되었습니다.";

        TravelGroup group = groupRepository.findByGroupAccountNo(targetAccountDto.getAccountNo());

        PushNotificationRequestDto notificationRequestDto = PushNotificationRequestDto.createDto(
            targetAccountDto.getUserId(),
            NotificationType.E,
            title,
            message,
            targetAccountDto.getAccountNo(),
            group.getGroupId(),
            requestDto.getTargetCurrencyCode()
        );

        // 알림 전송
        pushNotification(notificationRequestDto);

        return ResponseEntity.ok().body(new ResponseDto());
    }

    /*
     *  환전 알림 전송
     */
    public ResponseEntity<?> sendExchangeNotification(
        User user,
        MoneyBoxTransferRequestDto requestDto
    ) {

        String title = "환전 완료";
        String message = requestDto.getTransactionBalance() + requestDto.getSourceCurrencyCode() + " 이 성공적으로 환전되었습니다.";

        TravelGroup group = groupRepository.findByGroupAccountNo(requestDto.getAccountNo());

        PushNotificationRequestDto notificationRequestDto = PushNotificationRequestDto.createDto(
            user.getUserId(),
            NotificationType.E,
            title,
            message,
            requestDto.getAccountNo(),
            group.getGroupId(),
            requestDto.getTargetCurrencyCode()
        );

        // 알림 전송
        pushNotification(notificationRequestDto);

        return ResponseEntity.ok().body(new ResponseDto());
    }


    public List<NotificationDto> getAllByUserId() {

        User user = securityUtil.getUserByToken();

        List<Notification> notificationList = notificationRepository.findAllByUser_userId(user.getUserId());

        return notificationMapper.toDtoList(notificationList);
    }

    public ResponseDto readAllNotifications() {

        User user = securityUtil.getUserByToken();

        notificationRepository.readAllNotifications(user.getUserId());

        return new ResponseDto();
    }

    public ResponseDto deleteNotification(Long notificationId) {
        User user = securityUtil.getUserByToken();

        notificationRepository.findById(notificationId)
            .orElseThrow(() -> new NotificationNotFoundException(notificationId));

        notificationRepository.deleteById(notificationId);

        return new ResponseDto();

    }

}


