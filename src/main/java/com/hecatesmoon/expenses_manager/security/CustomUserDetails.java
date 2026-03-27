package com.hecatesmoon.expenses_manager.security;

import java.util.Collection;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails, CredentialsContainer {

    private String username;
    private String password;
    private Long id;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, Long id, String email){
        this.username = username;
        this.password = password;
        this.id = id;
        this.email = email;
        this.authorities = authorities;
    }

    @Override
    public String getUsername() { return username; }

    @Override
    public String getPassword() { return password; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    public Long getId() {return id;}

    public String getEmail() {return email;}

    @Override
    public void eraseCredentials(){ password=null;}
    
}
