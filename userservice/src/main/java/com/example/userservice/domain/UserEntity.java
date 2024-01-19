package com.example.userservice.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Column(nullable = false, length = 50)
    private String userId;

    @Column(nullable = false, unique = true)
    private String encryptedPwd;

    @Column(nullable = false, length = 50)
    private String name;
}
