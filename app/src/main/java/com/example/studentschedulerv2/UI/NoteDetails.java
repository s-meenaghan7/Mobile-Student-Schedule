package com.example.studentschedulerv2.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studentschedulerv2.Entities.Note;
import com.example.studentschedulerv2.R;
import com.example.studentschedulerv2.Utilities.DeleteNoteDialogFragment;
import com.example.studentschedulerv2.ViewModel.NoteViewModel;

import java.util.Objects;

public class NoteDetails extends AppCompatActivity
                         implements DeleteNoteDialogFragment.DeleteNoteDialogListener {

    private NoteViewModel noteViewModel;
    private Note currentNote;
    private EditText noteTitle;
    private EditText noteContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        Intent i = getIntent();
        currentNote = new Note(i.getIntExtra("noteId", -1),
                               i.getStringExtra("title"),
                               i.getStringExtra("content"),
                               i.getIntExtra("courseId", -1));

        noteTitle = findViewById(R.id.addNoteTitle);
        noteTitle.setText(currentNote.getTitle());

        noteContent = findViewById(R.id.addNoteContent);
        noteContent.setText(currentNote.getContent());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_details, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.deleteNote:
                showDeleteNoteDialog();
                return true;
            case R.id.shareNote:
                Intent i = new Intent().setAction(Intent.ACTION_SEND).setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, currentNote.getContent());
                i.putExtra(Intent.EXTRA_TITLE, currentNote.getTitle());

                Intent shareIntent = Intent.createChooser(i, null);
                startActivity(shareIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteNoteDialog() {
        DialogFragment dialog = new DeleteNoteDialogFragment();
        dialog.show(getSupportFragmentManager(), "deleteNoteDialog");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        noteViewModel.delete(currentNote);
        dialog.dismiss();
        Toast.makeText(getApplicationContext(),
                "Note deleted successfully.", Toast.LENGTH_LONG).show();
        this.finish();
    }

    public void updateNote(View view) {
        if (noteTitle.getText().toString().trim().isEmpty() ||
            noteContent.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this,
                    "Please include a title and content.", Toast.LENGTH_LONG).show();
            return;
        }

        String title = noteTitle.getText().toString();
        String content = noteContent.getText().toString();

        Note updatedNote = new Note(currentNote.getId(), title, content, currentNote.getCourseId());
        noteViewModel.update(updatedNote);
        Toast.makeText(this,
                "Note successfully updated.", Toast.LENGTH_LONG).show();
        this.finish();
    }
}