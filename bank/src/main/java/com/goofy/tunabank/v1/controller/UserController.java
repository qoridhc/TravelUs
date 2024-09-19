package com.goofy.tunabank.v1.controller;

import com.goofy.tunabank.v1.dto.user.UserJoinRequestDto;
import com.goofy.tunabank.v1.dto.user.UserJoinResponseDto;
import com.goofy.tunabank.v1.dto.user.UserSearchRequestDto;
import com.goofy.tunabank.v1.dto.user.UserSearchResponseDto;
import com.goofy.tunabank.v1.service.UserService;
import com.goofy.tunabank.v1.util.LogUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/join")
  public ResponseEntity joinNewUesr(@RequestBody UserJoinRequestDto request) {
    LogUtil.info("회원 가입 요청", request.getUserId());
    UserJoinResponseDto response = userService.createUser(request);
    return new ResponseEntity(response, HttpStatus.CREATED);
  }

  @PostMapping("/search")
  public ResponseEntity searchUser(@RequestBody UserSearchRequestDto request){
    LogUtil.info("유저 조회 요청", request.getUserId());
    UserSearchResponseDto response = userService.searchUser(request);
    return new ResponseEntity(response, HttpStatus.CREATED);
  }

}
