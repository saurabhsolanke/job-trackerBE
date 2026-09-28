package com.jobtracker.dto.auth;

public record AuthResponse(
        String token,
        String tokenType,
        Long userId,
        String email,
        String fullName
) {
    public static AuthResponse of(String token, Long userId, String email, String fullName) {
        return new AuthResponse(token, "Bearer", userId, email, fullName);
    }
}
