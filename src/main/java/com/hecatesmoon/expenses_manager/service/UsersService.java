package com.hecatesmoon.expenses_manager.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hecatesmoon.expenses_manager.dto.LoginRequest;
import com.hecatesmoon.expenses_manager.dto.RegisterRequest;
import com.hecatesmoon.expenses_manager.dto.UserResponse;
import com.hecatesmoon.expenses_manager.exception.BusinessException;
import com.hecatesmoon.expenses_manager.exception.ResourceNotFoundException;
import com.hecatesmoon.expenses_manager.model.User;
import com.hecatesmoon.expenses_manager.repository.UsersRepository;
import com.hecatesmoon.expenses_manager.security.JwtUtil;

@Service
public class UsersService {
    @Autowired
    private final UsersRepository repository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UsersService(UsersRepository repository, 
                        PasswordEncoder passwordEncoder,
                        AuthenticationManager authenticationManager,
                        JwtUtil jwtUtil){
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public User getUserById (Long id){
        return this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User does not exist: " + id));
    }

    public User getUserByEmail (String email){
        return this.repository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User with this email does not exist: " + email));
    }

    public UserResponse createUser(RegisterRequest newUser){

        newUserValidation(newUser);

        User user = RegisterRequest.toEntity(newUser);

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        user = this.repository.save(user);

        return UserResponse.from(user);
    }

    public UserResponse loginValidation(LoginRequest login){
        User user = repository.findByEmail(login.getEmail().toLowerCase())
                              .orElseThrow(() -> new BusinessException("Email or Password not valid"));
        
        if (!passwordEncoder.matches(login.getPassword(), user.getPassword())){
            throw new BusinessException("Email or Password not valid");
        }

        return UserResponse.from(user);
    }

    public Map<String, Object> loginToken(LoginRequest login){
        
        User user = getUserFromEmail(login.getEmail());

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(user.getId(), login.getPassword()));
        
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = jwtUtil.generateToken(Long.valueOf(userDetails.getUsername()));

        Map<String, Object> response = new HashMap<>();
        response.put("user", user);
        response.put("token", token);

        return response;
    }

    public Map<String, Object> registerVTwo(RegisterRequest newUser){
        newUserValidation(newUser);

        User user = RegisterRequest.toEntity(newUser);

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        user = this.repository.save(user);

        String token = getTokenFromEmail(newUser.getEmail());

        Map<String, Object> response = new HashMap<>();
        response.put("user" ,UserResponse.from(user));
        response.put("token", token);
        
        return response;
    }

    private void newUserValidation(RegisterRequest user){
        
        if (!user.getPassword().equals(user.getConfirmPassword())){
            throw new BusinessException("Passwords are not the same");
        }
        if (repository.existsByEmail(user.getEmail().toLowerCase())){
            throw new BusinessException("This email already has an account");
        }
    }

    private String getTokenFromEmail(String email){

        Long id = getUserFromEmail(email).getId();

        return jwtUtil.generateToken(id);
    }

    private User getUserFromEmail(String email){
        User user = repository.findByEmail(email.toLowerCase()).orElseThrow(
            () -> new ResourceNotFoundException("There is no user with this email: " + email));
        return user;
    }
}