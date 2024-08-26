package com.ssafy.soltravel.controller;

import com.ssafy.soltravel.dto.ResponseDto;
import com.ssafy.soltravel.dto.user.UserCreateRequestDto;
import com.ssafy.soltravel.dto.user.UserSearchRequestDto;
import com.ssafy.soltravel.dto.user.UserSearchResponseDto;
import com.ssafy.soltravel.service.user.UserService;
import com.ssafy.soltravel.util.LogUtil;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "User Management", description = "사용자 관리 API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "회원 가입 성공", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PostMapping("/join")
    public ResponseEntity<ResponseDto> createUser(
        @RequestBody UserCreateRequestDto joinDto) {

        LogUtil.info("requested", joinDto.toString());
        userService.createUser(joinDto);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto());
    }


    @Operation(summary = "단일 사용자 조회", description = "사용자 ID로 단일 사용자 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = UserSearchResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/search/{userId}")
    public ResponseEntity<?> searchUser(@PathVariable(name = "userId") Long userId) {

        LogUtil.info("requested", userId);
        UserSearchResponseDto response = userService.searchOneUser(userId);
        return ResponseEntity.ok().body(response);
    }


    @Operation(summary = "모든 사용자 조회", description = "모든 사용자를 조건에 맞춰 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = UserSearchResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/search/all")
    public ResponseEntity<?> searchAllUser(@ModelAttribute UserSearchRequestDto searchDto) {

        LogUtil.info("requested", searchDto.toString());
        List<UserSearchResponseDto> response = userService.searchAllUser(searchDto);
        return ResponseEntity.ok().body(response);
    }
}
