package com.example.studentschedulerv2.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.studentschedulerv2.Entities.Mentor;
import com.example.studentschedulerv2.Repositories.MentorRepository;

import java.util.List;

public class MentorViewModel extends AndroidViewModel {
    private MentorRepository repository;

    public MentorViewModel(@NonNull Application application) {
        super(application);
        repository = new MentorRepository(application);
    }

    public void insert(Mentor mentor) {
        repository.insert(mentor);
    }

    public void update(Mentor mentor) {
        repository.update(mentor);
    }

    public void delete(Mentor mentor) {
        repository.delete(mentor);
    }

    public List<Mentor> getAllMentors() {
        return repository.getAllMentors();
    }

    public int getMaxId() {
        return repository.getMaxId();
    }
}
