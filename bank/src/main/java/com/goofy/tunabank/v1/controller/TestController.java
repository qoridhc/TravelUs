package com.goofy.tunabank.v1.controller;


import com.goofy.tunabank.v1.dto.ApiRequestBody;
import com.goofy.tunabank.v1.util.LogUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

  @PostMapping("/1")
  public ResponseEntity<?> test(@RequestBody ApiRequestBody request) {
    LogUtil.info("정상 요청", request);
    return ResponseEntity.ok().build();
  }
}
