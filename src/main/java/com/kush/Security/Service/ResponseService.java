package com.kush.Security.Service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ResponseService {

    public ResponseEntity<Map<String, Object>> createResponse(int returnCode, Object returnObject, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("returnCode", returnCode);
        response.put("ReturnObject", returnObject);
        return new ResponseEntity<>(response, status);
    }
}
