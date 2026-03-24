package com.hecatesmoon.expenses_manager.service;

import java.util.Collections;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hecatesmoon.expenses_manager.model.User;
import com.hecatesmoon.expenses_manager.repository.UsersRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UsersRepository usersRepository;

    public CustomUserDetailsService (UsersRepository usersRepository){
        this.usersRepository=usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Long longId = Long.valueOf(id);
        User user = usersRepository.findById(longId)
                                    .orElseThrow(() -> new UsernameNotFoundException("There is no user with this id: " + longId));
        String stringId = String.valueOf(user.getId()); //todo: maybe this was too much
        return new org.springframework.security.core.userdetails.User(
                    stringId, //todo: consider use email 
                    user.getPassword(), 
                    Collections.emptyList());
    }
}
