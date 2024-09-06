package com.goofy.tunabank.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ForeignAccount extends Account {

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

}
