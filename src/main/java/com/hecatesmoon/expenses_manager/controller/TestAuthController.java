package com.hecatesmoon.expenses_manager.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hecatesmoon.expenses_manager.dto.LoginRequest;
import com.hecatesmoon.expenses_manager.dto.RegisterRequest;
import com.hecatesmoon.expenses_manager.dto.UserResponse;
import com.hecatesmoon.expenses_manager.service.UsersService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth/test/")
public class TestAuthController {
    private final UsersService usersService;

    public TestAuthController (UsersService usersService)
    {
        this.usersService = usersService;
    }

    @PostMapping("/signin")
    public ResponseEntity<Map<String, String>> authentication (@Valid @RequestBody LoginRequest user){
        Map<String, String> response = usersService.loginToken(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> register (@Valid @RequestBody RegisterRequest newUser){
        UserResponse response = usersService.createUser(newUser);

        return ResponseEntity.ok(response);
    }

}
