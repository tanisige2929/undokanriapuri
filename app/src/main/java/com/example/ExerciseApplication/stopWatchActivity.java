package com.example.ExerciseApplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class stopWatchActivity extends AppCompatActivity {

    private final String countfirst = "00:00.00";
    private Timer timer;
    private TimerCountTask timercount;
    private long count = 0, delay = 0, period = 10;
    private TextView timerText;
    private boolean timerNow = true, calenderfirst = false;
    private int countflag = -1;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);
        timerText = findViewById(R.id.swcount);
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
        });
    }
    private void startTimer(Button button) { //タイマースタート
        if(countflag == -1) {
            timer = new Timer();
            handler = new Handler();
            countflag = 1;
        }
        timercount = new stopWatchActivity.TimerCountTask();
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

    class TimerCountTask extends TimerTask { //タイマー処理

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
    }
}