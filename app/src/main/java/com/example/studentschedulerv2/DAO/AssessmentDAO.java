package com.example.studentschedulerv2.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.studentschedulerv2.Entities.Assessment;

import java.util.List;

@Dao
public interface AssessmentDAO {

    @Insert
    void insert(Assessment assessment);

    @Update
    void update(Assessment assessment);

    @Delete
    void delete(Assessment assessment);

    @Query("SELECT * FROM assessments")
    LiveData<List<Assessment>> getAllAssessments();

    @Query("SELECT * FROM assessments WHERE id=(SELECT max(id) FROM assessments)")
    int getMaxId();
}
