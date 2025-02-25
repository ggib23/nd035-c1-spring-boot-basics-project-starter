package com.udacity.jwdnd.course1.cloudstorage.services;

import java.util.List;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;

import org.springframework.stereotype.Service;

@Service
public class NoteService {
    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public int createNote(Note note) {
        return noteMapper.addNote(new Note(null, note.getNoteTitle(), note.getNoteDescription(), note.getUserId()));
    }

    public List<Note> getNotes(Integer userId) {
        return noteMapper.getAllNotes(userId);
    }

    public void deleteNote(Integer noteId) {
        noteMapper.deleteNoteId(noteId);
    }

    public void updateNote(Note note) {
        noteMapper.updateNote(note);
    }
}
