package com.ssafy.soltravel.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity(name = "exchange_rate")
public class ExchangeRate {

    @Id
    private String currency;

    @Column(name = "exchange_rate")
    private Float exchangeRate;

    @Column(name = "exchange_min")
    private Long exchangeMin;

    @Column
    private LocalDateTime created;
}
