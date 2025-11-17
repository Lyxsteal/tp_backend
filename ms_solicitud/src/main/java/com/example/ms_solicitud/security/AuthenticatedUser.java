package com.example.ms_solicitud.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class AuthenticatedUser {
    private String userId;
    private String username;
    private List<String> roles;
}