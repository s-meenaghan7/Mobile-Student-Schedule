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

import com.example.studentschedulerv2.Entities.Assessment;
import com.example.studentschedulerv2.R;
import com.example.studentschedulerv2.Utilities.AssessmentType;
import com.example.studentschedulerv2.ViewModel.AssessmentViewModel;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddAssessment extends AppCompatActivity {
    private AssessmentViewModel assessmentViewModel;

    private int assessmentId;
    private int courseId;
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
        setContentView(R.layout.activity_add_assessment);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        assessmentViewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);

        assessmentId = assessmentViewModel.getMaxId();
        courseId = getIntent().getIntExtra("courseId", -1);
        assessmentTitle = findViewById(R.id.addAssessmentTitle);
        editStartDate = findViewById(R.id.addAssessmentStartDate);
        editEndDate = findViewById(R.id.addAssessmentEndDate);

        // Implementing DatePickers
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

        // Start/End date clickListeners
        editStartDate.setOnClickListener(view -> new DatePickerDialog(AddAssessment.this, startDatePicker, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        editEndDate.setOnClickListener(view -> new DatePickerDialog(AddAssessment.this, endDatePicker, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        // Assessment Type spinner
        assessmentTypeSpinner = findViewById(R.id.assessmentTypeSpinner);
        AssessmentType[] assessmentTypes = new AssessmentType[] {AssessmentType.OBJECTIVE, AssessmentType.PERFORMANCE};
        ArrayAdapter<AssessmentType> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, assessmentTypes);
        assessmentTypeSpinner.setAdapter(typeAdapter);

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

    public void saveNewAssessment(View view) throws ParseException {
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

        Assessment newAssessment = new Assessment(++assessmentId, title, startDate, endDate, type, courseId);
        assessmentViewModel.insert(newAssessment);
        this.finish();
        Toast.makeText(this,
                title + " added to this course.", Toast.LENGTH_LONG).show();
    }
}