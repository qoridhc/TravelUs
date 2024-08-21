package com.ssafy.soltravel.controller;


import com.ssafy.soltravel.dto.user.UserCreateRequestDto;
import com.ssafy.soltravel.dto.user.UserSearchRequestDto;
import com.ssafy.soltravel.service.user.UserService;
import com.ssafy.soltravel.util.LogUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  @PostMapping("/join")
  public ResponseEntity<?> createUser(@RequestBody UserCreateRequestDto joinDto) {
    LogUtil.info("requested", joinDto.toString());
    userService.createUser(joinDto);
    return new ResponseEntity<String>("회원 가입 성공", HttpStatus.CREATED);
  }

  @PostMapping("/search")
  public ResponseEntity<?> searchUser(@RequestBody UserSearchRequestDto searchDto) {
    LogUtil.info("requested", searchDto.toString());
    userService.searchUser(searchDto);
    return new ResponseEntity<String>("유저 조회", HttpStatus.OK);
  }
}
