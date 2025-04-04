package com.kush.Security.Service;

import com.kush.Security.Repo.InvalidTokenRepo;
import com.kush.Security.model.InvalidToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class InvalidTokenService {

    @Autowired
    private InvalidTokenRepo invalidTokenRepo;

    public void revokeToken(String token, Date expirationDate) {
        InvalidToken invalidToken = new InvalidToken(token,expirationDate);
        invalidTokenRepo.save(invalidToken);  // Save the revoked token to the database
    }

    // Method to check if a token has been revoked
    public boolean isTokenRevoked(String token) {
        return invalidTokenRepo.findByToken(token).isPresent();  // Check if the token is in the revoked list
    }
}
