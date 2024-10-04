package com.ssafy.soltravel.v2.mapper;

import com.ssafy.soltravel.v2.domain.PersonalSettlementHistory;
import com.ssafy.soltravel.v2.dto.settlement.response.PersonalSettlementDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SettlementMapper {

  @Mapping(source = "participant.group.groupId", target = "groupId")
  @Mapping(source="participant.group.groupName", target = "groupName")
  @Mapping(source = "id", target = "personalSettlementId")
  @Mapping(source="createdAt", target = "settlementRequestTime")
  PersonalSettlementDto toPersonalSettlementNotificationListDto(PersonalSettlementHistory personalSettlementHistory);

  List<PersonalSettlementDto> toPersonalSettlementNotificationListDto(List<PersonalSettlementHistory> personalSettlementHistoryList);
}
