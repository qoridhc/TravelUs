package com.ssafy.soltravel.v2.dto.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Schema(description = "알림 정보를 담고 있는 DTO")
public class NotificationDto {

    @Schema(description = "알림 ID", example = "1")
    private Long notificationId;

    @Schema(description = "유저 ID", example = "123")
    private Long userId;

    @Schema(description = "알림 제목", example = "새로운 메시지가 도착했습니다.")
    private String title;

    @Schema(description = "알림 내용", example = "당신의 계좌로 새로운 입금이 발생했습니다.")
    private String message;

    @Schema(description = "읽음 여부", example = "false")
    private boolean isRead;

    @Schema(description = "알림 생성 시간", example = "2023-10-04T13:00:00")
    private LocalDateTime createdAt;
}