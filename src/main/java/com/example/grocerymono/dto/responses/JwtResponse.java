package com.example.grocerymono.dto.responses;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String userId;
    private String username;
    private List<String> roles;

    public JwtResponse(String token, String userId, String username, List<String> roles) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.roles = roles;
    }
}
