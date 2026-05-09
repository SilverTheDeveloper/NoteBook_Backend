package com.project.notebook.services.user;

import com.project.notebook.entity.User;

import java.util.List;

public interface UserServicesMain {
   public User registerUser(User user);
   public List<User> getAllUser();
   public User getUserById(Long id);
   public void deleteUserById(Long id);

}
