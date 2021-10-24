package com.example.studentschedulerv2.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.studentschedulerv2.DAO.AssessmentDAO;
import com.example.studentschedulerv2.Database.Database;
import com.example.studentschedulerv2.Entities.Assessment;

import java.util.List;

public class AssessmentRepository {

    private AssessmentDAO assessmentDAO;
    private LiveData<List<Assessment>> allAssessments;

    public AssessmentRepository(Application application) {
        Database database = Database.getInstance(application);
        assessmentDAO = database.assessmentDAO();
        allAssessments = assessmentDAO.getAllAssessments();
    }

    public void insert(Assessment assessment) {
        Database.databaseExecutor.execute(() -> assessmentDAO.insert(assessment));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Assessment assessment) {
        Database.databaseExecutor.execute(() -> assessmentDAO.update(assessment));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Assessment assessment) {
        Database.databaseExecutor.execute(() -> assessmentDAO.delete(assessment));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public LiveData<List<Assessment>> getAllAssessments() {
        return allAssessments;
    }

    public int getMaxId() {
        final int[] id = new int[1];
        Database.databaseExecutor.execute(() -> id[0] = assessmentDAO.getMaxId());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return id[0];
    }
}
