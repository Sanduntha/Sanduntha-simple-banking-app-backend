package com.example.app.service;

import com.example.app.dto.LoginDto;
import com.example.app.dto.UserDto;
import com.example.app.entity.User;

public interface UserService {
    String register(UserDto userDto);
    String login(LoginDto loginDto);
    User getUserFromToken(String token);
}
