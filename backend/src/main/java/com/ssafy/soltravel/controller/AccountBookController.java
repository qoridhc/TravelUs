package com.ssafy.soltravel.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/account-book")
@RequiredArgsConstructor
public class AccountBookController {



  @PostMapping("/upload/receipt")
  public ResponseEntity<?> uploadReceipt(@RequestParam("file") MultipartFile file) {
    return null;
  }

}
