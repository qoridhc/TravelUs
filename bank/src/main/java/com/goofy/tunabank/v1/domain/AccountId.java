package com.goofy.tunabank.v1.domain;

import com.goofy.tunabank.v1.domain.Enum.AccountType;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountId implements Serializable {

    private Long id;  // accountId 필드

    private AccountType accountType;  // accountType 필드

    // 기본 생성자, equals, hashCode, Getters and Setters
    public AccountId() {}

    public AccountId(Long accountId, AccountType accountType) {
        this.id = accountId;
        this.accountType = accountType;
    }

    public void setAccountId(Long accountId) {
        this.id = accountId;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountId accountId1 = (AccountId) o;
        return Objects.equals(id, accountId1.id) && accountType == accountId1.accountType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountType);
    }
}
