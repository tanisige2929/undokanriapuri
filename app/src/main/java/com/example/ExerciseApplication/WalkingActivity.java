package com.example.ExerciseApplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.io.IOException;
import java.util.Calendar;

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
    private long count = 0, period = 10;
    private SoundPool soundPool;
    private int soundId;
    private int streamId;
    private boolean soundflag = true;
    private boolean countup = false;
    private TextView tyuui;
    private DatabaseHelper helper;
    private SQLiteDatabase db;
    private Calendar calendar;
    private int year;
    private int month;
    private int day;
    private String date, w;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DATE);
        date = "" + year + "-";
        w = "" + month;
        if(w.length() == 1) w = "0" + w;
        date += w + "-";
        w = "" + day;
        if(w.length() == 1) w = "0" + w;
        date += w;

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        hosuu = findViewById(R.id.hosuu);
        timer = findViewById(R.id.countdown);
        start = findViewById(R.id.countdownstart);
        reset = findViewById(R.id.countdownreset);
        tyuui = findViewById(R.id.keikoku);
        tyuui.setVisibility(View.INVISIBLE);
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
        onPause();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countup = true;
                hourvalue = pickerhour.getValue();
                minutevalue = pickerminute.getValue();
                count = (hourvalue * 3600 + minutevalue * 60) * 1000;
                if(count == 0) {
                    tyuui.setVisibility(View.VISIBLE);
                }
                else {
                    hosuu.setVisibility(View.VISIBLE);
                    timer.setVisibility(View.VISIBLE);
                    pickerhour.setVisibility(View.INVISIBLE);
                    pickerminute.setVisibility(View.INVISIBLE);
                    hourText.setVisibility(View.INVISIBLE);
                    minuteText.setVisibility(View.INVISIBLE);
                    tyuui.setVisibility(View.INVISIBLE);
                    onStart();
                    startTimer(start);
                }
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countup = false;
                start.setClickable(true);
                tyuui.setVisibility(View.INVISIBLE);
                hosuu.setVisibility(View.INVISIBLE);
                timer.setVisibility(View.INVISIBLE);
                pickerhour.setVisibility(View.VISIBLE);
                pickerminute.setVisibility(View.VISIBLE);
                hourText.setVisibility(View.VISIBLE);
                minuteText.setVisibility(View.VISIBLE);
                countHosuu = 0;
                if(soundPool != null) {
                    if (!soundflag) {
                        soundPool.stop(streamId);
                        soundflag = true;
                    }
                }
                resetTimer(reset);
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
        sum = (float)Math.sqrt(Math.pow(sensorX, 2) + Math.pow(sensorY, 2) + Math.pow(sensorZ, 2));
        if(first) {
            first = false;
            up = true;
            degreeFirst = a * sum;
        }
        else {
            degree = a + sum + (1 - a) * degreeFirst;
            if(up && degree < degreeFirst) {
                up = false;
                if(countup) {
                    countHosuu++;
                }
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

    private void startTimer(Button button) { //タイマースタート
        start.setClickable(false);
        countdown = new CountDown(count, period, true);
        timer.setTextColor(Color.BLACK);
        countdown.start();
    }
    private void resetTimer(Button button) {
        start.setClickable(true);
        if(countdown != null) {
            countdown.cancel();
        }
        count = 0;
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
            timer.setText(String.format("%1$02d:%2$02d:%3$02d", h, m, s));
        }

        @Override
        public void onFinish() {
            countdown.cancel();
            countup = false;
            timer.setTextColor(Color.BLACK);
            if(soundflag) {
                streamId =  soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1);
                soundflag = false;
            }
            if(countHosuu != 0) Database();
            onPause();
        }
    }
    public void Database() {
        helper = new DatabaseHelper(WalkingActivity.this);
        db = helper.getWritableDatabase();
        try {
            String sqlInsert = "INSERT INTO Exercisemenu (date, menu, value, unit, complete) VALUES (?, ?, ?, ?, ?)";
            SQLiteStatement stmt = db.compileStatement(sqlInsert);
            stmt.bindString(1, date);
            stmt.bindString(2, "歩数");
            stmt.bindString(3, "" + countHosuu);
            stmt.bindString(4, "歩");
            stmt.bindLong(5, 1);
            stmt.executeInsert();
        }
        finally {
            db.close();
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
        if(countHosuu != 0) Database();
        finish();
    }
}