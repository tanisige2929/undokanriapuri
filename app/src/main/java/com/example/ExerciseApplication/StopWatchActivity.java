package com.example.ExerciseApplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class StopWatchActivity extends AppCompatActivity {

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

        Button save = findViewById(R.id.swsave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(StopWatchActivity.this);
                builder.setMessage("本日の新しい運動メニューとして、又は既存のメニューとしてデータを登録しますか");
                builder.setPositiveButton("新規登録", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //makeDialog(R.string.dialogtitle1, R.string.dialogmessage2, R.string.dialognext, R.string.dialogcancel, 2);
                    }
                });
            }
        });

    }
    private void startTimer(Button button) { //タイマースタート
        if(countflag == -1) {
            timer = new Timer();
            handler = new Handler();
            countflag = 1;
        }
        timercount = new StopWatchActivity.TimerCountTask();
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
    /*public void makeDialog(int title, int message, int ok, int cancel, int flag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CalendarDayActivity.this);
        if(flag == 1) {
            final EditText editText = new EditText(CalendarDayActivity.this);
            editText.setGravity(1);
            builder.setTitle(title).setMessage(message);
            builder.setView(editText);
            builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    menuname = editText.getText().toString();
                    //System.out.println(menuname);
                    makeDialog(R.string.dialogtitle1, R.string.dialogmessage2, R.string.dialognext, R.string.dialogcancel, 2);
                }
            });
            builder.setNeutralButton(cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "キャンセルしました", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else if(flag == 2){
            final EditText editText = new EditText(CalendarDayActivity.this);
            editText.setGravity(1);
            builder.setTitle(title).setMessage(message);
            builder.setView(editText);
            builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    menuvalue = editText.getText().toString();
                    //System.out.println(menuvalue);
                    makeDialog(R.string.dialogtitle1, R.string.dialogmessage3, R.string.dialogok, R.string.dialogcancel, 3);
                }
            });
            builder.setNeutralButton(cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "キャンセルしました", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if (flag == 3){
            builder.setTitle(title);
            builder.setSingleChoiceItems(adapterUnit, selectedIndex, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int index) {
                    selectedIndex = index;
                    menuunit = adapterUnit.getItem(index);
                }
            });
            builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(henshuflag == -1) {
                        databaseAction(1);
                        Toast.makeText(getApplicationContext(), "登録が完了しました", Toast.LENGTH_SHORT).show();
                        datasetflag = 0;
                        TextView nomenu = (TextView)findViewById(R.id.NoMenu);
                        nomenu.setVisibility(View.INVISIBLE);
                    }
                    else {
                        databaseAction(3);
                        Toast.makeText(getApplicationContext(), "編集が完了しました", Toast.LENGTH_SHORT).show();
                        henshuflag = -1;
                        datasetflag = -1;
                    }
                    databaseAction(2);
                    alertDialog.dismiss();
                }
            });
            builder.setNeutralButton(cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "キャンセルしました", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            builder.setMessage(message);
            builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    databaseAction(4);
                    datasetflag = -1;
                    databaseAction(2);
                    Toast.makeText(getApplicationContext(), "削除しました", Toast.LENGTH_SHORT).show();
                    if(idList.size() == 0) {
                        System.out.println(idList.size());
                        setDisplay();
                    }

                }
            });
            builder.setNeutralButton(cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "キャンセルしました", Toast.LENGTH_SHORT).show();
                }
            });
        }
        alertDialog = builder.create();
        alertDialog.show();

     */
}