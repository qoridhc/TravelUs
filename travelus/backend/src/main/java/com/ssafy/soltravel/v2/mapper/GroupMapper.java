package com.ssafy.soltravel.v2.mapper;

import com.ssafy.soltravel.v2.domain.Participant;
import com.ssafy.soltravel.v2.domain.TravelGroup;
import com.ssafy.soltravel.v2.dto.group.GroupDto;
import com.ssafy.soltravel.v2.dto.group.ParticipantDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ParticipantMapper.class)
public interface GroupMapper {

    @Mapping(source = "participants", target = "participants")
    GroupDto toDto(TravelGroup travelGroup);

    TravelGroup toEntity(GroupDto dto);

    @Mapping(source = "id", target = "participantId")
    @Mapping(source = "user.userId", target = "userId")
    ParticipantDto toParticipantDto(Participant participant);

    List<ParticipantDto> toParticipantDtoList(List<Participant> participants);

}