package com.project.notebook.events;

import com.project.notebook.entity.Note;
import com.project.notebook.entity.User;
import com.project.notebook.repository.RepoNotes;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * DemoNotesListener — listens for UserRegisteredEvent and creates 3 demo notes.
 *
 * Implements ApplicationListener<UserRegisteredEvent> — Spring automatically
 * calls onApplicationEvent() whenever a UserRegisteredEvent is published.
 *
 * Why this pattern?
 *   - UserServicesImpl stays focused on user logic only
 *   - Adding/removing demo notes never touches registration code
 *   - Easy to explain in interviews: "I used Spring's event system to decouple concerns"
 */
@Component
public class DemoNotesListener implements ApplicationListener<UserRegisteredEvent> {

    private final RepoNotes noteRepo;

    public DemoNotesListener(RepoNotes noteRepo) {
        this.noteRepo = noteRepo;
    }

    @Override
    public void onApplicationEvent(UserRegisteredEvent event) {
        User user = event.getUser();
        noteRepo.saveAll(buildDemoNotes(user));
    }

    /**
     * The 3 demo notes — edit the content here to change what new users see.
     * Each note is linked to the user via note.setUser(user).
     */
    private List<Note> buildDemoNotes(User user) {

        Note welcome = new Note();
        welcome.setTitle("👋 Welcome to Nota!");
        welcome.setContent(
            "Hi " + user.getName() + "! This is your personal notebook.\n\n" +
            "You can create, edit, and delete notes from your dashboard. " +
            "Use categories to stay organised — try General, Work, Personal, or Ideas."
        );
        welcome.setCategory("General");
        welcome.setUser(user);

        Note tips = new Note();
        tips.setTitle("💡 Tips & Tricks");
        tips.setContent(
            "Here are a few things you can do:\n\n" +
            "• Click any note card to edit it\n" +
            "• Use the search bar to filter notes instantly\n" +
            "• Click Delete twice to confirm deletion\n" +
            "• Hit the ↻ button to refresh from the server"
        );
        tips.setCategory("Ideas");
        tips.setUser(user);

        Note todo = new Note();
        todo.setTitle("📋 My First To-Do");
        todo.setContent(
            "Things to get started:\n\n" +
            "1. Edit this note and make it your own\n" +
            "2. Create a new note using the '+ New Note' button\n" +
            "3. Try different categories to organise your notes\n" +
            "4. Delete the demo notes when you're ready!"
        );
        todo.setCategory("Personal");
        todo.setUser(user);

        return List.of(welcome, tips, todo);
    }
}
