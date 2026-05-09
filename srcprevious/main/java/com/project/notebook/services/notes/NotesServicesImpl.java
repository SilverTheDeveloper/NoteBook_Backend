package com.project.notebook.services.notes;

import com.project.notebook.entity.Note;
import com.project.notebook.entity.User;
import com.project.notebook.repository.RepoNotes;
import com.project.notebook.repository.RepoUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotesServicesImpl implements NotesServicesMain{

    private final RepoNotes noteRepo;

    private final RepoUser userRepo;

    public NotesServicesImpl(RepoNotes noteRepo, RepoUser userRepo) {
        this.noteRepo = noteRepo;
        this.userRepo = userRepo;
    }


    //here while saving a note, we are update a field in note entity
    // that is "user", to attach that note with that user.
    @Override
    public Note saveNoteForUser(Long userId, Note note) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        note.setUser(user);
        return noteRepo.save(note);
    }
    @Override
    public List<Note> getAllNotesForUser(Long userId) {
        return noteRepo.findAllByUserId(userId);
    }
    @Override
    public Note getNotesById(Long id) {
        return noteRepo.findById(id)
                .orElseThrow(() ->new RuntimeException("Note not found"));
    }
    @Override
    public Note updateNoteById(Long id, Note note) {
        Note existingNote = noteRepo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Note not found"));
        existingNote.setTitle(note.getTitle());
        existingNote.setContent(note.getContent());
        existingNote.setCategory(note.getCategory());
        return noteRepo.save(existingNote);
    }
    @Override
    public void deleteNoteForUser(Long Id) {
        if (!noteRepo.existsById(Id)) {
            throw new RuntimeException("Note not found");
        }
        noteRepo.deleteById(Id);
    }
}
