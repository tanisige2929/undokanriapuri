package com.example.ExerciseApplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.io.IOException;

public class WalkingActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager sensorManager;
    private TextView hosuu;
    private Button start;
    private Button reset;
    private NumberPicker pickerhour;
    private NumberPicker pickerminute;
    private TextView hourText;
    private TextView minuteText;
    private TextView timer;
    private String[] minute = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
            "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
            "31", "32", "33", "34", "35", "36", "37", "38", "39", "40",
            "41", "42", "43", "44", "45", "46", "47", "48", "49", "50",
            "51", "52", "53", "54", "55", "56", "57", "58", "59"};
    private String[] hour = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private int hourvalue;
    private int minutevalue;
    private float a = 0.65f;
    private int countHosuu = 0;
    float degreeFirst, degree = 0f;
    private boolean up = false;
    private boolean first = true;
    private CountDown countdown;
    private MediaPlayer player;
    private boolean timerNow = false, countflag = false;;
    private long count = 0, period = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        hosuu = findViewById(R.id.hosuu);
        timer = findViewById(R.id.countdown);
        start = findViewById(R.id.countdownstart);
        reset = findViewById(R.id.countdownreset);
        pickerhour = findViewById(R.id.numberpickerhour);
        pickerminute = findViewById(R.id.numberpickerminute);
        hourText = findViewById(R.id.hour);
        minuteText = findViewById(R.id.minute);
        pickerhour.setMinValue(0);
        pickerhour.setMaxValue(10);
        pickerminute.setMinValue(0);
        pickerminute.setMaxValue(59);
        pickerhour.setDisplayedValues(hour);
        pickerminute.setDisplayedValues(minute);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hosuu.setVisibility(View.VISIBLE);
                timer.setVisibility(View.VISIBLE);
                pickerhour.setVisibility(View.INVISIBLE);
                pickerminute.setVisibility(View.INVISIBLE);
                hourText.setVisibility(View.INVISIBLE);
                minuteText.setVisibility(View.INVISIBLE);
                hourvalue = pickerhour.getValue();
                minutevalue = pickerminute.getValue();
                count = (hourvalue * 3600 + minutevalue * 60) * 1000;
                startTimer(start);
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setClickable(true);
                hosuu.setVisibility(View.INVISIBLE);
                timer.setVisibility(View.INVISIBLE);
                pickerhour.setVisibility(View.VISIBLE);
                pickerminute.setVisibility(View.VISIBLE);
                hourText.setVisibility(View.VISIBLE);
                minuteText.setVisibility(View.VISIBLE);
                if(countdown != null) {
                    countdown.cancel();
                    countdown = null;
                }
                onPause();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float sensorX, sensorY, sensorZ;
        float sum = 0;

        sensorX = event.values[0];
        sensorY = event.values[1];
        sensorZ = event.values[2];
        //System.out.println(sensorX);
        sum = (float)Math.sqrt(Math.pow(sensorX, 2) + Math.pow(sensorY, 2) + Math.pow(sensorZ, 2));
        System.out.println(sum);
        if(first) {
            first = false;
            up = true;
            degreeFirst = a * sum;
        }
        else {
            degree = a + sum + (1 - a) * degreeFirst;
            if(up && degree < degreeFirst) {
                up = false;
                countHosuu++;
            }
            else if(!up && degree > degreeFirst) {
                up = true;
                degreeFirst = degree;
            }
            hosuu.setText(String.valueOf(countHosuu) + "歩");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void showInfo(float x, float y, float z, float sum) {
        StringBuffer info = new StringBuffer("values: ");
        info.append("sensoerX:" + x + " ");
        info.append("sensoerY:" + y + " ");
        info.append("sensoerZ:" + z + " ");
        info.append("sum:" + sum);
        System.out.println(info);
    }
    private void startTimer(Button button) { //タイマースタート
        start.setClickable(false);
        countflag = false;
        timerNow = true;
        countdown = new CountDown(4000, period, true);
        timer.setTextColor(Color.BLACK);
        countdown.start();
    }
    private void resetTimer(Button button) {
        timerNow = false;
        if(countdown != null) {
            countdown.cancel();
        }
        count = 0;private void startTimer(Button button) { //タイマースタート
            start.setClickable(false);
            countflag = false;
            timerNow = true;
            countdown = new CountDown(4000, period, true);
            timer.setTextColor(Color.BLACK);
            countdown.start();
        }
        private void resetTimer(Button button) {
            timerNow = false;
            if(countdown != null) {
                countdown.cancel();
            }
        }
    }

    class CountDown extends CountDownTimer {
        private  boolean startflag;
        CountDown(long count, long period, boolean startflag) {
            super(count, period);
            this.startflag = startflag;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long h =  millisUntilFinished / 1000 / 3600;
            long m = millisUntilFinished / 1000 / 60 % 60;
            long s = millisUntilFinished / 1000 % 60;
            if(countflag) {
                timer.setText(String.format("%1$02d:%2$02d:%3$02d", h, m, s));
            }
            else if(startflag){
                if(s >= 1) {
                    timer.setText(String.format("%1$d", s));
                }
                else if(startflag && s < 1){
                    timer.setText("スタート");
                }
                else if(s == 0) {
                    startflag = false;
                }
            }
        }

        @Override
        public void onFinish() {
            countdown.cancel();
            timer.setTextColor(Color.BLACK);
            if(!countflag) {
                countflag = true;
                countdown = new CountDown(count, period, false);
                countdown.start();
            }
            else {
                countflag = false;
                /*try {
                    player.prepare();
                    player.start();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }*/
                timerNow = false;
            }
        }
    }

}