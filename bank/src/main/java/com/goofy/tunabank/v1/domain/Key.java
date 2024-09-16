package com.goofy.tunabank.v1.domain;

import com.goofy.tunabank.v1.domain.Enum.KeyStatus;
import com.goofy.tunabank.v1.domain.Enum.KeyType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;

@Entity
@Getter
@Table(name = "api_key")
public class Key {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column
  @Enumerated(EnumType.STRING)
  private KeyType type;

  @Column
  private String value;

  @Column
  @Enumerated(EnumType.STRING)
  private KeyStatus status;

  @Column(name = "create_at")
  private LocalDateTime createAt;

  @Column(name = "expire_at")
  private LocalDateTime expireAt;
}
