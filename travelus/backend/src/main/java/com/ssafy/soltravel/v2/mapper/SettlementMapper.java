package com.ssafy.soltravel.v2.mapper;

import com.ssafy.soltravel.v2.domain.PersonalSettlementHistory;
import com.ssafy.soltravel.v2.dto.settlement.response.GroupSettlementResponseDto;
import com.ssafy.soltravel.v2.dto.settlement.response.PersonalSettlementDto;
import com.ssafy.soltravel.v2.dto.settlement.response.PersonalSettlementHistoryDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SettlementMapper {

  @Mapping(source = "participant.group.groupId", target = "groupId")
  @Mapping(source = "participant.group.groupName", target = "groupName")
  @Mapping(source = "id", target = "personalSettlementId")
  @Mapping(source = "participant.id", target = "participantId")
  @Mapping(source = "createdAt", target = "settlementRequestTime")
  @Mapping(target = "participantCount", expression = "java(personalSettlementHistory.getParticipant().getGroup().getParticipants().size())")
  PersonalSettlementDto toPersonalSettlementNotificationListDto(PersonalSettlementHistory personalSettlementHistory);

  List<PersonalSettlementDto> toPersonalSettlementNotificationListDto(
      List<PersonalSettlementHistory> personalSettlementHistoryList);

  @Mapping(source = "id", target = "personalSettlementId")
  @Mapping(source = "createdAt", target = "settlementRequestTime")
  GroupSettlementResponseDto toGroupSettlementResponseDto(PersonalSettlementHistory personalSettlementHistory);

  @Mapping(source = "participant.id", target = "participantId")
  @Mapping(source = "id", target = "personalSettlementId")
  @Mapping(source = "createdAt", target = "settlementRequestTime")
  PersonalSettlementHistoryDto toPersonalSettlementHistoryDto(PersonalSettlementHistory personalSettlementHistory);

  List<PersonalSettlementHistoryDto> toPersonalSettlementHistoryDtoList(
      List<PersonalSettlementHistory> personalSettlementHistories);
}
