package com.kush.Security.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.kush.Security.Repo.UserRepo;
import com.kush.Security.exceptions.UserNotFoundException;
import com.kush.Security.model.Role;
import com.kush.Security.model.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final AmazonS3 amazonS3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;


    private  BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    public UserService(UserRepo userRepo , AmazonS3 amazonS3Client) {
        this.userRepo = userRepo;
        this.amazonS3Client = amazonS3Client;
    }




    public UserEntity addUser(UserEntity user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        if (userRepo.count() == 0) {
            user.setRole(Role.ADMIN);
        } else {
            user.setRole(Role.USER);
        }
        return userRepo.save(user);
    }

    public List<UserEntity> getAllUsers() {
        return userRepo.findAll();
    }

    public UserEntity getUserByUsername(String username) {
        return userRepo.findByUserName(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public String  uploadProfileImage(MultipartFile file ,String username ) throws IOException {
        UserEntity user = userRepo.findByUserName(username)
                .orElseThrow(()-> new UserNotFoundException("user not found"));

        File fileObj = convertToFile(file);

        String fileName = System.currentTimeMillis()+ "_" + file.getOriginalFilename();

        amazonS3Client.putObject(new PutObjectRequest(bucketName,fileName,fileObj));

        String imageUrl = amazonS3Client.getUrl(bucketName,fileName).toString();

        user.setImageUrl(imageUrl);
        userRepo.save(user);

        fileObj.delete();
        return imageUrl;
    }

    private File convertToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        try(FileOutputStream foo = new FileOutputStream(convertedFile)){
            foo.write(file.getBytes());
        }
        return convertedFile;
    }


    public UserEntity updateUserProfile(String userName, UserEntity user) {
        Optional<UserEntity> existingUser = userRepo.findByUserName(userName);

        if (existingUser.isEmpty()) {
            throw new UserNotFoundException("User not found with username: " + userName);
        }

        UserEntity userEntity = existingUser.get(); // Extract the actual user object

        // Update fields only if they are not null
        if (user.getUserName() != null) {
            userEntity.setUserName(user.getUserName());
        }

        if (user.getEmail() != null) {
            userEntity.setEmail(user.getEmail());
        }

        if (user.getPassword() != null) {
            userEntity.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }

        // Save the updated user back to the database
        return userRepo.save(userEntity);
    }

    public void deleteUser(String username) {
        Optional<UserEntity> user = userRepo.findByUserName(username);
        if (user.isPresent()) {
            userRepo.delete(user.get());
        } else {
            throw new UserNotFoundException("User not found with username: " + username);
        }
    }


}
