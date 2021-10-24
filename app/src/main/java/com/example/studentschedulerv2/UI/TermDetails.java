package com.example.studentschedulerv2.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentschedulerv2.Adapters.CourseAdapter;
import com.example.studentschedulerv2.Entities.Term;
import com.example.studentschedulerv2.R;
import com.example.studentschedulerv2.ViewModel.CourseViewModel;
import com.example.studentschedulerv2.ViewModel.TermViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class TermDetails extends AppCompatActivity {
    private Term currentTerm;
    private TermViewModel termViewModel; // required for updating

    private CourseAdapter courseAdapter;

    private EditText termTitle;
    private final Calendar myCalendar = Calendar.getInstance();
    private TextView editStartDate;
    private DatePickerDialog.OnDateSetListener startDatePicker;

    private TextView editEndDate;
    private DatePickerDialog.OnDateSetListener endDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent i = getIntent();
        currentTerm = new Term(i.getIntExtra("termId", -1),
                               i.getStringExtra("title"),
                        (Date) i.getSerializableExtra("startDate"),
                        (Date) i.getSerializableExtra("endDate"));

        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);

        termTitle = findViewById(R.id.editTextTermTitle);
        termTitle.setText(currentTerm.getTitle());

        editStartDate = findViewById(R.id.editTermStartDate);
        editEndDate = findViewById(R.id.editTermEndDate);

        String myFormat = "MM/dd/yy";
        String formattedStart = new SimpleDateFormat(myFormat, Locale.US).format(currentTerm.getStartDate());
        String formattedEnd = new SimpleDateFormat(myFormat, Locale.US).format(currentTerm.getEndDate());

        editStartDate.setText(formattedStart);
        editEndDate.setText(formattedEnd);

        startDatePicker = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateLabel(editStartDate);
        };

        endDatePicker = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateLabel(editEndDate);
        };

        editStartDate.setOnClickListener(view -> new DatePickerDialog(TermDetails.this, startDatePicker, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        editEndDate.setOnClickListener(view -> new DatePickerDialog(TermDetails.this, endDatePicker, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        RecyclerView courseRecyclerView = findViewById(R.id.course_recyclerview);
        courseAdapter = new CourseAdapter(new CourseAdapter.CourseDiff(), TermDetails.this);
        courseRecyclerView.setAdapter(courseAdapter);
        courseRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        CourseViewModel courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        courseViewModel.getAssociatedCourses(currentTerm).observe(this, courseAdapter::setCourses);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_term_details, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.deleteTerm:
                if (courseAdapter.getItemCount() == 0) {
                    Toast.makeText(getApplicationContext(),
                           currentTerm.getTitle() + " deleted successfully.",
                            Toast.LENGTH_LONG).show();
                    termViewModel.delete(currentTerm);
                    this.finish();
                } else {
                    Snackbar.make(findViewById(R.id.termDetailsView),
                            currentTerm.getTitle() + " has courses that must first be deleted before this term can be deleted.",
                            BaseTransientBottomBar.LENGTH_LONG).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateLabel(TextView editText) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(myCalendar.getTime()));
    }

    public void updateTerm(View view) throws ParseException {
        if (termTitle.getText().toString().trim().isEmpty() ||
            editStartDate.getText().toString().trim().isEmpty() ||
            editEndDate.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this,
                    "Please include a term title, start date, and end date", Toast.LENGTH_LONG).show();
            return;
        }

        String format = "MM/dd/yy";
        String title = termTitle.getText().toString();
        Date startDate = new SimpleDateFormat(format, Locale.US).parse(editStartDate.getText().toString());
        Date endDate = new SimpleDateFormat(format, Locale.US).parse(editEndDate.getText().toString());

        Term updatedTerm = new Term(currentTerm.getId(), title, startDate, endDate);
        termViewModel.update(updatedTerm);

        this.finish();
        Toast.makeText(this,
                title + " updated successfully.", Toast.LENGTH_LONG).show();
    }

    public void addCourse(View view) {
        Intent intent = new Intent(TermDetails.this, AddCourse.class);
        intent.putExtra("termId", currentTerm.getId());
        startActivity(intent);
    }
}