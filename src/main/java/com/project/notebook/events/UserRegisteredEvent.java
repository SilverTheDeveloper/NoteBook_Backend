package com.project.notebook.events;

import com.project.notebook.entity.User;
import org.springframework.context.ApplicationEvent;

/**
 * UserRegisteredEvent — fired by UserServicesImpl right after a new user is saved.
 *
 * Using Spring's ApplicationEvent pattern keeps the registration logic clean:
 * UserServicesImpl doesn't need to know anything about demo notes.
 * The listener handles it separately, following the Open/Closed principle.
 */
public class UserRegisteredEvent extends ApplicationEvent {

    private final User user;

    public UserRegisteredEvent(Object source, User user) {
        super(source);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
