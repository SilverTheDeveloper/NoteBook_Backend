package com.project.notebook.services.user;

import com.project.notebook.entity.User;

import java.util.List;

public interface UserServicesMain {
    User registerUser(User user);
    // loginUser removed — Spring Security's AuthenticationManager handles login
    List<User> getAllUser();
    User getUserById(Long id);
    void deleteUserById(Long id);
}
