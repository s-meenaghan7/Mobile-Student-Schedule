package com.example.studentschedulerv2.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.studentschedulerv2.Entities.Mentor;
import com.example.studentschedulerv2.R;
import com.example.studentschedulerv2.ViewModel.MentorViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // programmatically created button
        final Button termListButton = new Button(this);
        termListButton.setText(R.string.terms_list_button_label);
        termListButton.setTextColor(this.getResources().getColor(R.color.white));
        termListButton.setBackgroundColor(this.getResources().getColor(R.color.purple_500));
        termListButton.setOnClickListener(this::toTermsList);

        LinearLayout buttonLayout = findViewById(R.id.buttonLayout);
        buttonLayout.addView(termListButton);

    }

    public void toTermsList(View view) {
        Intent intent = new Intent(MainActivity.this, TermList.class);
        startActivity(intent);
    }
}