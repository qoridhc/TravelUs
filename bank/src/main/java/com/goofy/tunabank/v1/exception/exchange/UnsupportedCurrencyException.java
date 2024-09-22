package com.goofy.tunabank.v1.exception.exchange;

import com.goofy.tunabank.v1.exception.CustomException;

public class UnsupportedCurrencyException extends CustomException {

  private static final String DEFAULT_MESSAGE = "Unsupported currency type";
  private static final String DEFAULT_CODE = "UNSUPPORTED_CURRENCY";
  private static final int DEFAULT_STATUS = 400;

  public UnsupportedCurrencyException(int currency) {
    super(
        DEFAULT_MESSAGE,
        DEFAULT_CODE,
        DEFAULT_STATUS,
        String.format("%s: %s", DEFAULT_MESSAGE, currency)
    );
  }
}
