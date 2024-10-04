package com.ssafy.soltravel.v2.dto.notification;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class NotificationDto {

    private Long id;

    private Long userId;

    private String title;

    private String message;

    private boolean isRead;

    private LocalDateTime createdAt;

}
