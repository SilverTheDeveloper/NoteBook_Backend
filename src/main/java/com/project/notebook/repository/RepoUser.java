package com.project.notebook.repository;

import com.project.notebook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * RepoUser — added findByEmail for login lookup.
 * Spring Data JPA auto-generates the SQL from the method name.
 */
public interface RepoUser extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
