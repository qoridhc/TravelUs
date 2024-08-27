package com.ssafy.soltravel.controller;

import com.ssafy.soltravel.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
@Tag(name = "Notification API", description = "알림 관련 API")
public class NotificationController {

  private final NotificationService notificationService;

  /**
   * 메시지 알림 구독
   */
  @GetMapping("/subscribe")
  @Operation(summary = "메시지 알림 구독", description = "특정 사용자의 메시지 알림을 구독합니다. SSE를 통해 실시간 알림을 수신합니다.", responses = {
      @ApiResponse(responseCode = "200", description = "성공적으로 알림을 구독했습니다.", content = @Content(schema = @Schema(implementation = SseEmitter.class))),
      @ApiResponse(responseCode = "404", description = "해당 사용자를 찾을 수 없습니다.", content = @Content),
      @ApiResponse(responseCode = "500", description = "서버 오류입니다.", content = @Content)})
  public SseEmitter subscribe() {

    return notificationService.subscribe();
  }
}
