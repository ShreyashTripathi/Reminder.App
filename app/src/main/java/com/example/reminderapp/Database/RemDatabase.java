package com.example.reminderapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.reminderapp.Model.Reminder;

import java.util.ArrayList;

public class RemDatabase extends SQLiteOpenHelper{

    Context context;
    public RemDatabase(@Nullable Context context) {
        super(context, "rem_db", null, 1);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "Create table RemData(title,description,date,time,repeatMode)";
        sqLiteDatabase.execSQL(sql);
    }

    public void insertReminderData(Reminder r1)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title",r1.getTitle());
        cv.put("description",r1.getDescription());
        cv.put("date",r1.getDate());
        cv.put("time",r1.getTime());
        cv.put("repeatMode",r1.getRepeatMode());
        db.insert("RemData",null, cv);
        Toast.makeText(context, "Data Inserted!", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<Reminder> showReminderData()
    {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "Select * from RemData";
        Cursor cursor = db.rawQuery(sql,null);
        ArrayList<Reminder> userArrayList = new ArrayList<>();
        int flag_2 = 0;
        while(cursor.moveToNext())
        {
            flag_2 = 1;
            Reminder r1 = new Reminder();
            r1.setTitle(cursor.getString(0));
            r1.setDescription(cursor.getString(1));
            r1.setDate(cursor.getString(2));
            r1.setTime(cursor.getString(3));
            r1.setRepeatMode(cursor.getString(4));
            userArrayList.add(r1);
        }
        if(flag_2 == 0)
        {
            Toast.makeText(context, "No reminders found!", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        closeDatabase(db);
        return  userArrayList;
    }

    public void deleteReminder(String title){
        SQLiteDatabase db = getWritableDatabase();
        db.delete("RemData","title=?",new String[]{title});
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    private void closeDatabase(SQLiteDatabase db) {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
