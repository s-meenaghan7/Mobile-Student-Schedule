package com.example.studentschedulerv2.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.studentschedulerv2.DAO.NoteDAO;
import com.example.studentschedulerv2.Database.Database;
import com.example.studentschedulerv2.Entities.Note;

import java.util.List;

public class NoteRepository {

    private NoteDAO noteDAO;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {
        Database database = Database.getInstance(application);
        noteDAO = database.noteDAO();
        allNotes = noteDAO.getAllNotes();
    }

    public void insert(Note note) {
        Database.databaseExecutor.execute(() -> noteDAO.insert(note));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Note note) {
        Database.databaseExecutor.execute(() -> noteDAO.update(note));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Note note) {
        Database.databaseExecutor.execute(() -> noteDAO.delete(note));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    public int getMaxId() {
        final int[] id = new int[1];
        Database.databaseExecutor.execute(() -> id[0] = noteDAO.getMaxId());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return id[0];
    }
}
