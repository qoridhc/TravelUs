package com.goofy.tunabank.v1.dto.transaction.request;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Data;

@Data
public class TransactionHeader {

  private String transmissionDate;
  private String transmissionTime;

  public LocalDateTime getTransmissionDateTime() {
    String dateTimeString = transmissionDate + transmissionTime;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    return LocalDateTime.parse(dateTimeString, formatter);
  }
}
