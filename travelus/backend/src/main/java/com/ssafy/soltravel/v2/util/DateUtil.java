package com.ssafy.soltravel.v2.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

  private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  public static LocalDateTime getLocalDateTime(String dateString) {
    LocalDate date = LocalDate.parse(dateString, formatter);
    return date.atStartOfDay();
  }

  public static LocalDateTime getNextLocalDateTime(String dateString) {
    LocalDate date = LocalDate.parse(dateString, formatter);
    return date.plusDays(1).atStartOfDay();
  }

  public static String getNextLocalDateStr(String dateString) {
    LocalDate date = LocalDate.parse(dateString, formatter);
    return date.plusDays(1).format(formatter);
  }

  public static LocalDateTime parseTransactionAt(String dateString) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddTHH:mm");
    if(dateString.length() == 10) {
      dateString.concat("T00:00");
    }
    return LocalDateTime.parse(dateString, formatter);
  }

}
