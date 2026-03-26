package com.hecatesmoon.expenses_manager.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.hecatesmoon.expenses_manager.dto.LoginRequest;
import com.hecatesmoon.expenses_manager.dto.RegisterRequest;
import com.hecatesmoon.expenses_manager.service.UsersService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/auth")
public class UsersController {
    
    private final UsersService service;

    public UsersController (UsersService service){
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest user) {
        Map<String, Object> response = service.registerUser(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest login) {
        Map<String, Object> response = service.login(login);
        return ResponseEntity.ok(response);
    }
     
}