package com.example.ExerciseApplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Calendarfunction cl;
    private TextView[] textcl = new TextView[42];
    private FragmentStatePagerAdapter f;
    private ViewPager viewpager;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //画面生成時
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavController navcontroller = Navigation.findNavController(this, R.id.nav_host_fragment);
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(navigation, navcontroller);
        /*viewpager = findViewById(R.id.pager);
        viewpager.setAdapter(new FragmentCalendarAdapter(getSupportFragmentManager(), MainActivity.this));
        viewpager.setCurrentItem(10);*/
        //nowScreenToDo();
    }

    private void nowScreenToDo() { //現在ToDoの時

        //todo機能
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupWithNavController(R.id.bottom_navigation);
        /*final ListView listViewue = findViewById(R.id.todaymenulist);
        ListView listViewsita = findViewById(R.id.todaycompletemenulist);
        final todoFunction todaymenu = new todoFunction(MainActivity.this, listViewue, listViewsita);
        todaymenu.todoDatabase(1, -1);
        //listViewue.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                todaymenu.todoDatabase(2, position);
                todaymenu.adapterChange(parent.getItemAtPosition(position).toString());
                //System.out.println(parent.getItemAtPosition(position).toString());
                //adapter.notifyDataSetChanged();
            }
        });

        ImageButton button2 = findViewById(R.id.Calendar);
        ImageButton button3 = findViewById(R.id.Timer);*/
        /*button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //カレンダー画面に遷移
                setContentView(R.layout.calendar);
                //viewpager = (ViewPager) findViewById(R.id.pager);
                //viewpager.setAdapter(new FragmentCalendarAdapter(getSupportFragmentManager()));
                nowScreenCalendar();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //timer画面に遷移
                setContentView(R.layout.timer);
                nowScreenCount();
            }
        });*/
    }
    private void nowScreenCalendar() { //現在カレンダーの時
        ImageButton button1 = findViewById(R.id.ToDo);
        ImageButton button3 = findViewById(R.id.Timer);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //ToDo画面に遷移
                setContentView(R.layout.activity_main);
                nowScreenToDo();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //カウント画面に遷移
                setContentView(R.layout.timer);
                nowScreenCount();
            }
        });

        //if(!calenderfirst) {//カレンダー画面に初めて来たか？
            int clId;
            String calendarViewname;


    }
    private void nowScreenCount() { //現在Countの時
        ImageButton button1 = findViewById(R.id.ToDo);
        ImageButton button2 = findViewById(R.id.Calendar);

        ImageButton stopwatch = findViewById(R.id.stopwatchbutton);
        ImageButton alarm = findViewById(R.id.alarmbuttton);

        stopwatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, stopWatchActivity.class);
                startActivity(intent);
            }
        });
        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
                startActivity(intent);
            }
        });
        /*timerText = findViewById(R.id.swcount);
        System.out.println(timerText.getText());
        Button start_stop = findViewById(R.id.swstartorstop);
        start_stop.setOnClickListener(new View.OnClickListener() { //スタートorストップを押したとき
            @Override
            public void onClick(View v) {
                Button start_stop = findViewById(R.id.swstartorstop);
                if(timerNow) {
                    startTimer(start_stop);
                }
                else {
                    pauseTimer(start_stop);
                }
            }
        });

        Button reset = findViewById(R.id.swreset);
        reset.setOnClickListener(new View.OnClickListener() { //リセットボタンを押したとき
            @Override
            public void onClick(View v) {
                Button start_stop = findViewById(R.id.swstartorstop);
                resetTimer(start_stop);
            }
        });*/

        button1.setOnClickListener(new View.OnClickListener() { //ボタン１を押したとき
            @Override
            public void onClick(View v) { //ToDo画面に遷移
                setContentView(R.layout.activity_main);
                nowScreenToDo();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() { //ボタン2を押したとき
            @Override
            public void onClick(View v) { //カレンダー画面に遷移
                setContentView(R.layout.calendar);
                nowScreenCalendar();
            }
        });
    }

}
