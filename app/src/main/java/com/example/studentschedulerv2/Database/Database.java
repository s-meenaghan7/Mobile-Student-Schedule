package com.example.studentschedulerv2.Database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.studentschedulerv2.DAO.AssessmentDAO;
import com.example.studentschedulerv2.DAO.CourseDAO;
import com.example.studentschedulerv2.DAO.MentorDAO;
import com.example.studentschedulerv2.DAO.NoteDAO;
import com.example.studentschedulerv2.DAO.TermDAO;
import com.example.studentschedulerv2.Entities.Assessment;
import com.example.studentschedulerv2.Entities.Course;
import com.example.studentschedulerv2.Entities.Mentor;
import com.example.studentschedulerv2.Entities.Note;
import com.example.studentschedulerv2.Entities.Term;
import com.example.studentschedulerv2.Utilities.Converters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@androidx.room.Database(entities = {Term.class, Course.class, Mentor.class, Assessment.class, Note.class}, version = 7, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class Database extends RoomDatabase {

    // used to access the DAO
    public abstract TermDAO termDAO();
    public abstract CourseDAO courseDAO();
    public abstract AssessmentDAO assessmentDAO();
    public abstract MentorDAO mentorDAO();
    public abstract NoteDAO noteDAO();

    private static Database INSTANCE; // class is singleton
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static synchronized Database getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    Database.class, "student_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

}
