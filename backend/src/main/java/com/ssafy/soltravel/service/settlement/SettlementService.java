package com.ssafy.soltravel.service.settlement;

import com.ssafy.soltravel.dto.settlement.SettlementRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SettlementService {

  public SettlementResponseDto executeSettlement(SettlementRequestDto requestDto) {

    SettlementResponseDto responseDto = new SettlementResponseDto();
    return responseDto;
  }
}
