package com.jobtracker.service;

import com.jobtracker.dto.auth.AuthResponse;
import com.jobtracker.dto.auth.LoginRequest;
import com.jobtracker.dto.auth.RegisterRequest;
import com.jobtracker.entity.User;
import com.jobtracker.exception.EmailAlreadyExistsException;
import com.jobtracker.repository.UserRepository;
import com.jobtracker.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        User user = User.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .build();

        User saved = userRepository.save(user);
        String token = jwtService.generateToken(saved);

        return AuthResponse.of(token, saved.getId(), saved.getEmail(), saved.getFullName());
    }

    public AuthResponse login(LoginRequest request) {
        // Delegates to Spring Security's AuthenticationManager, which uses the
        // configured DaoAuthenticationProvider (CustomUserDetailsService + BCrypt)
        // to verify the password. Throws BadCredentialsException on mismatch,
        // handled centrally by GlobalExceptionHandler -> 401.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalStateException("User authenticated but not found: " + request.email()));

        String token = jwtService.generateToken(user);

        return AuthResponse.of(token, user.getId(), user.getEmail(), user.getFullName());
    }
}
