package com.project.notebook.repository;

import com.project.notebook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepoUser extends JpaRepository<User,Long> {
}
