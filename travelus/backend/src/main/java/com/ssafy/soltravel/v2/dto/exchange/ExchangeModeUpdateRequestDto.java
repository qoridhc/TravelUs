package com.ssafy.soltravel.v2.dto.exchange;

import com.ssafy.soltravel.v2.domain.Enum.ExchangeType;
import lombok.Data;

@Data
public class ExchangeModeUpdateRequestDto {

  long groupId;

  ExchangeType exchangeType;
}
