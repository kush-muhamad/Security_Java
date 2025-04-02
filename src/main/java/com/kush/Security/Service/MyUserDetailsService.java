package com.kush.Security.Service;

import com.kush.Security.Repo.UserRepo;
import com.kush.Security.model.UserEntity;
import com.kush.Security.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepo.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));

        return new UserPrincipal(user);
    }

}



