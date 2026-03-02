package com.hecatesmoon.expenses_manager.dto;

import com.hecatesmoon.expenses_manager.model.User;

public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    public static UserResponse from (User user){
        UserResponse response = new UserResponse();
        response.id = user.getId();
        response.firstName = user.getFirstName();
        response.lastName = user.getLastName();
        response.email = user.getEmail();
        return response;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

}
