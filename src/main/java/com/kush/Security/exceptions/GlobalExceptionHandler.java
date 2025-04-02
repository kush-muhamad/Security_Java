package com.kush.Security.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
        @ExceptionHandler(InvalidCredentialsException.class)
        public ResponseEntity<Map<String, Object>> handleInvalidCredentialsException(InvalidCredentialsException ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("returnCode", 401);
            response.put("message", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String , Object>> NotFound(UserNotFoundException ex){
            Map<String, Object> response = new HashMap<>();
            response.put("returnCode", 401);
            response.put("message", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        }
    }

