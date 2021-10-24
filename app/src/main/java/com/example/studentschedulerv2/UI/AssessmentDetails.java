package com.example.studentschedulerv2.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
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

import com.example.studentschedulerv2.Entities.Assessment;
import com.example.studentschedulerv2.R;
import com.example.studentschedulerv2.Utilities.AssessmentType;
import com.example.studentschedulerv2.Utilities.DeleteAssessmentDialogFragment;
import com.example.studentschedulerv2.Utilities.MyReceiver;
import com.example.studentschedulerv2.ViewModel.AssessmentViewModel;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AssessmentDetails extends AppCompatActivity
                               implements DeleteAssessmentDialogFragment.DeleteAssessmentDialogListener {

    private AlarmManager alarmManager;
    private AssessmentViewModel assessmentViewModel;
    private Assessment currentAssessment;

    private EditText assessmentTitle;
    private final Calendar myCalendar = Calendar.getInstance();
    private TextView editStartDate;
    private DatePickerDialog.OnDateSetListener startDatePicker;

    private TextView editEndDate;
    private DatePickerDialog.OnDateSetListener endDatePicker;

    private Spinner assessmentTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_details);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        assessmentViewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);
        Intent i = getIntent();
        currentAssessment = new Assessment(i.getIntExtra("assessmentId", -1),
                                            i.getStringExtra("title"),
                                     (Date) i.getSerializableExtra("startDate"),
                                     (Date) i.getSerializableExtra("endDate"),
                           (AssessmentType) i.getSerializableExtra("type"),
                                            i.getIntExtra("courseId", -1));

        assessmentTitle = findViewById(R.id.addAssessmentTitle);
        assessmentTitle.setText(getIntent().getStringExtra("title"));

        editStartDate = findViewById(R.id.addAssessmentStartDate);
        editEndDate = findViewById(R.id.addAssessmentEndDate);
        Date start = (Date) getIntent().getSerializableExtra("startDate");
        Date end = (Date) getIntent().getSerializableExtra("endDate");
        String myFormat = "MM/dd/yy";
        String formattedStart = new SimpleDateFormat(myFormat, Locale.US).format(start);
        String formattedEnd = new SimpleDateFormat(myFormat, Locale.US).format(end);
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

        editStartDate.setOnClickListener(view -> new DatePickerDialog(AssessmentDetails.this, startDatePicker, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        editEndDate.setOnClickListener(view -> new DatePickerDialog(AssessmentDetails.this, endDatePicker, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        assessmentTypeSpinner = findViewById(R.id.assessmentTypeSpinner);
        AssessmentType[] types = new AssessmentType[] {AssessmentType.OBJECTIVE, AssessmentType.PERFORMANCE};
        ArrayAdapter<AssessmentType> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types);
        assessmentTypeSpinner.setAdapter(typeAdapter);
        assessmentTypeSpinner.setSelection(typeAdapter.getPosition(currentAssessment.getType()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_assessment_details, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.assessment_notification_start:

                String startMessage = "Assessment " + currentAssessment.getTitle() + " starts today.";
                long startTime = currentAssessment.getStartDate().getTime();
                createAssessmentNotification(alarmManager, startMessage, startTime);

                Toast.makeText(this,
                        "Start date notification set", Toast.LENGTH_LONG).show();
                return true;
            case R.id.assessment_notification_end:

                String endMessage = "Assessment " + currentAssessment.getTitle() + " ends today.";
                long endTime = currentAssessment.getEndDate().getTime();
                createAssessmentNotification(alarmManager, endMessage, endTime);

                Toast.makeText(this,
                        "End date notification set", Toast.LENGTH_LONG).show();
                return true;
            case R.id.deleteAssessment:
                showDeleteAssessmentDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createAssessmentNotification(AlarmManager alarmManager, String message, long trigger) {
        final int notificationId = (int) System.currentTimeMillis();

        Intent intent = new Intent(AssessmentDetails.this, MyReceiver.class);
        intent.putExtra("title", "Student Scheduling");
        intent.putExtra("content", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(AssessmentDetails.this,
                notificationId, intent, 0);

        alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);
    }

    private void showDeleteAssessmentDialog() {
        DialogFragment dialog = new DeleteAssessmentDialogFragment();
        dialog.show(getSupportFragmentManager(), "deleteAssessmentDialog");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        assessmentViewModel.delete(currentAssessment);
        dialog.dismiss();
        Toast.makeText(getApplicationContext(),
                currentAssessment.getTitle() + " deleted successfully.", Toast.LENGTH_LONG).show();
        this.finish();
    }

    private void updateLabel(TextView editText) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(myCalendar.getTime()));
    }

    public void updateAssessment(View view) throws ParseException {
        if (assessmentTitle.getText().toString().trim().isEmpty() ||
            editStartDate.getText().toString().trim().isEmpty() ||
            editEndDate.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this,
                    "Please include assessment title, start date, end date, and type.", Toast.LENGTH_LONG).show();
            return;
        }

        String format = "MM/dd/yy";
        String title = assessmentTitle.getText().toString();
        Date startDate = new SimpleDateFormat(format, Locale.US).parse(editStartDate.getText().toString());
        Date endDate = new SimpleDateFormat(format, Locale.US).parse(editEndDate.getText().toString());
        AssessmentType type = (AssessmentType) assessmentTypeSpinner.getSelectedItem();

        Assessment updatedAssessment = new Assessment(currentAssessment.getId(), title, startDate, endDate, type, currentAssessment.getCourseId());
        assessmentViewModel.update(updatedAssessment);
        this.finish();
        Toast.makeText(this,
                title + " updated successfully.", Toast.LENGTH_LONG).show();
    }
}