package com.ssafy.soltravel.controller;

import com.ssafy.soltravel.dto.user.UserLoginRequestDto;
import com.ssafy.soltravel.dto.user.UserLoginResponseDto;
import com.ssafy.soltravel.service.user.AuthService;
import com.ssafy.soltravel.util.LogUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody UserLoginRequestDto loginRequestDto) {
    LogUtil.info(loginRequestDto.toString());
    UserLoginResponseDto response = authService.login(loginRequestDto);
    return ResponseEntity.ok().body(response);
  }
  
  @GetMapping("/test")
  public String test() {
    return "test";
  }

  @GetMapping("/test-ok")
  public String testOk() {
    return "testOk";
  }
}
