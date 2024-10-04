package com.ssafy.soltravel.v2.exception.settlement;

import com.ssafy.soltravel.v2.exception.CustomException;

public class PersonalSettlementHistoryNotFoundException extends CustomException {

  private static final String DEFAULT_MESSAGE = "Personal settlement history not found for settlement ID.";
  private static final String DEFAULT_CODE = "PERSONAL_SETTLEMENT_HISTORY_NOT_FOUND";
  private static final int DEFAULT_STATUS = 404;

  public PersonalSettlementHistoryNotFoundException(Long id) {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s: %d", DEFAULT_MESSAGE, id)
    );
  }
}