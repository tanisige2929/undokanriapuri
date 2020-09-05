package com.example.ExerciseApplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URI;

import static android.provider.Settings.System.DEFAULT_ALARM_ALERT_URI;

public class IntervalActivity extends AppCompatActivity {

    private String[] minute = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
            "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
            "31", "32", "33", "34", "35", "36", "37", "38", "39", "40",
            "41", "42", "43", "44", "45", "46", "47", "48", "49", "50",
            "51", "52", "53", "54", "55", "56", "57", "58", "59", "60"};
    private String[] second = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
            "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
            "31", "32", "33", "34", "35", "36", "37", "38", "39", "40",
            "41", "42", "43", "44", "45", "46", "47", "48", "49", "50",
            "51", "52", "53", "54", "55", "56", "57", "58", "59"};
    private String[] set = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private NumberPicker pickerminute;
    private NumberPicker pickersecond;
    private NumberPicker pickerset;
    private TextView timer;
    private TextView minuteText;
    private TextView secondText;
    private TextView setText;
    private TextView intervalMessage;
    private TextView joutai;
    private TextView tyuui;
    private long count = 0, period = 10;
    private long dialogCount = 0;
    private boolean timerNow = false;
    private boolean countflag = false;
    private Handler handler;
    private Button start;
    private Button reset;
    private int setvalue;
    private int minutevalue;
    private int secondvalue;
    private int minutedialogvalue;
    private int seconddialogvalue;
    private CountDown countdown;
    private Context context = this;
    private AlertDialog alertDialog;
    private int cnt;
    private SoundPool soundPool;
    private int soundId1, soundId2;
    private int streamId1, streamId2;
    private boolean sound1flag = true, sound2flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interval);
        start = findViewById(R.id.countdownstart);
        reset = findViewById(R.id.countdownreset);
        timer = findViewById(R.id.Timer);
        minuteText = findViewById(R.id.minute);
        secondText = findViewById(R.id.second);
        setText = findViewById(R.id.set);
        intervalMessage = findViewById(R.id.timertitle);
        joutai = findViewById(R.id.joutai);
        tyuui = findViewById(R.id.keikoku);
        tyuui.setVisibility(View.INVISIBLE);
        pickerminute = findViewById(R.id.numberpickerminute);
        pickersecond = findViewById(R.id.numberpickersecond);
        pickerset = findViewById(R.id.numberpickerset);

        pickerminute.setMinValue(0);
        pickerminute.setMaxValue(59);
        pickersecond.setMinValue(0);
        pickersecond.setMaxValue(59);
        pickerset.setMinValue(0);
        pickerset.setMaxValue(9);

        pickerminute.setDisplayedValues(minute);
        pickersecond.setDisplayedValues(second);
        pickerset.setDisplayedValues(set);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM).setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();
        soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(1).build();
        soundId1 = soundPool.load(this, R.raw.countdown, 1);
        soundId2 = soundPool.load(this, R.raw.clockalarm, 1);
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
                sound1flag = true;
                sound2flag = true;
                minutevalue = pickerminute.getValue();
                secondvalue = pickersecond.getValue();
                count = (minutevalue * 60 + secondvalue) * 1000;
                if(count == 0) {
                    tyuui.setVisibility(View.VISIBLE);
                }
                else {
                    tyuui.setVisibility(View.INVISIBLE);
                    LayoutInflater layoutInflater = LayoutInflater.from(IntervalActivity.this);
                    final View inputView = layoutInflater.inflate(R.layout.intervaldialog, null);
                    final NumberPicker dialogminute = inputView.findViewById(R.id.numberpickerminutedialog);
                    final NumberPicker dialogsecond = inputView.findViewById(R.id.numberpickerseconddialog);
                    dialogminute.setMinValue(0);
                    dialogminute.setMaxValue(59);
                    dialogsecond.setMinValue(0);
                    dialogsecond.setMaxValue(59);
                    dialogminute.setDisplayedValues(minute);
                    dialogsecond.setDisplayedValues(second);

                    setvalue = pickerset.getValue() + 1;

                    if(setvalue != 1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(IntervalActivity.this);
                        builder.setMessage("運動間隔（休憩）時間を指定してください").setView(inputView).setPositiveButton("スタート", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setInvisible(1);
                                minutedialogvalue = dialogminute.getValue();
                                seconddialogvalue = dialogsecond.getValue();
                                dialogCount = (minutedialogvalue * 60 + seconddialogvalue) * 1000;
                                if(!timerNow) {
                                    cnt = 0;
                                    startTimer(start);
                                }
                            }
                        }).setNeutralButton("キャンセル", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "キャンセルしました", Toast.LENGTH_SHORT).show();
                            }
                        });
                        alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                    }
                    else {
                        setInvisible(1);
                        startTimer(start);
                        if(!timerNow) {
                            cnt = 0;
                            startTimer(start);
                        }
                    }
                }
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setInvisible(2);
                joutai.setVisibility(View.INVISIBLE);
                tyuui.setVisibility(View.INVISIBLE);
                if(soundPool != null) {
                    if(!sound1flag) {
                        soundPool.stop(streamId1);
                    }
                    else if(!sound2flag) {
                        soundPool.stop(streamId2);
                    }
                }
                resetTimer(reset);
            }
        });
    }
    private void startTimer(Button button) { //タイマースタート
        start.setClickable(false);
        cnt = 0;
        countflag = false;
        timerNow = true;
        countdown = new CountDown(4000, period, true);
        timer.setTextColor(Color.BLACK);
        countdown.start();
    }
    private void resetTimer(Button button) {
        start.setClickable(true);
        timerNow = false;
        if(countdown != null) {
            countdown.cancel();
            countdown = null;
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
                joutai.setVisibility(View.VISIBLE);
                joutai.setText(R.string.intervaljoutai1);
                if(h == 0 && m == 0 && s <= 5) {
                    timer.setTextColor(Color.RED);
                    if(s == 0) {
                        streamId2 =  soundPool.play(soundId2, 1.0f, 1.0f, 0, 0, 1);
                        sound2flag = false;
                    }
                }
                timer.setText(String.format("%1$02d:%2$02d:%3$02d", h, m, s));
            }
            else if(startflag){
                if(sound1flag) {
                    streamId1 = soundPool.play(soundId1, 1.0f, 1.0f, 0, 0, 1);
                    sound1flag = false;
                }
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
            else {
                joutai.setText(R.string.intervaljoutai2);
                timer.setText(String.format("%1$02d:%2$02d", m, s));
                if(s <= 3) {
                    if(sound1flag) {
                        streamId1 = soundPool.play(soundId1, 1.0f, 1.0f, 0, 0, 1);
                        sound1flag = false;
                    }
                }
            }
        }

        @Override
        public void onFinish() {
            countdown.cancel();
            timer.setTextColor(Color.BLACK);
            if(!countflag) {
                countflag = true;
                sound2flag = true;
                countdown = new CountDown(count, period, false);
                countdown.start();
                cnt++;
            }
            else {
                countflag = false;
                sound1flag = true;
                if(cnt < setvalue) {
                    countdown = new CountDown(dialogCount, period, false);
                    countdown.start();
                }
                else {
                    joutai.setText(R.string.intervaljoutei3);
                    timerNow = false;
                }
            }
        }
    }
    public void setInvisible(int flag) {
        if(flag == 1) {
            pickerminute.setVisibility(View.INVISIBLE);
            pickersecond.setVisibility(View.INVISIBLE);
            pickerset.setVisibility(View.INVISIBLE);
            minuteText.setVisibility(View.INVISIBLE);
            secondText.setVisibility(View.INVISIBLE);
            setText.setVisibility(View.INVISIBLE);
            intervalMessage.setVisibility(View.INVISIBLE);
            timer.setVisibility(View.VISIBLE);
        }
        else if(flag == 2) {
            pickerminute.setVisibility(View.VISIBLE);
            pickersecond.setVisibility(View.VISIBLE);
            pickerset.setVisibility(View.VISIBLE);
            minuteText.setVisibility(View.VISIBLE);
            secondText.setVisibility(View.VISIBLE);
            setText.setVisibility(View.VISIBLE);
            intervalMessage.setVisibility(View.VISIBLE);
            timer.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public void onBackPressed() {
        if(soundPool != null) {
            if(!sound1flag) {
                soundPool.stop(streamId1);
            }
            else if(!sound2flag) {
                soundPool.stop(streamId2);
            }
        }
        finish();
    }
}