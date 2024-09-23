package com.ssafy.soltravel.v2.service.card;


import com.ssafy.soltravel.v2.dto.card.CardIssueRequestDto;
import com.ssafy.soltravel.v2.dto.card.CardIssueResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CardService {


  public CardIssueResponseDto createNewCard(CardIssueRequestDto request) {

    
    return null;
  }
}
