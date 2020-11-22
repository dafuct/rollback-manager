package com.makarenko.userservice.service;

import com.makarenko.userservice.dto.CreateUserDto;
import com.makarenko.userservice.dto.UserDto;

public interface UserService {

    String createUser(CreateUserDto createUserDto, String txId);

    void updateUser(UserDto userDto, String txId);
}
