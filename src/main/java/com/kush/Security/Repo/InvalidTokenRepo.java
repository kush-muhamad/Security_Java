package com.kush.Security.Repo;

import com.kush.Security.model.InvalidToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvalidTokenRepo extends JpaRepository<InvalidToken , Long> {
    Optional<InvalidToken> findByToken(String token);
}
