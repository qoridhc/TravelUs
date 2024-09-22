package com.ssafy.soltravel.v2.mapper;

import com.ssafy.soltravel.v2.domain.TravelGroup;
import com.ssafy.soltravel.v2.dto.group.GroupDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    GroupDto toDto(TravelGroup travelGroup);

    TravelGroup toEntity(GroupDto dto);
}