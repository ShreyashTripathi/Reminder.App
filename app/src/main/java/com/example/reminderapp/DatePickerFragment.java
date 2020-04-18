package com.example.reminderapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current date as the default date in the date picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ReminderDate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        i1 = i1+1;
        editor.putInt("Day", i2);
        editor.putInt("Month", i1);
        editor.putInt("Year", i);
        editor.commit();

        TextView tv = getActivity().findViewById(R.id._date_text);
        String stringOfDate = i2 + " / " + i1 + " / " + i;
        tv.setText(stringOfDate);

    }
}
