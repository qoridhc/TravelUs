package com.goofy.tunabank.v1.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Bank {

  //은행 아이디
  @Id
  @Column(name = "bank_id")
  private Long id;

  //은행 이름
  @Column(name = "bank_name")
  private String bankName;

  @OneToMany(fetch = FetchType.LAZY)
  private List<Account> accounts;
}
