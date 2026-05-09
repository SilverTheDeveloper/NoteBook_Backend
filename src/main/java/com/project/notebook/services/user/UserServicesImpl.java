package com.project.notebook.services.user;

import com.project.notebook.entity.User;
import com.project.notebook.events.UserRegisteredEvent;
import com.project.notebook.repository.RepoUser;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * UserServicesImpl — updated for traditional Spring Security flow.
 *
 * registerUser: BCrypt-hashes the password, saves the user, then publishes
 * a UserRegisteredEvent so DemoNotesListener can create the 3 demo notes.
 *
 * loginUser is handled by Spring Security's AuthenticationManager.
 */
@Service
public class UserServicesImpl implements UserServicesMain {

    private final RepoUser                 userRepo;
    private final PasswordEncoder          passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    public UserServicesImpl(RepoUser userRepo,
                            PasswordEncoder passwordEncoder,
                            ApplicationEventPublisher eventPublisher) {
        this.userRepo       = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher  = eventPublisher;
    }

    @Override
    public User registerUser(User user) {
        // Hash the password before persisting — never store plain text
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepo.save(user);

        // Publish event — DemoNotesListener picks this up and creates 3 demo notes
        eventPublisher.publishEvent(new UserRegisteredEvent(this, saved));

        return saved;
    }

    @Override
    public List<User> getAllUser() {
        return userRepo.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("No such user found."));
    }

    @Override
    public void deleteUserById(Long id) {
        if (!userRepo.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepo.deleteById(id);
    }
}
