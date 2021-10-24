package com.example.studentschedulerv2.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.studentschedulerv2.Entities.Mentor;

import java.util.List;

@Dao
public interface MentorDAO {

    @Insert
    void insert(Mentor mentor);

    @Update
    void update(Mentor mentor);

    @Delete
    void delete(Mentor mentor);

    @Query("SELECT * FROM mentors")
    List<Mentor> getAllMentors(); // is never displayed; does not need LiveData wrapper

    @Query("SELECT * FROM mentors WHERE mentor_ID=(SELECT max(mentor_ID) FROM mentors)")
    int getMaxId();
}
