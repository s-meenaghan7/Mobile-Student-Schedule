package com.example.studentschedulerv2.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentschedulerv2.Entities.Course;
import com.example.studentschedulerv2.Entities.Mentor;
import com.example.studentschedulerv2.R;
import com.example.studentschedulerv2.Utilities.CreateMentorDialogFragment;
import com.example.studentschedulerv2.Utilities.Status;
import com.example.studentschedulerv2.ViewModel.CourseViewModel;
import com.example.studentschedulerv2.ViewModel.MentorViewModel;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddCourse extends AppCompatActivity
                       implements CreateMentorDialogFragment.CreateMentorDialogListener {

    private CourseViewModel courseViewModel;
    private MentorViewModel mentorViewModel;

    private Mentor mentor;
    private int mentorId;
    private int courseId;
    private int termId;
    private EditText courseTitle;
    private final Calendar myCalendar = Calendar.getInstance();

    private TextView editStartDate;
    private DatePickerDialog.OnDateSetListener startDatePicker;

    private TextView editEndDate;
    private DatePickerDialog.OnDateSetListener endDatePicker;

    private Spinner statusDropdown;
    private TextView editMentor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        courseId = courseViewModel.getMaxId() + 1;
        termId = getIntent().getIntExtra("termId", -1);

        courseTitle = findViewById(R.id.addCourseTitle);
        editStartDate = findViewById(R.id.addCourseStartDate);
        editEndDate = findViewById(R.id.addCourseEndDate);

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

        editStartDate.setOnClickListener(view -> new DatePickerDialog(AddCourse.this, startDatePicker, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        editEndDate.setOnClickListener(view -> new DatePickerDialog(AddCourse.this, endDatePicker, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        // Status Spinner
        statusDropdown = findViewById(R.id.statusSpinner);
        Status[] items = new Status[] {Status.PLAN_TO_TAKE, Status.IN_PROGRESS, Status.COMPLETED, Status.DROPPED};
        ArrayAdapter<Status> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        statusDropdown.setAdapter(statusAdapter);

        mentorViewModel = new ViewModelProvider(this).get(MentorViewModel.class);
        // Mentor TextView
        editMentor = findViewById(R.id.textEditMentor);
        editMentor.setOnClickListener(view -> showCreateMentorDialog());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateLabel(TextView editText) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(myCalendar.getTime()));
    }

    private void showCreateMentorDialog() {
        CreateMentorDialogFragment dialog = new CreateMentorDialogFragment(this);
        dialog.show(getSupportFragmentManager(), "createMentorDialog");
        mentorId = mentorViewModel.getMaxId();
    }

    @Override
    public void onCreateMentorDialogPositiveClick(CreateMentorDialogFragment dialog) {
        String mentorName = dialog.getMentorNameEditText().getText().toString();
        String mentorPhone = dialog.getMentorPhoneEditText().getText().toString();
        String mentorEmail = dialog.getMentorEmailEditText().getText().toString();

        mentor = new Mentor(++mentorId, mentorName, mentorPhone, mentorEmail, courseId);
        editMentor.setText(mentor.toString());
        dialog.dismiss();
    }

    public void saveNewCourse(View view) throws ParseException {
        if (courseTitle.getText().toString().trim().isEmpty() ||
            editStartDate.getText().toString().trim().isEmpty() ||
            editEndDate.getText().toString().trim().isEmpty() ||
                (mentor == null))
        {
            Toast.makeText(this,
                    "Please include a course title, start date, end date, course status, and mentor.", Toast.LENGTH_LONG).show();
            return;
        }

        String format = "MM/dd/yy";
        String title = courseTitle.getText().toString();
        Date startDate = new SimpleDateFormat(format, Locale.US).parse(editStartDate.getText().toString());
        Date endDate = new SimpleDateFormat(format, Locale.US).parse(editEndDate.getText().toString());
        Status status = (Status) statusDropdown.getSelectedItem();

        Toast.makeText(this,
                "Adding course, please wait...", Toast.LENGTH_SHORT).show();

        Course newCourse = new Course(courseId, title, startDate, endDate, status, mentor, termId);
        courseViewModel.insert(newCourse);
        mentorViewModel.insert(mentor);
        this.finish();
        Toast.makeText(this,
                title + " added to this term.", Toast.LENGTH_LONG).show();
    }
}