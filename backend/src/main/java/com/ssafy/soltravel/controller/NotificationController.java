package com.ssafy.soltravel.controller;

import com.ssafy.soltravel.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  // 메시지 알림
  @GetMapping("/subscribe")
  public SseEmitter subscribe() {

    SseEmitter sseEmitter = notificationService.subscribe(2L);
    return sseEmitter;
  }
}
