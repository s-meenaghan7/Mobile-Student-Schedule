package com.example.studentschedulerv2.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.studentschedulerv2.Entities.Assessment;
import com.example.studentschedulerv2.Entities.Course;
import com.example.studentschedulerv2.Repositories.AssessmentRepository;

import java.util.List;
import java.util.stream.Collectors;

public class AssessmentViewModel extends AndroidViewModel {
    private AssessmentRepository repository;
    private LiveData<List<Assessment>> allAssessments;

    public AssessmentViewModel(@NonNull Application application) {
        super(application);
        repository = new AssessmentRepository(application);
        allAssessments = repository.getAllAssessments();
    }

    public void insert(Assessment assessment) {
        repository.insert(assessment);
    }

    public void update(Assessment assessment) {
        repository.update(assessment);
    }

    public void delete(Assessment assessment) {
        repository.delete(assessment);
    }

    public LiveData<List<Assessment>> getAssociatedAssessments(Course course) {
        return Transformations.map(
                allAssessments,
                assessments -> assessments.stream()
                            .filter(assessment -> assessment.getCourseId() == course.getId())
                            .collect(Collectors.toList()));
    }

    public int getMaxId() {
        return repository.getMaxId();
    }
}
