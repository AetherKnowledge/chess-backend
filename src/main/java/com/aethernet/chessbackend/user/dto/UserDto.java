package com.aethernet.chessbackend.user.dto;

import com.aethernet.chessbackend.auth.jwt.Jwt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder
@ToString
public class UserDto {
    private UUID id;
    private String name;
    private String email;
    private Role role;

    public UserDto(Jwt jwt) {
        this.id = jwt.getUserId();
        this.email = jwt.getEmail();
        this.name = jwt.getName();
        this.role = jwt.getRole();
    }
}

