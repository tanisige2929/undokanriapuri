package com.example.ExerciseApplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
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
    private AlertDialog alertDialog;
    private String menuname = "";
    private String menuvalue = "";
    private String menuunit = "";
    private boolean keikokuflag = false;
    private Calendar calendar;
    private int yearNow;
    private int monthNow;
    private int dateNow;
    private String Day = "";
    private String w;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_watch);
        calendar = Calendar.getInstance();
        yearNow = calendar.get(Calendar.YEAR);
        monthNow = calendar.get(Calendar.MONTH);
        dateNow = calendar.get(Calendar.DATE);
        Day += yearNow + "-";
        w = "" + monthNow;
        if(w.length() == 1) w = "0" + w ;
        Day += w + "-";
        w = "" + dateNow;
        if(w.length() == 1) w = "0" + w;
        Day += w;
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
        timercount = new StopWatchActivity.TimerCountTask();
        timer.schedule(timercount, delay, period);
        button.setText(R.string.stop);
        timerNow = false;
    }

    private void pauseTimer(Button button) { //タイマーストップ
        timercount.cancel();
        button.setText(R.string.restart);
        timerNow = true;
    }

    private void resetTimer(Button button) { //タイマーリセット
        if(timer != null) {
            timer.cancel();
            timer = null;
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
   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       MenuInflater inflater = getMenuInflater();
       inflater.inflate(R.menu.menu_options, menu);
       return super.onCreateOptionsMenu(menu);
   }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        makeDialog(R.string.dialogtitle1, R.string.dialogmessage, R.string.dialogok, R.string.dialogcancel);
        return super.onOptionsItemSelected(item);
    }
    public void makeDialog(int title, int message, int ok, int cancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(StopWatchActivity.this);
        LayoutInflater layoutInflater = LayoutInflater.from(StopWatchActivity.this);
        final View inputView = layoutInflater.inflate(R.layout.calendarmenudialog, null);
        final EditText editText1 = inputView.findViewById(R.id.input1);
        final EditText editText2 = inputView.findViewById(R.id.input2);
        final Spinner spinner = inputView.findViewById(R.id.spinner);
        TextView keikoku = inputView.findViewById(R.id.keikoku);
        if (keikokuflag) {
            keikoku.setVisibility(View.VISIBLE);
            keikokuflag = false;
        }
        builder.setTitle(title).setView(inputView);
        builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                menuname = editText1.getText().toString();
                menuvalue = editText2.getText().toString();
                int idx = spinner.getSelectedItemPosition();
                menuunit = spinner.getSelectedItem().toString();
                if (menuname.equals("") || menuvalue.equals("")) {
                    keikokuflag = true;
                    makeDialog(R.string.dialogtitle1, R.string.dialogmessage, R.string.dialognext, R.string.dialogcancel);
                } else {
                    databaseAction();
                    Toast.makeText(getApplicationContext(), "登録が完了しました", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            }
        });
        builder.setNeutralButton(cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "キャンセルしました", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
    public void databaseAction() {
        DatabaseHelper helper = new DatabaseHelper(StopWatchActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            String sqlInsert = "INSERT INTO Exercisemenu (date, menu, value, unit, complete) VALUES (?, ?, ?, ?, ?)";
            SQLiteStatement stmt = db.compileStatement(sqlInsert);
            stmt.bindString(1, Day);
            stmt.bindString(2, menuname);
            stmt.bindString(3, menuvalue);
            stmt.bindString(4, menuunit);
            stmt.bindLong(5, -1);
            stmt.executeInsert();
        }
        finally {
            db.close();
        }
    }
    @Override
    public void onBackPressed() {
        if(timer != null) {
            timer.cancel();
        }
        finish();
    }
}