package com.example.studentschedulerv2.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.studentschedulerv2.Entities.Course;
import com.example.studentschedulerv2.Entities.Note;
import com.example.studentschedulerv2.Repositories.NoteRepository;

import java.util.List;
import java.util.stream.Collectors;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository repository;
    private LiveData<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
    }

    public void insert(Note note) {
        repository.insert(note);
    }

    public void update(Note note) {
        repository.update(note);
    }

    public void delete(Note note) {
        repository.delete(note);
    }

    public LiveData<List<Note>> getAssociatedNotes(int courseId) {
        return Transformations.map(
                allNotes,
                notes -> notes.stream()
                        .filter(note -> note.getCourseId() == courseId)
                        .collect(Collectors.toList()));
    }

    public int getMaxId() {
        return repository.getMaxId();
    }
}
