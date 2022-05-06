package com.udacity.jwdnd.course1.cloudstorage.mapper;

import java.util.List;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface NoteMapper {
    @Select("SELECT * FROM NOTES WHERE userId = #{userId}")
    List<Note> getAllNotes(Integer userId);

    @Select("SELECT * FROM NOTES WHERE noteTitle = #{noteTitle}")
    Note getNote(String noteTitle);

    @Insert("INSERT INTO NOTES (noteTitle, noteDescription, userId) VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int addNote(Note note);

    @Delete("DELETE FROM NOTES WHERE noteId = #{noteId}")
    void deleteNoteId(Integer noteId);

    @Update("UPDATE NOTES SET noteTitle = #{noteTitle}, noteDescription = #{noteDescription} WHERE noteId = #{noteId}")
    void updateNote(Note note);
}
