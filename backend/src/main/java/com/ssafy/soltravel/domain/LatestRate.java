package com.ssafy.soltravel.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.Data;

@Data
@Entity
public class LatestRate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "latest_rate_id")
  private Long id;

  @Column(name = "post_at")
  private LocalDate postAt;

  @Column(name = "deal_bas_r")
  private Double dealBasR;         // 매매 기준율

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  @JoinColumn(name = "currency_id")
  private Currency currency;            // 국가/통화명

  @Column(name = "ttb")
  private Double ttb;              // 전신환(송금) 받으실때 (TTB)

  @Column(name = "tts")
  private Double tts;              // 전신환(송금) 보내실때 (TTS)

  @Column(name = "cash_buying")
  private Double cashBuying;  //현찰 매입시(파실때)

  @Column(name = "cash_selling")
  private Double cashSelling;//현찰 매도시(사실때)

  @Column(name = "tc_buying")
  private Double tcBuying;//여행자 수표 매입시(사실때)
}
