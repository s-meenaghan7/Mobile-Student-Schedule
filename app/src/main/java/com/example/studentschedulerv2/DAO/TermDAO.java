package com.example.studentschedulerv2.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.studentschedulerv2.Entities.Term;

import java.util.List;

@Dao
public interface TermDAO {

    @Insert
    void insert(Term term);

    @Update
    void update(Term term);

    @Delete
    void delete(Term term);

    @Query("SELECT * FROM terms ORDER BY id ASC")
    LiveData<List<Term>> getAllTerms();

    @Query("SELECT * FROM terms WHERE id=(SELECT max(id) FROM terms)")
    int getMaxId();
}
