package com.example.studentschedulerv2.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.studentschedulerv2.Entities.Term;
import com.example.studentschedulerv2.Repositories.TermRepository;

import java.util.List;

public class TermViewModel extends AndroidViewModel {
    private TermRepository repository;
    private LiveData<List<Term>> allTerms;

    public TermViewModel(@NonNull Application application) {
        super(application);
        repository = new TermRepository(application);
        allTerms = repository.getAllTerms();
    }

    public void insert(Term term) {
        repository.insert(term);
    }

    public void update(Term term) {
        repository.update(term);
    }

    public void delete(Term term) {
        repository.delete(term);
    }

    public LiveData<List<Term>> getAllTerms() {
        return allTerms;
    }

    public int getMaxId() {
        return repository.getMaxId();
    }
}
