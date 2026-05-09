package com.project.notebook.repository;

import com.project.notebook.entity.Note;
import com.project.notebook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepoNotes extends JpaRepository<Note,Long> {


    public List<Note> findAllByUserId(Long userId);

}
