package com.project.notebook.controller;

import com.project.notebook.entity.User;
import com.project.notebook.services.user.UserServicesMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserServicesMain serv;
    public UserController(UserServicesMain serv) {
        this.serv = serv;
    }

    @PostMapping("/register")
    private User registerUser(@RequestBody User user) {
        return serv.registerUser(user);
    }

    @GetMapping("/")
    private List<User> getAllUser() {
        return serv.getAllUser();
    }

    @GetMapping("/{id}")
    private User getUserById(@PathVariable Long id){
        return serv.getUserById(id);
    }

    @DeleteMapping("/{id}")
    private void deleteUserById(@PathVariable Long id){
        serv.deleteUserById(id);
    }

}
