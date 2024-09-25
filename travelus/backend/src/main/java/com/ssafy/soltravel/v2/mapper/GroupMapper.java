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

    /*
     *  그룹 매핑 메서드
     */
    @Mapping(source = "participants", target = "participants")
    GroupDto toDto(TravelGroup travelGroup);

    List<GroupDto> toDtoList(List<TravelGroup> groups);

    TravelGroup toEntity(GroupDto dto);



    /*
     *  참여자 매핑 메서드
     */

    @Mapping(source = "id", target = "participantId")
    @Mapping(source = "user.userId", target = "userId")
    ParticipantDto toParticipantDto(Participant participant);

    List<ParticipantDto> toParticipantDtoList(List<Participant> participants);

}