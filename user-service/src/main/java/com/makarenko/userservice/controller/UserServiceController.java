package com.makarenko.userservice.controller;

import com.makarenko.userservice.dto.CreateUserDto;
import com.makarenko.userservice.dto.UserDto;
import com.makarenko.userservice.exception.UserException;
import com.makarenko.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

import static com.makarenko.userservice.util.ErrorMessages.COSTUME_EXCEPTION;

@RestController
public class UserServiceController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/save")
    public String saveUser(@RequestBody CreateUserDto createUserDto, @RequestParam String txId) {
        generateException();
        return userService.createUser(createUserDto, txId);
    }

    @PostMapping(value = "/update")
    public void updateUser(@RequestBody UserDto userDto, @RequestParam String txId) {
        generateException();
        userService.updateUser(userDto, txId);
    }

    private void generateException() {
        Random random = new Random();
        int value = random.nextInt(5);
        if (value == 2) {
            throw new UserException(COSTUME_EXCEPTION.getMessage());
        }
    }
}
