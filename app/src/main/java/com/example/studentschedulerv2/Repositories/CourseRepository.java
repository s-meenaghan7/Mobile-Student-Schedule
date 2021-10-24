package com.example.studentschedulerv2.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.studentschedulerv2.DAO.CourseDAO;
import com.example.studentschedulerv2.Database.Database;
import com.example.studentschedulerv2.Entities.Course;

import java.util.List;

public class CourseRepository {

    private CourseDAO courseDAO;
    private LiveData<List<Course>> allCourses;

    public CourseRepository(Application application) {
        Database database = Database.getInstance(application);
        courseDAO = database.courseDAO();
        allCourses = courseDAO.getAllCourses();
    }

    public void insert(Course course) {
        Database.databaseExecutor.execute(() -> courseDAO.insert(course));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Course course) {
        Database.databaseExecutor.execute(() -> courseDAO.update(course));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void delete(Course course) {
        Database.databaseExecutor.execute(() -> courseDAO.delete(course));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    }

    public int getMaxId() {
        final int[] id = new int[1];
        Database.databaseExecutor.execute(() -> id[0] = courseDAO.getMaxId());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return id[0];
    }
}
