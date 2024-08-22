package com.ssafy.soltravel.controller;


import com.ssafy.soltravel.dto.user.UserCreateRequestDto;
import com.ssafy.soltravel.dto.user.UserSearchRequestDto;
import com.ssafy.soltravel.dto.user.UserSearchResponseDto;
import com.ssafy.soltravel.service.user.UserService;
import com.ssafy.soltravel.util.LogUtil;
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
public class UserController {

  private final UserService userService;

  /*
   * 회원가입
   */
  @PostMapping("/join")
  public ResponseEntity<?> createUser(@RequestBody UserCreateRequestDto joinDto) {
    LogUtil.info("requested", joinDto.toString());
    userService.createUser(joinDto);
    return new ResponseEntity<String>("회원 가입 성공", HttpStatus.CREATED);
  }

  /*
   * 단건 조회
   */
  @GetMapping("/search/{userId}")
  public ResponseEntity<?> searchUser(@PathVariable(name = "userId") Long userId) {
    LogUtil.info("requested", userId);
    UserSearchResponseDto response = userService.searchOneUser(userId);
    return ResponseEntity.ok().body(response);
  }


  /*
   * 전체 조회
   */
  @GetMapping("/search/all")
  public ResponseEntity<?> searchAllUser(@ModelAttribute UserSearchRequestDto searchDto) {
    LogUtil.info("requested", searchDto.toString());
    List<UserSearchResponseDto> response = userService.searchAllUser(searchDto);
    return ResponseEntity.ok().body(response);
  }
}
