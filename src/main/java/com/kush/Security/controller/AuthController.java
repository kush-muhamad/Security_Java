package com.kush.Security.controller;

import com.kush.Security.Service.JwtService;
import com.kush.Security.Service.UserService;
import com.kush.Security.exceptions.InvalidCredentialsException;
import com.kush.Security.model.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public  AuthController(UserService userService , AuthenticationManager authenticationManager ,JwtService jwtService){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register/user")
    // http://localhost:4000/api/register/user
    public ResponseEntity<Map<String , Object>> registerUser( @RequestBody UserEntity user) {
        Map<String, Object> response = new HashMap<>();
        UserEntity newUser = userService.addUser(user);
        response.put("returnCode", 201);
        response.put("ReturnObject", newUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    // http://localhost:4000/api/login
    public ResponseEntity<Map<String, Object>> userLogin(@RequestBody UserEntity user){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));

        if (authentication.isAuthenticated()) {
            Map<String, Object> response = new HashMap<>();
            response.put("returnCode", 200);
            response.put("ReturnObject", jwtService.generateToken(user.getUserName()));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }


}

