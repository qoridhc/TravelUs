package com.goofy.tunabank.v1.service;

import com.goofy.tunabank.v1.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CardService {

  private final UserService userService;

  public void createNewCard() {

    String userKey = "asd";
    
    // 유저 검증
    User user = userService.findUserByUserKey(userKey);

  }
}
