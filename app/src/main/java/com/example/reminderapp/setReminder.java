package com.example.reminderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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
import java.util.Random;

import static java.util.Calendar.AM;
import static java.util.Calendar.PM;
import static java.util.Calendar.getInstance;

public class setReminder extends FragmentActivity implements CustomRepModeFragment.DialogListener {


    TextView td,tt,num_d;
    EditText title,desc;
    Button save;
    RadioGroup rg;
    RadioButton rb;
    RemDatabase rdb;
    String repMode;
    int req_code;
    int num_days;

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
        num_d = findViewById(R.id.num_days_sr);
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

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rb = findViewById(i);
                if(rb != null)
                {
                    repMode = rb.getText().toString();
                }
                if(repMode.equals("Custom"))
                {
                    DialogFragment dialogFragment = new CustomRepModeFragment();

                    Bundle bundle = new Bundle();
                    bundle.putBoolean("notAlertDialog", true);

                    dialogFragment.setArguments(bundle);

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);
                    dialogFragment.show(ft, "dialog");

                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                req_code = Integer.parseInt(calendar.getTimeInMillis()/(1000*1000*1000) + new Random().nextInt(100) +  "" );
                //req_code = 1221;
                Reminder rem = new Reminder();
                rem.setTitle(title.getText().toString());
                rem.setDescription(desc.getText().toString());
                rem.setDate(td.getText().toString());
                rem.setTime(tt.getText().toString());
                //repMode = getRbString();
                if(repMode.equals("Custom"))
                    repMode = "Repeat after every "+num_days+" days";
                rem.setRepeatMode(repMode);
                rem.setReqCode(req_code);
                rdb.insertReminderData(rem);

                startAlert();

                Toast.makeText(setReminder.this, "Reminder is set!", Toast.LENGTH_SHORT).show();
                Intent i1 = new Intent(setReminder.this,showReminders.class);
                startActivity(i1);
            }
        });
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

        //Toast.makeText(this, ""+calendar.getTime(), Toast.LENGTH_LONG).show();

        long s1 = calendar.getTimeInMillis(); //set Time
        long repSec = 0;
        if(repMode.equals("Daily"))
        {
            repSec = 1000 * 60 * 60 * 24;
        }
        else if(repMode.equals("Custom"))
        {
            repSec = num_days * 1000 * 60 * 60 * 24;
        }
        Intent myIntent = new Intent(setReminder.this , MyBroadcastReceiver.class ) ;
        myIntent.putExtra("title_",title.getText().toString());
        myIntent.putExtra("desc_",desc.getText().toString());
        AlarmManager alarmManager = (AlarmManager) this.getSystemService( ALARM_SERVICE ) ;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(),req_code,myIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        if(repSec != 0)
            alarmManager.setRepeating(AlarmManager. RTC_WAKEUP , s1 , repSec , pendingIntent) ;
        else
            alarmManager.set(AlarmManager. RTC_WAKEUP , s1 , pendingIntent);

    }
    public void onFinishEditDialog(String inputText) {

        if (TextUtils.isEmpty(inputText)) {
            num_days = 0;
        } else
            num_days = Integer.parseInt(inputText);
        num_d.setVisibility(View.VISIBLE);
        num_d.setText("Repeat after "+num_days+" days");
    }

}
