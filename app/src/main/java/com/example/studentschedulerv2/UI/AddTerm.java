package com.example.studentschedulerv2.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentschedulerv2.Entities.Term;
import com.example.studentschedulerv2.R;
import com.example.studentschedulerv2.ViewModel.TermViewModel;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddTerm extends AppCompatActivity {
    private TermViewModel termViewModel;

    private int termId;
    private EditText termTitle;
    private final Calendar myCalendar = Calendar.getInstance();

    private TextView editStartDate;
    private DatePickerDialog.OnDateSetListener startDatePicker;

    private TextView editEndDate;
    private DatePickerDialog.OnDateSetListener endDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);

        termId = termViewModel.getMaxId();
        termTitle = findViewById(R.id.addTermTitle);
        editStartDate = findViewById(R.id.addTermStartDate);
        editEndDate = findViewById(R.id.addTermEndDate);

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

        editStartDate.setOnClickListener(view -> new DatePickerDialog(AddTerm.this, startDatePicker, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        editEndDate.setOnClickListener(view -> new DatePickerDialog(AddTerm.this, endDatePicker, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());
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

    public void saveNewTerm(View view) throws ParseException {
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

        Term newTerm = new Term(++termId, title, startDate, endDate);
        termViewModel.insert(newTerm);

        this.finish();
        Toast.makeText(this,
                title + " added successfully.", Toast.LENGTH_LONG).show();
    }
}