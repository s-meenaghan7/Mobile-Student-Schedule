package com.example.studentschedulerv2.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.studentschedulerv2.Adapters.NoteAdapter;
import com.example.studentschedulerv2.R;
import com.example.studentschedulerv2.ViewModel.NoteViewModel;

import java.util.Objects;

public class CourseNotesList extends AppCompatActivity {

    private int courseId;
    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_notes_list);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        courseId = getIntent().getIntExtra("courseId", -1);
        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        // RecyclerView setup
        RecyclerView recyclerView = findViewById(R.id.note_recyclerview);
        final NoteAdapter noteAdapter = new NoteAdapter(new NoteAdapter.NoteDiff(), CourseNotesList.this);
        recyclerView.setAdapter(noteAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        noteViewModel.getAssociatedNotes(courseId).observe(this, noteAdapter::setNotes);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addNote(View view) {
        Intent intent = new Intent(CourseNotesList.this, AddNote.class);
        intent.putExtra("courseId", courseId);
        startActivity(intent);
    }
}