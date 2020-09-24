package com.stackroute.keepnote.service;

import java.util.*;

import com.stackroute.keepnote.exception.NoteNotFoundExeption;
import com.stackroute.keepnote.model.Note;
import com.stackroute.keepnote.model.NoteUser;
import com.stackroute.keepnote.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * Service classes are used here to implement additional business logic/validation
 * This class has to be annotated with @Service annotation.
 * @Service - It is a specialization of the component annotation. It doesn't currently
 * provide any additional behavior over the @Component annotation, but it's a good idea
 * to use @Service over @Component in service-layer classes because it specifies intent
 * better. Additionally, tool support and additional behavior might rely on it in the
 * future.
 * */
@Service
public class NoteServiceImpl implements NoteService {

    /*
     * Autowiring should be implemented for the NoteRepository and MongoOperation.
     * (Use Constructor-based autowiring) Please note that we should not create any
     * object using the new keyword.
     */
    private NoteRepository noteRepository;

    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    /*
     * This method should be used to save a new note.
     */
    public boolean createNote(Note note) {
        UUID uuid = UUID.randomUUID();
        note.setNoteId(uuid.hashCode());
        note.setNoteCreationDate(new Date());
        NoteUser newNoteUser = new NoteUser();
        newNoteUser.setUserId(note.getNoteCreatedBy());
        newNoteUser.setNotes(new ArrayList<>());
        newNoteUser.getNotes().add(note);
        NoteUser addedNoteUser = null;
        Optional<NoteUser> optionalNoteUser = noteRepository.findById(note.getNoteCreatedBy());
        if (optionalNoteUser.isPresent()) {
            optionalNoteUser.get().getNotes().add(note);
            addedNoteUser = noteRepository.save(optionalNoteUser.get());
        } else {
            addedNoteUser = noteRepository.insert(newNoteUser);
        }
        if (addedNoteUser == null) {
            return false;
        } else {
            return true;
        }
    }

    /* This method should be used to delete an existing note. */


    public boolean deleteNote(String userId, int noteId) {
        Optional<NoteUser> noteUser = noteRepository.findById(userId);
        if (noteUser.isPresent()) {
            List<Note> notes = noteUser.get().getNotes();
            boolean status = notes.removeIf(n -> n.getNoteId() == noteId);
            noteRepository.save(noteUser.get());
            return status;
        } else {
            return false;
        }
    }

    /* This method should be used to delete all notes with specific userId. */


    public boolean deleteAllNotes(String userId) throws NoteNotFoundExeption {

        Optional<NoteUser> optionalNoteUser = noteRepository.findById(userId);
        if (optionalNoteUser.isPresent()) {
            NoteUser noteUser = optionalNoteUser.get();
            List<Note> notes = noteUser.getNotes();
            if (notes.isEmpty()) {
                throw new NoteNotFoundExeption("No notes found");
            } else {
                noteUser.getNotes().clear();
                noteRepository.save(noteUser);
                return true;
            }
        } else {
            return false;
        }
    }

    /*
     * This method should be used to update a existing note.
     */
    public Note updateNote(Note note, int id, String userId) throws NoteNotFoundExeption {
        try {
            Optional<NoteUser> noteUser = noteRepository.findById(userId);
            if (noteUser.isPresent()) {
                Note foundNote = noteUser.get().getNotes().stream()
                        .filter(n -> n.getNoteId() == id)
                        .findAny()
                        .orElse(null);
                if (foundNote != null) {
                    note.setNoteId(id);
                    NoteUser foundNoteUser = noteUser.get();
                    copyNote(note, foundNote);
                    noteRepository.save(foundNoteUser);
                    return note;
                } else {
                    throw new NoteNotFoundExeption("Note not found");
                }
            } else {
                throw new NoteNotFoundExeption("Note not found");
            }
        } catch (NoSuchElementException e) {
            throw new NoteNotFoundExeption("Note not found");
        }
    }

    /*
     * This method should be used to get a note by noteId created by specific user
     */
    public Note getNoteByNoteId(String userId, int noteId) throws NoteNotFoundExeption {
        try {
            Optional<NoteUser> noteUser = noteRepository.findById(userId);
            if (noteUser.isPresent()) {
                Note note = noteUser.get().getNotes().stream().filter(n -> n.getNoteId() == noteId).findAny().orElse(null);
                if (note != null) {
                    return note;
                } else {
                    throw new NoteNotFoundExeption("Note not found");
                }
            } else {
                throw new NoteNotFoundExeption("Note not found");
            }
        } catch (NoSuchElementException e) {
            throw new NoteNotFoundExeption("Note not found");
        }
    }

    /*
     * This method should be used to get all notes with specific userId.
     */
    public List<Note> getAllNoteByUserId(String userId) {
        Optional<NoteUser> noteUser = noteRepository.findById(userId);
        if (noteUser.isPresent()) {
            return noteUser.get().getNotes();
        } else {
            return null;
        }
    }

    public void copyNote(Note source, Note dest) {
        dest.setNoteTitle(source.getNoteTitle());
        dest.setNoteContent(source.getNoteContent());
        dest.setNoteStatus(source.getNoteStatus());
        dest.setNoteCreationDate(source.getNoteCreationDate());
        dest.setNoteCreatedBy(source.getNoteCreatedBy());
        dest.setCategory(source.getCategory());
        dest.setReminders(source.getReminders());
    }

}
