package com.aethernet.chessbackend.user;

import com.aethernet.chessbackend.user.dto.RegisterUserRequest;
import com.aethernet.chessbackend.user.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(RegisterUserRequest request);

}