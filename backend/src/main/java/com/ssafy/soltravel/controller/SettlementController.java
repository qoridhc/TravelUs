package com.ssafy.soltravel.controller;

import com.ssafy.soltravel.dto.settlement.SettlementRequestDto;
import com.ssafy.soltravel.service.settlement.SettlementResponseDto;
import com.ssafy.soltravel.service.settlement.SettlementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/settlement")
@Slf4j
@Tag(name = "Settlement API", description = "정산 관련 API")
public class SettlementController {

  private final SettlementService settlementService;

  @PostMapping
  public ResponseEntity<SettlementResponseDto> executeSettlement(
      @RequestBody SettlementRequestDto settlementRequestDto) {

    return ResponseEntity.ok().body(settlementService.executeSettlement(settlementRequestDto));
  }
}
