package com.example.reminderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.reminderapp.Adapters.RemAdapter;
import com.example.reminderapp.Database.RemDatabase;
import com.example.reminderapp.Model.Reminder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class showReminders extends AppCompatActivity {
    RecyclerView recyclerView;
    RemDatabase rdb;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_reminders);
        recyclerView = findViewById(R.id.recycler_view);
        rdb = new RemDatabase(showReminders.this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        ArrayList<Reminder> remArrayList = rdb.showReminderData();

        RemAdapter remAdapter = new RemAdapter(showReminders.this,remArrayList);
        recyclerView.setAdapter(remAdapter);

        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(showReminders.this,setReminder.class);
                startActivity(i1);
            }
        });

    }
}
