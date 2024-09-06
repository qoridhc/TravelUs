package com.goofy.tunabank.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Country {

    @Id
    @Column(name = "country_id")
    private int countryId;

    private String countryCode;

    private String countryName;

    @OneToMany(mappedBy = "country")
    private List<Currency> currencyList;

}
