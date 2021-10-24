package com.example.studentschedulerv2.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.studentschedulerv2.DAO.TermDAO;
import com.example.studentschedulerv2.Database.Database;
import com.example.studentschedulerv2.Entities.Term;

import java.util.List;

public class TermRepository {

    private TermDAO termDAO;
    private LiveData<List<Term>> allTerms;

    public TermRepository(Application application) {
        Database database = Database.getInstance(application);
        termDAO = database.termDAO();
        allTerms = termDAO.getAllTerms();
    }

    public void insert(Term term) {
        Database.databaseExecutor.execute(() -> termDAO.insert(term));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Term term) {
        Database.databaseExecutor.execute(() -> termDAO.update(term));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Term term) {
        Database.databaseExecutor.execute(() -> termDAO.delete(term));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public LiveData<List<Term>> getAllTerms() {
        return allTerms;
    }

    public int getMaxId() {
        final int[] id = new int[1];
        Database.databaseExecutor.execute(() -> id[0] = termDAO.getMaxId());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return id[0];
    }
}
