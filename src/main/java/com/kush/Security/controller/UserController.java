package com.kush.Security.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.kush.Security.Service.ResponseService;
import com.kush.Security.Service.UserService;
import com.kush.Security.model.UserEntity;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final AmazonS3 amazonS3;
    private  final ResponseService responseService;

    public UserController(UserService userService , AmazonS3 amazonS3 , ResponseService responseService) {
        this.userService = userService;
        this.amazonS3 = amazonS3;
        this.responseService = responseService;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<Map<String , Object>> getAllUsers(){
        List<UserEntity> userEntityList = userService.getAllUsers();
        return responseService.createResponse(200,userEntityList, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    @GetMapping("/users/{username}")
    public ResponseEntity<Map<String, Object>> getUserProfile(@PathVariable String username) {
        UserEntity userEntity = userService.getUserByUsername(username);
        if (userEntity == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return responseService.createResponse(200, userEntity,HttpStatus.OK);

    }
    @PreAuthorize("#username == authentication.name") // This ensures the authenticated user is the one trying to upload their own image
    @PostMapping("/users/{username}/profile-image")
    public ResponseEntity<Map<String, Object>> uploadProfileImage(
            @RequestParam("file") MultipartFile file,
            @PathVariable String username) throws IOException {
        // Upload the image and get the URL
        String imageUrl = userService.uploadProfileImage(file, username);
        return responseService.createResponse(200,imageUrl,HttpStatus.CREATED);

    }
    @PatchMapping("/edit/{username}")
    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable String username, @RequestBody UserEntity user) {
        UserEntity updatedUser = userService.updateUserProfile(username, user);
        return responseService.createResponse(200, updatedUser,HttpStatus.OK);

    }

    @DeleteMapping("/delete/{username}")
    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    public ResponseEntity<Map<String , Object>> deleteUser(@PathVariable String username){
        userService.deleteUser(username);
        return responseService.createResponse(200,"User Deleted Successfully",HttpStatus.OK);

    }








}



