package com.project.notebook.services.notes;

import com.project.notebook.entity.Note;

import java.util.List;

public interface NotesServicesMain {

   public Note saveNoteForUser(Long userId,Note note);
   public List<Note> getAllNotesForUser(Long userId);
   public Note getNotesById(Long id);
   public Note updateNoteById(Long id,Note note);
   public void deleteNoteForUser(Long Id);

}
