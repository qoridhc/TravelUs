package com.ssafy.soltravel.v2.mapper;

import com.ssafy.soltravel.v2.domain.Participant;
import com.ssafy.soltravel.v2.domain.TravelGroup;
import com.ssafy.soltravel.v2.dto.group.GroupDto;
import com.ssafy.soltravel.v2.dto.group.ParticipantDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    GroupDto toDto(TravelGroup travelGroup);

    TravelGroup toEntity(GroupDto dto);

    @Mapping(source = "id", target = "participantId")
    ParticipantDto toParticipantDto(Participant participant);


}