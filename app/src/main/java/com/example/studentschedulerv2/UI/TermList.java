package com.example.studentschedulerv2.UI;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.studentschedulerv2.Adapters.TermAdapter;
import com.example.studentschedulerv2.R;
import com.example.studentschedulerv2.ViewModel.TermViewModel;

import java.util.Objects;

public class TermList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // setup recyclerview
        RecyclerView recyclerView = findViewById(R.id.term_recyclerview);
        final TermAdapter termAdapter = new TermAdapter(new TermAdapter.TermDiff(), TermList.this);
        recyclerView.setAdapter(termAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TermViewModel termViewModel = new ViewModelProvider(this).get(TermViewModel.class);

        termViewModel.getAllTerms().observe(this, termAdapter::setTerms);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addTerm(View view) {
        Intent intent = new Intent(TermList.this, AddTerm.class);
        startActivity(intent);
    }
}