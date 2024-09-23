package com.ssafy.soltravel.v2.controller;

import com.ssafy.soltravel.v2.dto.group.GroupDto;
import com.ssafy.soltravel.v2.dto.group.ParticipantDto;
import com.ssafy.soltravel.v2.dto.group.request.CreateGroupRequestDto;
import com.ssafy.soltravel.v2.dto.group.request.CreateParticipantRequestDto;
import com.ssafy.soltravel.v2.service.group.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Group API", description = "모임 & 참여자 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    // === 모임 관련 메서드 ===
    @Operation(summary = "새로운 모임 생성", description = "새로운 모임을 생성하는 API.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "모임 생성 성공", content = @Content(schema = @Schema(implementation = GroupDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PostMapping("/createGroup")
    public ResponseEntity<GroupDto> createNewGroup(
        @RequestBody CreateGroupRequestDto requestDto
    ) {

        GroupDto accountDto = groupService.createNewGroup(requestDto);

        return  ResponseEntity.status(HttpStatus.CREATED).body(accountDto);
    }

    // === 참여자 관련 메서드 ===
    @Operation(summary = "새로운 참여자 생성", description = "새로운 참여자를 모임에 추가하는 API.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "참여자 생성 성공", content = @Content(schema = @Schema(implementation = ParticipantDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PostMapping("/createParticipant")
    public ResponseEntity<ParticipantDto> createNewParticipant(
        @RequestBody CreateParticipantRequestDto requestDto
    ) {

        ParticipantDto participantDto = groupService.createNewParticipant(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(participantDto);
    }

}
