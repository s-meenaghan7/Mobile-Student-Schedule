package com.example.studentschedulerv2.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.studentschedulerv2.Entities.Course;
import com.example.studentschedulerv2.Entities.Term;
import com.example.studentschedulerv2.Repositories.CourseRepository;

import java.util.List;
import java.util.stream.Collectors;

public class CourseViewModel extends AndroidViewModel {
    private CourseRepository repository;
    private LiveData<List<Course>> allCourses;

    public CourseViewModel(@NonNull Application application) {
        super(application);
        repository = new CourseRepository(application);
        allCourses = repository.getAllCourses();
    }

    public void insert(Course course) {
        repository.insert(course);
    }

    public void update(Course course) {
        repository.update(course);
    }

    public void delete(Course course) {
        repository.delete(course);
    }

    public LiveData<List<Course>> getAssociatedCourses(Term term) {
        return Transformations.map(
                allCourses,
                courses -> courses.stream()
                        .filter(course -> course.getTermId() == term.getId())
                        .collect(Collectors.toList()));
    }

    public int getMaxId() {
        return repository.getMaxId();
    }
}
