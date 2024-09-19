package com.goofy.tunabank.domain;

import com.goofy.tunabank.domain.Enum.CurrencyType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Currency {

    @Id
    @Column(name = "currency_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency_code")
    private CurrencyType currencyCode;  // KOR, JPY, USD 등

    private double exchangeRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    Country country;

    // ===== 생성자 매서드 =====

}
