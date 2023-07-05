package com.nhn.ep.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity //엔티티임을 선언  <-- DB 테이블
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increment PK 순번
    private Long id;

    @Column(unique = true) //Unique 중복 없도록
    private String username;

    private String password;

    @Column(unique = true) //unique 중복 없도록
    private String email;
}
