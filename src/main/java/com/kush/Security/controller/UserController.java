package com.kush.Security.controller;

import com.amazonaws.services.s3.AmazonS3;
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

    public UserController(UserService userService , AmazonS3 amazonS3) {
        this.userService = userService;
        this.amazonS3 = amazonS3;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<Map<String , Object>> getAllUsers(){
        Map<String , Object> response = new HashMap<>();
        List<UserEntity> userEntityList = userService.getAllUsers();
        response.put("returnCode",200);
        response.put("returnMessage", userEntityList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    @GetMapping("/users/{username}")
    public ResponseEntity<Map<String, Object>> getUserProfile(@PathVariable String username) {
        Map<String, Object> response = new HashMap<>();

        UserEntity userEntity = userService.getUserByUsername(username);
        if (userEntity == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        response.put("returnCode", 200);
        response.put("returnMessage", userEntity);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PreAuthorize("#username == authentication.name") // This ensures the authenticated user is the one trying to upload their own image
    @PostMapping("/users/{username}/profile-image")
    public ResponseEntity<Map<String, Object>> uploadProfileImage(
            @RequestParam("file") MultipartFile file,
            @PathVariable String username) throws IOException {

        Map<String, Object> response = new HashMap<>();

        // Upload the image and get the URL
        String imageUrl = userService.uploadProfileImage(file, username);

        response.put("returnCode", 200);
        response.put("returnMessage", "Profile image uploaded successfully.");
        response.put("imageUrl", imageUrl);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PatchMapping("/edit/{username}")
    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable String username, @RequestBody UserEntity user) {
        Map<String, Object> response = new HashMap<>();

        UserEntity updatedUser = userService.updateUserProfile(username, user);

        response.put("returnCode", 200);
        response.put("ReturnObject", updatedUser);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{username}")
    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    public ResponseEntity<Map<String , Object>> deleteUser(@PathVariable String username){
        Map<String , Object> response = new HashMap<>();
        userService.deleteUser(username);
        response.put("returnCode", 200);
        response.put("ReturnObject", "User deleted successfully");
        return new ResponseEntity<>(response , HttpStatus.OK);
    }








}



