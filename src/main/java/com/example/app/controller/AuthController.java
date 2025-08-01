package com.example.app.controller;

import com.example.app.dto.LoginDto;
import com.example.app.dto.UserDto;
import com.example.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.register(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        System.out.println("Login attempt for email: " + loginDto.getEmail());

        String token = userService.login(loginDto);

        if (token.equals("Invalid credentials!")) {
            System.out.println("Login failed: Invalid credentials for email: " + loginDto.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(token);
        }

        System.out.println("Login successful: Token generated for email: " + loginDto.getEmail());
        return ResponseEntity.ok(token);
    }

}

