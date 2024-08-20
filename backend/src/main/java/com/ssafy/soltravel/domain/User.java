package com.ssafy.soltravel.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;


@Getter
@Entity
@Table(name = "`user`")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  @Column
  private String name;

  @Column
  private String password;

  @Column
  private String email;

  @Column
  private String phone;

  @Column
  private String address;

  @Column(name = "register_at")
  private LocalDateTime registerAt;

  @Column(name = "is_exit")
  private Boolean isExit;

  @Enumerated(EnumType.STRING)
  private Role role;


  /*
   * 생성 메서드
   */
  public static User createUser(String name, String password, String email, String phone,
      String address) {
    User user = new User();
    user.name = name;
    user.password = password;
    user.email = email;
    user.phone = phone;
    user.address = address;
    user.registerAt = LocalDateTime.now();
    user.role = Role.USER;
    return user;
  }
}
