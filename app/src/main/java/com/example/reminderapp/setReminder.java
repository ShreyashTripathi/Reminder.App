package com.example.reminderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.reminderapp.Database.RemDatabase;
import com.example.reminderapp.Model.Reminder;

import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.AM;
import static java.util.Calendar.PM;
import static java.util.Calendar.getInstance;

public class setReminder extends AppCompatActivity {


    TextView td,tt;
    EditText title,desc;
    Button save;
    RadioGroup rg;
    RadioButton rb;
    RemDatabase rdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_reminder);

        td = findViewById(R.id._date_text);
        tt = findViewById(R.id._time_text);
        title = findViewById(R.id._title);
        desc = findViewById(R.id._desc);
        save = findViewById(R.id._save);
        rg = findViewById(R.id.rep_mode);

        rdb = new RemDatabase(setReminder.this);

        td.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(),"Date Picker");
            }
        });

        tt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(),"TimePicker");
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reminder rem = new Reminder();
                rem.setTitle(title.getText().toString());
                rem.setDescription(desc.getText().toString());
                rem.setDate(td.getText().toString());
                rem.setTime(tt.getText().toString());
                rem.setRepeatMode(getRbString());
                rdb.insertReminderData(rem);

                startAlert();

                Intent i1 = new Intent(setReminder.this,showReminders.class);
                startActivity(i1);
            }
        });
    }

    public String getRbString()
    {
        int selectedId=rg.getCheckedRadioButtonId();
        String rep_mode = "";
        rb = findViewById(selectedId);

        if(rb != null)
            rep_mode = rb.getText().toString();

        return rep_mode;
    }
    public void startAlert()
    {
        SharedPreferences d_prefs = setReminder.this.getSharedPreferences(
                "ReminderDate", Context.MODE_PRIVATE);
        SharedPreferences t_prefs = setReminder.this.getSharedPreferences(
                "ReminderTime", Context.MODE_PRIVATE);

        int day = d_prefs.getInt("Day",new Date().getDay());
        int month = d_prefs.getInt("Month",new Date().getMonth());
        int year = d_prefs.getInt("Year",new Date().getYear());

        int min = t_prefs.getInt("Minutes",0);
        int hr = t_prefs.getInt("Hour",0);
        month = month - 1;

        Calendar calendar = Calendar.getInstance () ;
        calendar.set(Calendar.SECOND,0) ;
        calendar.set(Calendar.MINUTE,min ) ;
        if(hr < 12)
            calendar.set(Calendar.HOUR , hr ) ;
        else
            calendar.set(Calendar.HOUR , (hr-12)) ;
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,day) ;
        calendar.set(Calendar.YEAR,year);
        if(hr < 12)
            calendar.set(Calendar.AM_PM,AM);
        else
            calendar.set(Calendar.AM_PM,PM);

        Toast.makeText(this, ""+calendar.getTime(), Toast.LENGTH_LONG).show();

        //Calendar c2 = Calendar.getInstance();
        long s1 = calendar.getTimeInMillis();
        long s2 = System.currentTimeMillis();
        long diff = s1 - s2;
        Intent myIntent = new Intent(setReminder.this , MyNotifyService.class ) ;

        AlarmManager alarmManager = (AlarmManager) this.getSystemService( ALARM_SERVICE ) ;
        PendingIntent pendingIntent = PendingIntent. getService ( getApplicationContext(), 0 , myIntent , 0 ) ;

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP , diff,1000 * 60 * 60 * 24 , pendingIntent);

        //Toast.makeText(setReminder.this, ""+day+" / "+month+" / "+year+" || "+hr+" : "+min, Toast.LENGTH_LONG).show();

    }

}
