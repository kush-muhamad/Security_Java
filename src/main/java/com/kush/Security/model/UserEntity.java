package com.kush.Security.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "Username cannot be empty")
    private String userName;
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be empty")
    @Column(name = "email", unique = true)
    private String email;
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
    private String imageUrl;
    //@NotBlank(message = "Role Cannot be blank")
    @Enumerated(EnumType.STRING) // stores the enum as a string in the db
    private Role role;


}
