package com.ssafy.soltravel.v2.dto.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PushNotificationRequestDto {

    @Schema(description = "알림을 받을 대상 사용자 ID", example = "1")
    private Long targetUserId;

    @Schema(description = "알림 제목", example = "새로운 메시지가 도착했습니다.")
    private String title;

    @Schema(description = "알림 메시지 내용", example = "안녕하세요! 푸시 알림을 확인하세요.")
    private String message;

    @Schema(description = "알림에 표시될 아이콘 URL", example = "/icons/favicon.ico")
    private String icon;
}
