package com.kush.Security.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserPrincipal implements UserDetails {

    //private static final long serialVersionUID = 1L;


    private UserEntity user;

    public UserPrincipal(UserEntity user) {
        this.user=user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Check if the user has an "ADMIN" role
        if (user.getRole().equals(Role.ADMIN)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        // Check if the user has a "USER" role
        if (user.getRole().equals(Role.USER)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // Ensure user entity has password field
    }

    @Override
    public String getUsername() {
        return user.getUserName(); // Ensure user entity has username field
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;	}


    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
    public boolean isEnabled() {
        return true;
    }



}
