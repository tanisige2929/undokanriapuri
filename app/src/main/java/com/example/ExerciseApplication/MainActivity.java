package com.example.ExerciseApplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private Calendarfunction cl;
    private TextView[] textcl = new TextView[42];
    private FragmentStatePagerAdapter f;
    private ViewPager viewpager;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //画面生成時
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo);
        nowScreenToDo();
    }

    private void nowScreenToDo() { //現在ToDoの時

        //todo機能
        final ListView listViewue = findViewById(R.id.todaymenulist);
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
        ImageButton button3 = findViewById(R.id.Timer);
        button2.setOnClickListener(new View.OnClickListener() {
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
        });
    }
    private void nowScreenCalendar() { //現在カレンダーの時
        ImageButton button1 = findViewById(R.id.ToDo);
        ImageButton button3 = findViewById(R.id.Timer);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //ToDo画面に遷移
                setContentView(R.layout.todo);
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
            /*for(int i = 0; i < textcl.length; i++) {
                calendarViewname = "c" + i;
                clId = getResources().getIdentifier(calendarViewname, "id", getPackageName());
                //textcl[i] = findViewById(clId); //ID紐づけ
            }*/
            viewpager = (ViewPager) findViewById(R.id.pager);
            viewpager.setAdapter(new FragmentCalendarAdapter(getSupportFragmentManager(), MainActivity.this));
            viewpager.setCurrentItem(10);
            //calendarTitle = findViewById(R.id.calendartitle);
            //cl = new Calendarfunction();
            //calenderfirst = true;
        //}
        //cl.CalendarSet(textcl, calendarTitle);
        /*FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        CalendarDayPlansFragment cdp = new CalendarDayPlansFragment();
        transaction.replace(R.id.CalendarDayFragment, cdp, " a");
        transaction.addToBackStack(null);
        transaction.commit();*/
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
                setContentView(R.layout.todo);
                nowScreenToDo();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() { //ボタン2を押したとき
            @Override
            public void onClick(View v) { //カレンダー画面に遷移
                setContentView(R.layout.calendar);
                //viewpager = (ViewPager) findViewById(R.id.pager);
                //viewpager.setAdapter(new FragmentCalendarAdapter(getSupportFragmentManager()));
                nowScreenCalendar();
            }
        });
    }

    /*private void startTimer(Button button) { //タイマースタート
        if(countflag == -1) {
            timer = new Timer();
            handler = new Handler();
            countflag = 1;
        }
        timercount = new TimercountTask();
        timer.schedule(timercount, delay, period);
        button.setText(R.string.stop);
        timerNow = false;
    }

    private void pauseTimer(Button button) { //タイマーストップ
        timercount.cancel();
        //count = timercount.getCount();
        button.setText(R.string.restart);
        timerNow = true;
    }

    private void resetTimer(Button button) { //タイマーリセット
        if(timer != null) {
            timer.cancel();
            timer = null;
            //timercount.cancel();
            countflag = -1;
            button.setText(R.string.start);
            count = 0;
        }
        timerText.setText(String.format(Locale.US, countfirst));
        //timerText.invalidate();
        System.out.println(timerText.getText());
        timerNow = true;
    }

    class TimercountTask extends TimerTask { //タイマー処理

        @Override
        public void run() {
            handler.post(new Runnable() {
                public void run() {
                    if (timer != null) {
                        count++;
                        long h = count / 100 / 3600;
                        long m = count / 100 / 60 % 60;
                        long s = count / 100 % 60;
                        long ps = count % 100;
                        if (h >= 1) {
                            timerText.setText(String.format(Locale.US, "%02d:%02d:%02d", h, m, s));
                        } else {
                            timerText.setText(String.format(Locale.US, "%02d:%02d.%02d", m, s, ps));
                        }
                    }
                }
            });
        }
    }*/
}
