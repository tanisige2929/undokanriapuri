package com.example.ExerciseApplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.*;
import java.time.*;

public class Calendarfunction extends AppCompatActivity {
    Calendar calendar;
    private int dateNow;
    private int yearNow;
    private int monthNow;
    private int youbinow;
    private int day;
    private int year;
    private int month;
    private int youbi;
    private int monthlastday;
    private String yearmonth = "";
    private int page;
    private Intent intent;

    Calendarfunction(int page, TextView[] text) {
        this.page = page;
        //this.text = text;
        calendar = Calendar.getInstance();
        youbinow = calendar.get(Calendar.DAY_OF_WEEK);
        dateNow = calendar.get(Calendar.DATE);
        yearNow = calendar.get(Calendar.YEAR);
        monthNow = calendar.get(Calendar.MONTH);
        System.out.println("month:" + month);
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
        int j = 1;
        for(i = 0; i < youbi - 1; i++) {
            //System.out.println("test:" + i);
            text[i].setText(" ");//一日まで空白で埋める
        }
        monthlastday = calendar.getActualMaximum(calendar.DAY_OF_MONTH); //当月の最大日数取得
        int k = j;
        int l = i;
        for( ; j < monthlastday + 1; i++) {
            text[i].setText("" + j);//日付セット開始
            if(month == monthNow && year == yearNow) {
                if(j == dateNow) {
                    //t[i].setTypeface(Typeface.DEFAULT_BOLD);
                    text[i].setBackgroundColor(Color.YELLOW);//当日を黄色に塗る
                }
            }
            text[i].setClickable(true);
            j++;
        }
        title.setText(year + "年" + (month + 1) + "月");
        String monthstring = "" + (month + 1);
        if(monthstring.length() == 1) monthstring = "0" + monthstring;
        yearmonth = "" + year + "-" + monthstring + "-";

        return yearmonth;
    }
}
