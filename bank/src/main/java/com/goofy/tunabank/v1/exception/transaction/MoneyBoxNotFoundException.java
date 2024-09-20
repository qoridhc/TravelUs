package com.goofy.tunabank.v1.exception.transaction;

import com.goofy.tunabank.v1.exception.CustomException;

public class MoneyBoxNotFoundException extends CustomException {

  private static final String DEFAULT_MESSAGE = "MoneyBox not found";
  private static final String DEFAULT_CODE = "MONEY_BOX_NOT_FOUND";
  private static final int DEFAULT_STATUS = 404;

  public MoneyBoxNotFoundException(long accountId, int currencyId) {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s. accountID: %d, currencyID: %d", DEFAULT_MESSAGE, accountId, currencyId)
    );
  }
}

