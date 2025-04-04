package com.kush.Security.controller;

import com.kush.Security.Service.InvalidTokenService;
import com.kush.Security.Service.JwtService;
import com.kush.Security.Service.ResponseService;
import com.kush.Security.Service.UserService;
import com.kush.Security.exceptions.InvalidCredentialsException;
import com.kush.Security.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    private  ResponseService responseService;

    @Autowired
    private InvalidTokenService invalidTokenService;




    public  AuthController(UserService userService , AuthenticationManager authenticationManager ,JwtService jwtService){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register/user")
    // http://localhost:4000/api/register/user
    public ResponseEntity<Map<String , Object>> registerUser( @RequestBody UserEntity user) {

        UserEntity newUser = userService.addUser(user);
       return  responseService.createResponse(200,newUser,HttpStatus.OK);

    }

    @PostMapping("/login")
    // http://localhost:4000/api/login
    public ResponseEntity<Map<String, Object>> userLogin(@RequestBody UserEntity user){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));

        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(user.getUserName());
            return responseService.createResponse(200,token,HttpStatus.OK);

        } else {
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);  // Extract the token

        // Get the token expiration date from the JwtService (or generate it as needed)
        Date expirationDate = jwtService.extractExpiration(token); // Assuming this method exists

        // Revoke the token
        invalidTokenService.revokeToken(token, expirationDate);

        return responseService.createResponse(200, "Logged Out",HttpStatus.OK);
    }



}

