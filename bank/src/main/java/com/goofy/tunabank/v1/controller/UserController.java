package com.goofy.tunabank.v1.controller;

import com.goofy.tunabank.v1.dto.user.UserJoinRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {


  @PostMapping("/join")
  public ResponseEntity joinNewUesr(@RequestBody UserJoinRequestDto request) {


    return null;
  }

}
