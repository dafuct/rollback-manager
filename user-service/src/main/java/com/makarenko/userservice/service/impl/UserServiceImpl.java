package com.makarenko.userservice.service.impl;

import com.makarenko.userservice.dto.CreateUserDto;
import com.makarenko.userservice.dto.UserDto;
import com.makarenko.userservice.exception.UserException;
import com.makarenko.userservice.model.User;
import com.makarenko.userservice.repository.UserRepository;
import com.makarenko.userservice.service.RollBackService;
import com.makarenko.userservice.service.UserService;
import com.makarenko.userservice.util.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RollBackService rollBackService;

    @Override
    public String createUser(CreateUserDto createUserDto, String txId) {
        User user = User.builder()
                .name(createUserDto.getName())
                .email(createUserDto.getEmail())
                .build();
        User savedUser = userRepository.save(user);

        UserDto sendUser = UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();

        rollBackService.sendUserToRollback(sendUser, txId, false);

        return savedUser.getId();
    }

    @Override
    public void updateUser(UserDto userDto, String txId) {
        User userFromDb = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new UserException(String.format(ErrorMessages.NOT_FOUND.getMessage(), userDto.getId())));

        UserDto senderUser = UserDto.builder()
                .id(userFromDb.getId())
                .name(userFromDb.getName())
                .email(userFromDb.getEmail())
                .build();
        rollBackService.sendUserToRollback(senderUser, txId, true);

        userFromDb.setName(userDto.getName() == null ? userFromDb.getName() : userDto.getName());
        userFromDb.setEmail(userDto.getEmail() == null ? userFromDb.getEmail() : userDto.getEmail());

        userRepository.save(userFromDb);
    }
}
