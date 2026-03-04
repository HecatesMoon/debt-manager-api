package com.hecatesmoon.expenses_manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hecatesmoon.expenses_manager.dto.LoginRequest;
import com.hecatesmoon.expenses_manager.dto.RegisterRequest;
import com.hecatesmoon.expenses_manager.dto.UserResponse;
import com.hecatesmoon.expenses_manager.exception.BusinessException;
import com.hecatesmoon.expenses_manager.exception.ResourceNotFoundException;
import com.hecatesmoon.expenses_manager.model.User;
import com.hecatesmoon.expenses_manager.repository.UsersRepository;

@Service
public class UsersService {
    @Autowired
    private final UsersRepository repository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository repository, PasswordEncoder passwordEncoder){
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserById (Long id){
        return this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User does not exist: " + id));
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

    private void newUserValidation(RegisterRequest user){
        
        if (!user.getPassword().equals(user.getConfirmPassword())){
            throw new BusinessException("Passwords are not the same");
        }
        if (repository.existsByEmail(user.getEmail().toLowerCase())){
            throw new BusinessException("This email already has an account");
        }
    }
}