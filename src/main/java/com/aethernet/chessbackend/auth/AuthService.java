package com.aethernet.chessbackend.auth;

import com.aethernet.chessbackend.auth.jwt.JwtConfig;
import com.aethernet.chessbackend.auth.jwt.JwtService;
import com.aethernet.chessbackend.user.User;
import com.aethernet.chessbackend.user.UserRepository;
import com.aethernet.chessbackend.user.dto.UserDto;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final JwtConfig jwtConfig;

    @Nullable
    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return null;

        var userDto = (UserDto) authentication.getPrincipal();
        if (userDto == null) return null;

        return userRepository.findById(userDto.getId()).orElse(null);
    }

}
