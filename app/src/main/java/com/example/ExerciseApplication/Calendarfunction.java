package com.example.ExerciseApplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import java.util.*;

public class Calendarfunction extends AppCompatActivity {
    private Calendar calendar;
    private int yearNow;
    private int monthNow;
    private int dateNow;
    private int day;
    private int year;
    private int month;
    private int youbi;
    private int monthlastday;
    private String yearmonth = "";
    private String dayString;
    private int page;
    private Intent intent;
    private Context context;
    private DatabaseHelper helper;
    private SQLiteDatabase db;
    private TextView[] text;
    private int position, size;

    Calendarfunction(int page, Context context) {
        this.context = context;
        this.page = page;
        calendar = Calendar.getInstance();
        yearNow = calendar.get(Calendar.YEAR);
        monthNow = calendar.get(Calendar.MONTH);
        dateNow = calendar.get(Calendar.DATE);
        if(page - 10 < 0) {
            calendar.set(Calendar.MONTH, monthNow - (page - 10) * -1);
        }
        else if(page - 10 > 0) {
            calendar.set(Calendar.MONTH, monthNow + (page - 10));
        }
        calendar.set(Calendar.DATE, 1);

        day = calendar.get(Calendar.DATE);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        youbi = calendar.get(Calendar.DAY_OF_WEEK);
    }

    public String CalendarSet(TextView[] text, TextView title) {
        int i;
        this.text = text;
        title.setText(year + "年" + (month + 1) + "月");
        String monthstring = "" + (month + 1);
        if(monthstring.length() == 1) monthstring = "0" + monthstring;
        yearmonth = "" + year + "-" + monthstring + "-";
        for(i = 0; i < youbi - 1; i++) {
            text[i].setText(" ");//一日まで空白で埋める
        }
        monthlastday = calendar.getActualMaximum(calendar.DAY_OF_MONTH); //当月の最大日数取得
        int j = 1;
        position = i;
        size = monthlastday + 1;
        for( ; j < size; i++) {
            text[i].setText("" + j);//日付セット開始
            dayString = "" + j;
            if(dayString.length() == 1) dayString = "0" + dayString;
            if(year == yearNow && month == monthNow) {
                if (j == dateNow) {
                    text[i].setBackgroundColor(Color.YELLOW);//当日を黄色に塗る
                }
                else if(j < dateNow){
                    ArrayList<DATE> arrayList1 = Database(dayString, 1);
                    if(arrayList1.size() != 0) {
                        text[i].setBackgroundColor(Color.parseColor("#DDDDDD"));
                    }
                }
            }
            if(year <= yearNow && month < monthNow) {
                ArrayList<DATE> arrayList1 = Database(dayString, 1);
                if (arrayList1.size() != 0) {
                    text[i].setBackgroundColor(Color.parseColor("#DDDDDD"));
                }
            }
            if(yearNow <= year && monthNow <= month) {
                ArrayList<DATE> arrayList2 = Database(dayString, 2);
                System.out.println("dateNow " + dateNow + "j" + j);
                if((monthNow == month && dateNow <= j) || (monthNow < month)) {
                    if(arrayList2.size() != 0) {
                        text[i].setTextColor(Color.GREEN);
                    }
                }
            }
            text[i].setClickable(true);
            j++;
        }
        return yearmonth;
    }
    public ArrayList<DATE> Database(String day, int flag) {
        helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
        ArrayList<DATE> arrayList = new ArrayList<>();
        if(flag == 1) {
            try {
                String sql = "SELECT date, complete FROM ExerciseMenu WHERE date = '" + yearmonth + day + "'";
                Cursor cursor = db.rawQuery(sql, null);
                String date;
                int complete;
                while (cursor.moveToNext()) {
                    int idxNote;
                    idxNote = cursor.getColumnIndex("date");
                    date = cursor.getString(idxNote);
                    idxNote = cursor.getColumnIndex("complete");
                    complete = cursor.getInt(idxNote);
                    DATE d = new DATE(date, complete);
                    arrayList.add(d);
                }
            }
            finally {
                db.close();
            }
            return arrayList;
        }
        else {
            try {
                String sql = "SELECT date, complete FROM ExerciseMenu WHERE date = '" + yearmonth + day + "' AND complete = -1";
                System.out.println(yearmonth + day);
                Cursor cursor = db.rawQuery(sql, null);
                String date;
                int complete;
                while (cursor.moveToNext()) {
                    int idxNote;
                    idxNote = cursor.getColumnIndex("date");
                    date = cursor.getString(idxNote);
                    idxNote = cursor.getColumnIndex("complete");
                    complete = cursor.getInt(idxNote);
                    DATE d = new DATE(date, complete);
                    arrayList.add(d);
                }
            }
            finally {
                db.close();
            }
            return arrayList;
        }
    }
    public void calendareventset() {
        int j = 1;
        for(int i = position; j < size; i++) {
            dayString = "" + j;
            if(dayString.length() == 1) dayString = "0" + dayString;
            if(yearNow <= year && monthNow <= month) {
                ArrayList<DATE> arrayList2 = Database(dayString, 2);
                if((monthNow == month && dateNow <= j) || (monthNow < month)) {
                    if(arrayList2.size() != 0) {
                        text[i].setTextColor(Color.GREEN);
                    }
                }
            }
            j++;
        }
    }
}
class DATE {
    private String date;
    private int complete;
    DATE(String date, int complete) {
        this.date = date;
        this.complete = complete;
    }
}