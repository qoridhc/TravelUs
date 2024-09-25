package com.ssafy.soltravel.v2.controller;

import com.ssafy.soltravel.v2.dto.group.GroupDto;
import com.ssafy.soltravel.v2.dto.group.ParticipantDto;
import com.ssafy.soltravel.v2.dto.group.request.CreateGroupRequestDto;
import com.ssafy.soltravel.v2.dto.group.request.CreateParticipantRequestDto;
import com.ssafy.soltravel.v2.dto.group.response.GroupSummaryDto;
import com.ssafy.soltravel.v2.service.group.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

        return ResponseEntity.status(HttpStatus.CREATED).body(accountDto);
    }

    // 모임 조회
    @Operation(summary = "모임 정보 조회", description = "특정 모임의 정보를 조회하는 API.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "모임 정보 조회 성공", content = @Content(schema = @Schema(implementation = GroupDto.class))),
        @ApiResponse(responseCode = "404", description = "모임을 찾을 수 없음", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/{groupId}")
    public ResponseEntity<GroupDto> getGroupInfo(
        @PathVariable Long groupId
    ) {
        GroupDto accountDto = groupService.getGroupInfo(groupId);

        return ResponseEntity.status(HttpStatus.OK).body(accountDto);
    }

    @Operation(summary = "가입한 모든 모임 조회 (생성한거는 조회 X)", description = "사용자가 가입한 모든 모임의 요약 정보를 반환하는 API.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "모임 요약 정보 조회 성공",
            content = @Content(schema = @Schema(implementation = GroupSummaryDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/joined")
    public ResponseEntity<List<GroupSummaryDto>> getAllJoinedGroup() {
        List<GroupSummaryDto> groupSummaryDtoList = groupService.getAllJoinedGroup(false);

        return ResponseEntity.status(HttpStatus.OK).body(groupSummaryDtoList);
    }

    @Operation(summary = "생성한 모든 모임 조회 (가입 한거는 X 생성만)", description = "사용자가 생성한 모든 모임의 요약 정보를 반환하는 API.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "모임 요약 정보 조회 성공",
            content = @Content(schema = @Schema(implementation = GroupSummaryDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/created")
    public ResponseEntity<List<GroupSummaryDto>> getAllCreatedGroup() {
        List<GroupSummaryDto> groupSummaryDtoList = groupService.getAllJoinedGroup(true);

        return ResponseEntity.status(HttpStatus.OK).body(groupSummaryDtoList);
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

//    @GetMapping()
//    public ResponseEntity<ParticipantDto> getAllGroupsByUserId() {
//
//        groupService.getAllGroupInfosByUserId();
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(participantDto);
//    }
}
