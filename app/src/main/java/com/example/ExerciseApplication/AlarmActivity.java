package com.example.ExerciseApplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;

import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.io.IOException;

import static android.provider.Settings.System.DEFAULT_ALARM_ALERT_URI;

public class AlarmActivity extends AppCompatActivity {

    private AlarmManager am;
    private String[] minuteandsecond = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                               "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                               "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
                               "31", "32", "33", "34", "35", "36", "37", "38", "39", "40",
                               "41", "42", "43", "44", "45", "46", "47", "48", "49", "50",
                               "51", "52", "53", "54", "55", "56", "57", "58", "59"};
    private String[] hour = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private NumberPicker pickerminute;
    private NumberPicker pickersecond;
    private NumberPicker pickerhour;
    private TextView timer;
    private TextView minuteText;
    private TextView secondText;
    private TextView hourText;
    private long count = 0, period = 10;
    private TextView timerText;
    private TextView tyuui;
    private int countflag = -1;
    private Handler handler;
    private Button start;
    private Button reset;
    private int hourvalue;
    private int minutevalue;
    private int secondvalue;
    private CountDown countdown;
    private Context context = this;
    private SoundPool soundPool;
    private boolean soundflag = true;
    private int soundId;
    private int streamId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        start = findViewById(R.id.countdownstart);
        reset = findViewById(R.id.countdownreset);
        start.setClickable(false);
        reset.setClickable(false);
        tyuui = findViewById(R.id.keikoku);
        tyuui.setVisibility(View.INVISIBLE);
        timer = findViewById(R.id.Timer);
        timerText = findViewById(R.id.alarmtitle);
        minuteText = findViewById(R.id.minute);
        secondText = findViewById(R.id.second);
        hourText = findViewById(R.id.hour);
        pickerminute = findViewById(R.id.numberpickerminute);
        pickersecond = findViewById(R.id.numberpickersecond);
        pickerhour = findViewById(R.id.numberpickerhour);

        pickerminute.setMinValue(0);
        pickerminute.setMaxValue(59);
        pickersecond.setMinValue(0);
        pickersecond.setMaxValue(59);
        pickerhour.setMinValue(0);
        pickerhour.setMaxValue(10);

        pickerminute.setDisplayedValues(minuteandsecond);
        pickersecond.setDisplayedValues(minuteandsecond);
        pickerhour.setDisplayedValues(hour);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM).setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();
        soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(1).build();
        soundId = soundPool.load(this, R.raw.clockalarm, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                start.setClickable(true);
                reset.setClickable(true);
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundflag = true;
                hourvalue = pickerhour.getValue();
                minutevalue = pickerminute.getValue();
                secondvalue = pickersecond.getValue();
                count = (hourvalue * 3600 + minutevalue * 60 + secondvalue) * 1000;
                if(count == 0) {
                    tyuui.setVisibility(View.VISIBLE);
                }
                else {
                    pickerminute.setVisibility(View.INVISIBLE);
                    pickersecond.setVisibility(View.INVISIBLE);
                    pickerhour.setVisibility(View.INVISIBLE);
                    minuteText.setVisibility(View.INVISIBLE);
                    secondText.setVisibility(View.INVISIBLE);
                    hourText.setVisibility(View.INVISIBLE);
                    timerText.setVisibility(View.INVISIBLE);
                    timer.setVisibility(View.VISIBLE);
                    startTimer(start);
                }
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setClickable(true);
                tyuui.setVisibility(View.INVISIBLE);
                pickerminute.setVisibility(View.VISIBLE);
                pickersecond.setVisibility(View.VISIBLE);
                pickerhour.setVisibility(View.VISIBLE);
                minuteText.setVisibility(View.VISIBLE);
                secondText.setVisibility(View.VISIBLE);
                hourText.setVisibility(View.VISIBLE);
                timerText.setVisibility(View.VISIBLE);
                timer.setVisibility(View.INVISIBLE);
                if(soundPool != null) {
                    if (!soundflag) {
                        soundPool.stop(streamId);
                        soundflag = true;
                    }
                }
                resetTimer(reset);
            }
        });
    }
    private void startTimer(Button button) { //タイマースタート
        start.setClickable(false);
        countdown = new CountDown(count, period);
        countdown.start();
    }
    private void resetTimer(Button button) {
        start.setClickable(true);
        if(countdown != null) {
            countdown.cancel();
            countdown = null;
        }
    }
    class CountDown extends CountDownTimer {

        CountDown(long count, long period) {
            super(count, period);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            long h =  millisUntilFinished / 1000 / 3600;
            long m = millisUntilFinished / 1000 / 60 % 60;
            long s = millisUntilFinished / 1000 % 60;
            timer.setText(String.format("%1$02d:%2$02d:%3$02d", h, m, s));
        }

        @Override
        public void onFinish() {
            countdown.cancel();
            timer.setText("00:00:00");
            if(soundflag) {
                streamId =  soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1);
                soundflag = false;
            }
        }
    }
    @Override
    public void onBackPressed() {
        if(soundPool != null) {
            if (!soundflag) {
                soundPool.stop(streamId);
                soundflag = true;
            }
        }
        if(countdown != null) {
            countdown.cancel();
        }
        finish();
    }
}
