package org.example.bebyundohwi.week1.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.bebyundohwi.week1.domain.user.domain.UserEntity;
import org.example.bebyundohwi.week1.domain.user.domain.role.Role;
import org.example.bebyundohwi.week1.domain.user.dto.request.UserLoginRequest;
import org.example.bebyundohwi.week1.domain.user.exception.PasswordMissMatchException;
import org.example.bebyundohwi.week1.domain.user.exception.UserNotFoundException;
import org.example.bebyundohwi.week1.domain.user.repository.UserRepository;
import org.example.bebyundohwi.week1.global.security.jwt.JwtTokenProvider;
import org.example.bebyundohwi.week1.global.security.jwt.dto.TokenResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLoginService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public TokenResponse login(UserLoginRequest request) {
        UserEntity user = userRepository.findByUsername(request.getUsername()).orElseThrow(()-> UserNotFoundException.EXCEPTION);

        if(passwordEncoder.matches(user.getPassword(), request.getPassword())){
            throw PasswordMissMatchException.EXCEPTION;
        }

        return jwtTokenProvider.generateToken(request.getUsername(), user.getRole().toString());
    }

}
