package com.ssafy.soltravel.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "exchange_rate")
public class ExchangeRate {

    @Id
    private String currency;

    @Column(name = "exchange_rate")
    private String exchangeRate;

    @Column(name = "exchange_min")
    private String exchangeMin;

    private String created;
}
