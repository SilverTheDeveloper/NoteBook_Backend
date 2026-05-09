package com.project.notebook.controller;

import com.project.notebook.entity.Note;
import com.project.notebook.services.notes.NotesServicesMain;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NoteController {

    public final NotesServicesMain serv;

    public NoteController(NotesServicesMain serv) {
        this.serv = serv;
    }

    @PostMapping("/notes/user/{userId}")
    public Note saveNoteForUser(@PathVariable Long userId, @RequestBody Note note){
        return serv.saveNoteForUser(userId,note);
    }

    @GetMapping("/notes/user/{userId}")
    public List<Note> getAllNotesForUser(@PathVariable Long userId){
        return serv.getAllNotesForUser(userId);
    }

    @GetMapping("/notes/{id}")
    public Note getNotesById(@PathVariable Long id){
        return serv.getNotesById(id);
    }

    @PutMapping("/notes/{id}")
    public Note  updateNoteById(@PathVariable Long id,@RequestBody Note note) {
        return serv.updateNoteById(id,note);
    }

    @DeleteMapping("/notes/{Id}")
    public void deleteNoteForUser(@PathVariable Long Id){
      serv.deleteNoteForUser(Id);
    }


}
