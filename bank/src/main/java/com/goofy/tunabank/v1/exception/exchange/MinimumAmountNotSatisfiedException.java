package com.goofy.tunabank.v1.exception.exchange;

import com.goofy.tunabank.v1.exception.CustomException;

public class MinimumAmountNotSatisfiedException extends CustomException {

  private static final String DEFAULT_MESSAGE = "amount is below the minimum allowable amount";
  private static final String DEFAULT_CODE = "MINIMUM_AMOUNT_NOT_SATISFIED";
  private static final int DEFAULT_STATUS = 400;

  public MinimumAmountNotSatisfiedException(int currencyId, double amount) {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s. Currency ID: %s, Amount: %.2f", DEFAULT_MESSAGE, currencyId, amount)
    );
  }
}
