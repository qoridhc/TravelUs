package com.ssafy.soltravel.v2.controller;

import com.ssafy.soltravel.v2.dto.account.AccountDto;
import com.ssafy.soltravel.v2.dto.account.request.InquireAccountRequestDto;
import com.ssafy.soltravel.v2.dto.group.GroupDto;
import com.ssafy.soltravel.v2.dto.group.ParticipantDto;
import com.ssafy.soltravel.v2.dto.group.request.CreateGroupRequestDto;
import com.ssafy.soltravel.v2.dto.group.request.CreateParticipantRequestDto;
import com.ssafy.soltravel.v2.service.group.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    // === 모임 관련 메서드 ===
    @PostMapping("/createGroup")
    public ResponseEntity<GroupDto> createNewGroup(
        @RequestBody CreateGroupRequestDto requestDto
    ) {

        GroupDto accountDto = groupService.createNewGroup(requestDto);

        return  ResponseEntity.status(HttpStatus.CREATED).body(accountDto);
    }

    // === 참여자 관련 메서드 ===
    @PostMapping("/createParticipant")
    public ResponseEntity<ParticipantDto> createNewParticipant(
        @RequestBody CreateParticipantRequestDto requestDto
    ) {

        ParticipantDto participantDto = groupService.createNewParticipant(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(participantDto);
    }

}
