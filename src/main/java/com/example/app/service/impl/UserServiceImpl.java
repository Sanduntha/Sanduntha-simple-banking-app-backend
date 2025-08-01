package com.example.app.service.impl;

import com.example.app.dto.LoginDto;
import com.example.app.dto.UserDto;
import com.example.app.entity.User;
import com.example.app.repo.UserRepo;
import com.example.app.service.UserService;
import com.example.app.util.JWTTokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final JWTTokenGenerator jwtUtil;


    @Override
    public String register(UserDto userDto) {
        if (userRepo.findByEmail(userDto.getEmail()).isPresent()) {
            return "Email already exists!";
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(jwtUtil.encodePassword(userDto.getPassword()));
        user.setBalance(0.0);
        userRepo.save(user);
        return "Registration successful!";
    }


    @Override
    public String login(LoginDto loginDto) {
        Optional<User> optionalUser = userRepo.findByEmail(loginDto.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            String encodedRawPassword = jwtUtil.encodePassword(loginDto.getPassword());
            String storedPassword = user.getPassword();

            if (encodedRawPassword.equals(storedPassword)) {
                String token = jwtUtil.generateToken(user);
                return token;
            }
        }
        return "Invalid credentials!";
    }


    @Override
    public User getUserFromToken(String token) {
        String email = jwtUtil.getClaims(token).getPayload().getSubject();
        return userRepo.findByEmail(email).orElse(null);
    }
}
