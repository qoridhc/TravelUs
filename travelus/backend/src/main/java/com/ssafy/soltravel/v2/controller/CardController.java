package com.ssafy.soltravel.v2.controller;

import com.ssafy.soltravel.v2.dto.card.CardIssueRequestDto;
import com.ssafy.soltravel.v2.dto.card.CardResponseDto;
import com.ssafy.soltravel.v2.service.card.CardService;
import com.ssafy.soltravel.v2.util.LogUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/card")
@RequiredArgsConstructor
public class CardController {


  private final CardService cardService;

  @PostMapping("/issue")
  public ResponseEntity issueNewCard(@RequestBody CardIssueRequestDto request){
    LogUtil.info("카드 발급 요청", request);
    CardResponseDto response = cardService.createNewCard(request);
    return new ResponseEntity(response, HttpStatus.CREATED);
  }




}
