package com.goofy.tunabank.v1.domain;

import com.goofy.tunabank.v1.domain.Enum.KeyStatus;
import com.goofy.tunabank.v1.domain.Enum.KeyType;
import com.goofy.tunabank.v1.domain.Enum.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;

@Entity
@Getter
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @Column(nullable = false, name = "email")
  private String email;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  List<Key> keys = new ArrayList<>();

  @Column(name = "is_exit")
  private Boolean isExit;

  @Column(name = "role")
  @Enumerated(EnumType.STRING)
  private Role role;

  @Column(name = "create_at")
  private LocalDateTime createdAt;

  @Column(name = "update_at")
  private LocalDateTime updateAt;

  @Column(name = "exit_at")
  private LocalDateTime exitAt;


  /*
  * 생성 메서드
  */
  public static User createUser(String email, Role role){
    User user = new User();
    user.email = email;
    user.role = role;
    user.isExit = false;
    user.createdAt = LocalDateTime.now();
    user.updateAt = LocalDateTime.now();
    user.exitAt = null;
    return user;
  }

  /*
  * 유저 탈퇴 처리
  */
  public void deactiveUser(){
    this.isExit = true;
    this.exitAt = LocalDateTime.now();

    keys.stream().forEach(key -> key.dedactiveKey());
  }
}
