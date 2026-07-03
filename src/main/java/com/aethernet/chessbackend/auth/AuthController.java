package com.aethernet.chessbackend.auth;

import com.aethernet.chessbackend.auth.dto.JwtResponse;
import com.aethernet.chessbackend.auth.dto.LoginRequest;
import com.aethernet.chessbackend.auth.jwt.JwtConfig;
import com.aethernet.chessbackend.auth.jwt.JwtService;
import com.aethernet.chessbackend.user.UserMapper;
import com.aethernet.chessbackend.user.UserRepository;
import com.aethernet.chessbackend.user.dto.UserDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final JwtConfig jwtConfig;
    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        String accessToken = jwtService.generateAccessToken(user).toString();
        String refreshToken = jwtService.generateRefreshToken(user).toString();

        var accessCookie = new Cookie("accessToken", accessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(jwtConfig.getAccessTokenExpiration()); // 7 days
        accessCookie.setSecure(true);

        response.addCookie(accessCookie);

        var refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/auth/refresh");
        refreshCookie.setMaxAge(jwtConfig.getRefreshTokenExpiration()); // 7 days
        refreshCookie.setSecure(true);

        response.addCookie(refreshCookie);

        return ResponseEntity.ok(new JwtResponse(accessToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(
            @CookieValue(value = "refreshToken") String refreshToken,
            HttpServletResponse response
    ){
        var jwt = jwtService.parse(refreshToken);
        if(jwt == null || jwt.isExpired()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var user = userRepository.findById(jwt.getUserId()).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);

        var accessCookie = new Cookie("accessToken", accessToken.toString());
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(jwtConfig.getAccessTokenExpiration()); // 7 days
        accessCookie.setSecure(true);

        response.addCookie(accessCookie);

        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(
            @AuthenticationPrincipal UserDto user
    ){
        var currentUser = authService.getCurrentUser();

        System.out.println(userMapper.toDto(currentUser));
        return ResponseEntity.ok(userMapper.toDto(currentUser));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentialsExceptions(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}