package com.example.studentschedulerv2.Repositories;

import android.app.Application;

import com.example.studentschedulerv2.DAO.MentorDAO;
import com.example.studentschedulerv2.Database.Database;
import com.example.studentschedulerv2.Entities.Mentor;

import java.util.List;

public class MentorRepository {

    private MentorDAO mentorDAO;
    private List<Mentor> allMentors;

    public MentorRepository(Application application) {
        Database database = Database.getInstance(application);
        mentorDAO = database.mentorDAO();
    }

    public void insert(Mentor mentor) {
        Database.databaseExecutor.execute(() -> mentorDAO.insert(mentor));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Mentor mentor) {
        Database.databaseExecutor.execute(() -> mentorDAO.update(mentor));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Mentor mentor) {
        Database.databaseExecutor.execute(() -> mentorDAO.delete(mentor));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Mentor> getAllMentors() {
        Database.databaseExecutor.execute(() -> allMentors = mentorDAO.getAllMentors());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allMentors;
    }

    public int getMaxId() {
        final int[] id = new int[1];
        Database.databaseExecutor.execute(() -> id[0] = mentorDAO.getMaxId());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return id[0];
    }
}
