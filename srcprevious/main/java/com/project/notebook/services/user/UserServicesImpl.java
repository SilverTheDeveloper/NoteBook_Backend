package com.project.notebook.services.user;

import com.project.notebook.entity.User;
import com.project.notebook.repository.RepoUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServicesImpl implements UserServicesMain{

    private final RepoUser user;

    public UserServicesImpl(RepoUser user) {
        this.user = user;
    }

    @Override
    public User registerUser(User user) {
        return this.user.save(user);
    }

    @Override
    public List<User> getAllUser() {

       return this.user.findAll();

    }

    @Override
    public User getUserById(Long id) {
        return this.user.findById(id).orElseThrow(()->new RuntimeException("no such user found."));
    }

    @Override
    public void deleteUserById(Long id) {
        if (!this.user.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        this.user.deleteById(id);
    }
}
