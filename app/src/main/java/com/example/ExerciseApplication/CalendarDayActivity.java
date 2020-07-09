package com.example.ExerciseApplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CalendarDayActivity extends AppCompatActivity {

    private String DayTitle;
    private Intent intent;
    private String year;
    private String month;
    private String day;
    private Date date;
    private ArrayList data = new ArrayList<>();
    private DatabaseHelper helper;
    private SQLiteDatabase db;
    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_day);

        //helper = new DatabaseHelper(CalendarDayActivity.this);
        //db = helper.getWritableDatabase();

        TextView title = (TextView)findViewById(R.id.daymenutitle);
        TextView nomenu = (TextView)findViewById(R.id.NoMenu);
        nomenu.setVisibility(View.INVISIBLE);
        //if(nomenu.)
        intent = getIntent();
        DayTitle = intent.getStringExtra("key");
        year = DayTitle.substring(0, 4);
        month = DayTitle.substring(5, 7);
        day = DayTitle.substring(8, 10);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/mm/dd", Locale.JAPAN);
        try {
            date =  sdf.parse(DayTitle);
        }catch(ParseException e) {
            e.printStackTrace();
        }
        //date = Date.valueOf(DayTitle);
        //System.out.println("aiueo" + DayTitle + year + " " + month + " " + day);
        title.setText(month + "月" + day + "日のメニュー");//ここまで初期画面

        /*try {
            String sqlSelect = "SELECT * FROM ExerciseMenu WHERE DATE = ?";
            SQLiteStatement stmt = db.compileStatement(sqlSelect);
            //stmt.e
        }*/
        data.add("うんこ");

        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, data);
        ListView listView = (ListView)findViewById(R.id.listview);
        listView.setAdapter(adapter);

        FloatingActionButton plusButton = findViewById(R.id.plusbutton);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Fragment fragment = getSupportFragmentManager();
                CalendarDialogFragment1 dialogfragment1 = new CalendarDialogFragment1(CalendarDayActivity.this);
                dialogfragment1.show(getSupportFragmentManager(), "CalendarDialogFragment1");
                flag = dialogfragment1.getFlag();
                System.out.println(flag);
                if(flag == 1) {
                    CalendarDialogFragment2 dialogfragment2 = new CalendarDialogFragment2(CalendarDayActivity.this);
                    dialogfragment2.show(getSupportFragmentManager(), "CalendarDialogFragment2");
                }
            }
        });
    }
    /*private class ListItemClickListener implements AdapterView.OnItemClickListener {
    }*/
}