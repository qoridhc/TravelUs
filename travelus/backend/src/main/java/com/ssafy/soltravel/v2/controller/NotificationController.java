package com.ssafy.soltravel.v2.controller;

import com.ssafy.soltravel.v2.dto.ResponseDto;
import com.ssafy.soltravel.v2.dto.notification.PushNotificationRequestDto;
import com.ssafy.soltravel.v2.dto.notification.RegisterNotificationRequestDto;
import com.ssafy.soltravel.v2.service.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
@Tag(name = "Notification API", description = "알림 관련 API")
public class NotificationController {

    private final NotificationService notificationService;

//    /**
//     * 메시지 알림 구독
//     */
//    @GetMapping(value = "/subscribe/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    @Operation(summary = "메시지 알림 구독", description = "특정 사용자의 메시지 알림을 구독합니다. SSE를 통해 실시간 알림을 수신합니다.", responses = {
//        @ApiResponse(responseCode = "200", description = "성공적으로 알림을 구독했습니다.", content = @Content(schema = @Schema(implementation = SseEmitter.class))),
//        @ApiResponse(responseCode = "404", description = "해당 사용자를 찾을 수 없습니다.", content = @Content),
//        @ApiResponse(responseCode = "500", description = "서버 오류입니다.", content = @Content)})
//    public SseEmitter subscribe(@Parameter(description = "사용자의 userId", example = "1") @PathVariable Long userId) {
//
//        return notificationService.subscribe(userId);
//    }
//
//    @GetMapping(value = "/sendAll", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    @Operation(summary = "전체 사용자에게 메세지 전송", description = "테스트용 메서드", responses = {
//        @ApiResponse(responseCode = "200", description = "전송 성공", content = @Content(schema = @Schema(implementation = SseEmitter.class))),
//        @ApiResponse(responseCode = "404", description = "해당 사용자를 찾을 수 없습니다.", content = @Content),
//        @ApiResponse(responseCode = "500", description = "서버 오류입니다.", content = @Content)})
//    public ResponseEntity<?> subscribe() {
//        notificationService.notifyAllUser();
//        return ResponseEntity.ok()
//            .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_EVENT_STREAM_VALUE).build();
//    }

    /* FCM Token 서버 저장 API */
    @PostMapping("/register")
    public ResponseEntity<ResponseDto> setToken(@RequestBody RegisterNotificationRequestDto requestDto) {
        ResponseDto responseDto = notificationService.saveFcmToken(requestDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/push")
    public ResponseEntity<?> pushFcmNotification(@RequestBody PushNotificationRequestDto requestDto) {
        ResponseEntity<?> response = notificationService.pushNotification(requestDto);

        return ResponseEntity.ok().body(response);
    }
}
