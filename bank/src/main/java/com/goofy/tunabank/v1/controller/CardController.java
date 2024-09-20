package com.goofy.tunabank.v1.controller;

import com.goofy.tunabank.v1.service.CardService;
import com.goofy.tunabank.v1.util.LogUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/card")
@RequiredArgsConstructor
public class CardController {

  private final CardService cardService;

  public ResponseEntity issueNewCard(){
    LogUtil.info("카드 발급 요청", "으앵");
    cardService.createNewCard();
    return new ResponseEntity("발급 성공", HttpStatus.CREATED);
  }

}
