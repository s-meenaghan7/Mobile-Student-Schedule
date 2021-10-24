package com.example.studentschedulerv2.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentschedulerv2.Adapters.AssessmentAdapter;
import com.example.studentschedulerv2.Entities.Course;
import com.example.studentschedulerv2.Entities.Mentor;
import com.example.studentschedulerv2.R;
import com.example.studentschedulerv2.Utilities.CreateMentorDialogFragment;
import com.example.studentschedulerv2.Utilities.DeleteCourseDialogFragment;
import com.example.studentschedulerv2.Utilities.MyReceiver;
import com.example.studentschedulerv2.Utilities.Status;
import com.example.studentschedulerv2.ViewModel.AssessmentViewModel;
import com.example.studentschedulerv2.ViewModel.CourseViewModel;
import com.example.studentschedulerv2.ViewModel.MentorViewModel;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CourseDetails extends AppCompatActivity
                           implements DeleteCourseDialogFragment.DeleteCourseDialogListener,
                                      CreateMentorDialogFragment.CreateMentorDialogListener {

    private AlarmManager alarmManager;
    private Course currentCourse;
    private Mentor currentMentor;
    private AssessmentViewModel assessmentViewModel;
    private CourseViewModel courseViewModel;
    private MentorViewModel mentorViewModel;

    private EditText courseTitle;
    private final Calendar myCalendar = Calendar.getInstance();
    private TextView editStartDate;
    private DatePickerDialog.OnDateSetListener startDatePicker;

    private TextView editEndDate;
    private DatePickerDialog.OnDateSetListener endDatePicker;

    private Spinner statusSpinner;
    private TextView editMentor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // get selected course from previous activity
        Intent i = getIntent();
        currentCourse = new Course(i.getIntExtra("courseId", -1),
                                   i.getStringExtra("title"),
                            (Date) i.getSerializableExtra("startDate"),
                            (Date) i.getSerializableExtra("endDate"),
                          (Status) i.getSerializableExtra("status"),
                                   i.getParcelableExtra("mentor"),
                                   i.getIntExtra("termId", -1));

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        // assign and prepopulate fields
        courseTitle = findViewById(R.id.addCourseTitle);
        courseTitle.setText(currentCourse.getTitle());

        editStartDate = findViewById(R.id.addCourseStartDate);
        editEndDate = findViewById(R.id.addCourseEndDate);
        String myFormat = "MM/dd/yy";
        String formattedStart = new SimpleDateFormat(myFormat, Locale.US).format(currentCourse.getStartDate());
        String formattedEnd = new SimpleDateFormat(myFormat, Locale.US).format(currentCourse.getEndDate());
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

        editStartDate.setOnClickListener(view -> new DatePickerDialog(CourseDetails.this, startDatePicker, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        editEndDate.setOnClickListener(view -> new DatePickerDialog(CourseDetails.this, endDatePicker, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        // Status spinner
        statusSpinner = findViewById(R.id.statusSpinner);
        Status[] items = new Status[] {Status.PLAN_TO_TAKE, Status.IN_PROGRESS, Status.COMPLETED, Status.DROPPED};
        ArrayAdapter<Status> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        statusSpinner.setAdapter(statusAdapter);
        statusSpinner.setSelection(statusAdapter.getPosition(currentCourse.getStatus()));

        // Mentor TextView
        mentorViewModel = new ViewModelProvider(this).get(MentorViewModel.class);
        currentMentor = currentCourse.getMentor();
        editMentor = findViewById(R.id.textEditMentor);
        editMentor.setOnClickListener(view -> showCreateMentorDialog());
        editMentor.setText(currentMentor.toString());

        // RecyclerView setup
        RecyclerView recyclerView = findViewById(R.id.assessment_recyclerview);
        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(new AssessmentAdapter.AssessmentDiff(), CourseDetails.this);
        recyclerView.setAdapter(assessmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        assessmentViewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);
        assessmentViewModel.getAssociatedAssessments(currentCourse).observe(this, assessmentAdapter::setAssessments);

    }

    private void showCreateMentorDialog() {
        CreateMentorDialogFragment dialog = new CreateMentorDialogFragment(this);
        dialog.show(getSupportFragmentManager(), "createMentorDialog");

        dialog.getMentorNameEditText().setText(currentMentor.getName());
        dialog.getMentorEmailEditText().setText(currentMentor.getEmail());
        dialog.getMentorPhoneEditText().setText(currentMentor.getPhoneNumber());
    }

    @Override
    public void onCreateMentorDialogPositiveClick(CreateMentorDialogFragment dialog) {
        String mentorName = dialog.getMentorNameEditText().getText().toString();
        String mentorPhone = dialog.getMentorPhoneEditText().getText().toString();
        String mentorEmail = dialog.getMentorEmailEditText().getText().toString();

        currentMentor = new Mentor(currentMentor.getId(), mentorName, mentorPhone, mentorEmail, currentMentor.getCourse_id());
        // possibly need to set currentMentor.getId() and currentMentor.getCourse_id() to global variables?
        editMentor.setText(currentMentor.toString());
        dialog.dismiss();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course_details, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.course_notification_start:

                String startMessage = "Course " + currentCourse.getTitle() + " starts today.";
                long startTime = currentCourse.getStartDate().getTime();
                createCourseNotification(alarmManager, startMessage, startTime);

                Toast.makeText(getApplicationContext(),
                        "Start date notification set", Toast.LENGTH_LONG).show();
                return true;
            case R.id.course_notification_end:

                String endMessage = "Course " + currentCourse.getTitle() + " ends today.";
                long endTime = currentCourse.getEndDate().getTime();
                createCourseNotification(alarmManager, endMessage, endTime);

                Toast.makeText(getApplicationContext(),
                        "End date notification set", Toast.LENGTH_LONG).show();
                return true;
            case R.id.mentorDetails:
                mentorDialogBuilder().create().show();
                return true;
            case R.id.addNote:
                Intent i = new Intent(CourseDetails.this, AddNote.class);
                i.putExtra("courseId", currentCourse.getId());
                startActivity(i);
                return true;
            case R.id.courseNotesList:
                Intent j = new Intent(CourseDetails.this, CourseNotesList.class);
                j.putExtra("courseId", currentCourse.getId());
                startActivity(j);
                return true;
            case R.id.deleteCourse:
                showDeleteCourseDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createCourseNotification(AlarmManager alarmManager, String message, long trigger) {
        final int notificationId = (int) System.currentTimeMillis();

        Intent intent = new Intent(CourseDetails.this, MyReceiver.class);
        intent.putExtra("title", "Student Scheduling");
        intent.putExtra("content", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(CourseDetails.this,
                notificationId, intent, 0);

        alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);
    }

    private AlertDialog.Builder mentorDialogBuilder() {
        return new AlertDialog.Builder(this)
                .setTitle("Mentor Details")
                .setMessage("Name: " + currentCourse.getMentor().getName() + "\n" +
                            "Phone: " + currentCourse.getMentor().getPhoneNumber() + "\n" +
                            "Email: " + currentCourse.getMentor().getEmail())
                .setNegativeButton("OK", null);
    }

    private void showDeleteCourseDialog() {
        DialogFragment dialog = new DeleteCourseDialogFragment();
        dialog.show(getSupportFragmentManager(), "deleteCourseDialog");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        courseViewModel.delete(currentCourse);
        dialog.dismiss();
        Toast.makeText(getApplicationContext(),
                "Course deleted successfully.", Toast.LENGTH_LONG).show();
        this.finish();
    }

    private void updateLabel(TextView editText) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(myCalendar.getTime()));
    }

    public void updateCourse(View view) throws ParseException {
        if (courseTitle.getText().toString().trim().isEmpty() ||
            editStartDate.getText().toString().trim().isEmpty() ||
            editEndDate.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this,
                    "Please include a course title, start date, end date, course status, and mentor.", Toast.LENGTH_LONG).show();
            return;
        }

        String format = "MM/dd/yy";
        String title = courseTitle.getText().toString();
        Date startDate = new SimpleDateFormat(format, Locale.US).parse(editStartDate.getText().toString());
        Date endDate = new SimpleDateFormat(format, Locale.US).parse(editEndDate.getText().toString());
        Status status = (Status) statusSpinner.getSelectedItem();

        Toast.makeText(this,
                "Updating, please wait...", Toast.LENGTH_SHORT).show();

        Course updatedCourse = new Course(currentCourse.getId(), title, startDate, endDate, status, currentMentor, currentCourse.getTermId());
        courseViewModel.update(updatedCourse);
        mentorViewModel.update(currentMentor);
        this.finish();
        Toast.makeText(this,
                title + " updated successfully.", Toast.LENGTH_LONG).show();
    }

    public void addAssessment(View view) {
        Intent intent = new Intent(CourseDetails.this, AddAssessment.class);
        intent.putExtra("courseId", currentCourse.getId());
        startActivity(intent);
    }

}