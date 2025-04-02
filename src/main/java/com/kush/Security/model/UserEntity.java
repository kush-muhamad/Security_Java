package com.kush.Security.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users_table")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE) // testing purposes
    private Long id;
    @Column(name = "user_name", unique = true)
    private String userName;
    @Column(name = "email", unique = true)
    private String email;
    private String password;
    private String imageUrl;
    @Enumerated(EnumType.STRING) // stores the enum as a string in the db
    private Role role;


}
