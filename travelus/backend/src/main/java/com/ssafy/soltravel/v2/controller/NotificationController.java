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
