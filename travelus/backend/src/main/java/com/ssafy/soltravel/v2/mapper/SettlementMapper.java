package com.ssafy.soltravel.v2.mapper;

import com.ssafy.soltravel.v2.domain.PersonalSettlementHistory;
import com.ssafy.soltravel.v2.dto.settlement.response.PersonalSettlementHistoryDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SettlementMapper {

  @Mapping(source = "id", target = "personalSettlementId")
  @Mapping(source="createdAt", target = "settlementRequestTime")
  PersonalSettlementHistoryDto toPersonalSettlementHistoryDto(PersonalSettlementHistory personalSettlementHistory);

  List<PersonalSettlementHistoryDto> toPersonalSettlementHistoryDtos(List<PersonalSettlementHistory> personalSettlementHistoryList);
}
