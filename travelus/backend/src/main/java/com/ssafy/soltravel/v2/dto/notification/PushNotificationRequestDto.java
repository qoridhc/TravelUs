package com.ssafy.soltravel.v2.dto.notification;

import com.ssafy.soltravel.v2.domain.Enum.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor  // 기본 생성자 추가
@AllArgsConstructor
public class PushNotificationRequestDto {

    @Schema(description = "알림을 받을 대상 사용자 ID", example = "1")
    private Long targetUserId;

    @Schema(description = "알림 발송 타입", example = "PT")
    private NotificationType notificationType;

    @Schema(description = "알림 제목", example = "새로운 메시지가 도착했습니다.")
    private String title;

    @Schema(description = "알림 메시지 내용", example = "안녕하세요! 푸시 알림을 확인하세요.")
    private String message;

    @Schema(description = "알림에 표시될 아이콘 URL", example = "/icons/favicon.ico")
    private String icon;

    @Schema(description = "그룹 ID", example = "12345")
    private Long groupId;

    @Schema(description = "계좌 번호", example = "123-456-7890")
    private String accountNo;

    public PushNotificationRequestDto(Long targetUserId, NotificationType notificationType, String title,
        String message, String icon, String accountNo, Long groupId) {
        this.targetUserId = targetUserId;
        this.notificationType = notificationType;
        this.title = title;
        this.message = message;
        this.icon = icon;
        this.accountNo = accountNo;
        this.groupId = groupId;
    }
}