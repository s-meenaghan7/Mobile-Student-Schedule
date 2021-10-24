package com.example.studentschedulerv2.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studentschedulerv2.Entities.Note;
import com.example.studentschedulerv2.R;
import com.example.studentschedulerv2.ViewModel.NoteViewModel;

import java.util.Objects;

public class AddNote extends AppCompatActivity {

    private int courseId;
    private int noteId;
    private NoteViewModel noteViewModel;
    private EditText noteTitle;
    private EditText noteContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        courseId = getIntent().getIntExtra("courseId", -1);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        noteId = noteViewModel.getMaxId();

        noteTitle = findViewById(R.id.addNoteTitle);
        noteContent = findViewById(R.id.addNoteContent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveNewNote(View view) {
        if (noteTitle.getText().toString().trim().isEmpty() ||
            noteContent.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this,
                    "Please include a title and content.", Toast.LENGTH_LONG).show();
            return;
        }

        String title = noteTitle.getText().toString();
        String content = noteContent.getText().toString();

        Note newNote = new Note(++noteId, title, content, courseId);
        noteViewModel.insert(newNote);
        Toast.makeText(getApplicationContext(),
                "Note added successfully.", Toast.LENGTH_LONG).show();

        this.finish();
    }
}