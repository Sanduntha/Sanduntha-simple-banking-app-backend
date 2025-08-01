package com.example.app.controller;

import com.example.app.entity.User;
import com.example.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private User getUserFromHeader(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return userService.getUserFromToken(token);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String authHeader) {
        User user = getUserFromHeader(authHeader);

        Map<String, Object> profile = new HashMap<>();
        profile.put("username", user.getUsername());
        profile.put("email", user.getEmail());
        profile.put("balance", user.getBalance());
        profile.put("role", user.getRole());

        return ResponseEntity.ok(profile);
    }
}
