package com.ssafy.soltravel.v2.mapper;

import com.ssafy.soltravel.v2.domain.Notification;
import com.ssafy.soltravel.v2.dto.notification.NotificationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ParticipantMapper.class)
public interface NotificationMapper {

    @Mapping(source = "user.userId", target = "userId")
    NotificationDto toDto(Notification notification);

}
