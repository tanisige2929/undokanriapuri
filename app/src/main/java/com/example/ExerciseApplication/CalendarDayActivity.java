package com.example.ExerciseApplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarDayActivity extends AppCompatActivity{

    private String Day;
    private Intent intent;
    private String yearSelected;
    private String monthSelected;
    private String daySelected;
    private List<String> list;
    private ArrayAdapter<String> adapterUnit;
    private ArrayList idList;
    private Context context;
    private AlertDialog alertDialog;
    private String menuname = "";
    private String menuvalue = "";
    private String menuunit = "";
    private ListView listView;
    private int henshuflag = -1;
    private int listPosition;
    private int datasetflag = -1;
    private boolean keikokuflag = false;
    private Calendar calendar;
    private int dayNow;
    private int yearNow;
    private int monthNow;
    private int Today;
    private int w;
    private TextView nomenu;
    private int dateflag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        context = this;
        calendar = Calendar.getInstance();
        dayNow = calendar.get(Calendar.DATE);
        yearNow = calendar.get(Calendar.YEAR);
        monthNow = calendar.get(Calendar.MONTH) + 1;
        Today += yearNow * 10000 + monthNow * 100 + dayNow;
        setDisplay();
        datasetflag = 0;
        databaseAction(2, dateflag);
        if(idList.size() != 0) {
            nomenu.setVisibility(View.INVISIBLE);
        }
        adapterUnit = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView)super.getView(position, convertView, parent);
                view.setTextSize(20);
                return view;
            }
        };
    }
    public void makeDialog(int title, int message, int ok, int cancel, int flag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CalendarDayActivity.this);
        if(flag == 1) {
            LayoutInflater layoutInflater = LayoutInflater.from(CalendarDayActivity.this);
            final View inputView = layoutInflater.inflate(R.layout.calendarmenudialog, null);
            final EditText editText1 = inputView.findViewById(R.id.input1);
            final EditText editText2 = inputView.findViewById(R.id.input2);
            final Spinner spinner = inputView.findViewById(R.id.spinner);
            TextView keikoku = inputView.findViewById(R.id.keikoku);
            if(keikokuflag) {
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
                        makeDialog(R.string.dialogtitle1, R.string.dialogmessage, R.string.dialognext, R.string.dialogcancel, 1);
                    } else {
                        if (henshuflag == -1) {
                            databaseAction(1, 0);
                            Toast.makeText(getApplicationContext(), "登録が完了しました", Toast.LENGTH_SHORT).show();
                            datasetflag = 0;
                            TextView nomenu = (TextView) findViewById(R.id.NoMenu);
                            nomenu.setVisibility(View.INVISIBLE);
                        }
                        else {
                            databaseAction(3, 0);
                            Toast.makeText(getApplicationContext(), "編集が完了しました", Toast.LENGTH_SHORT).show();
                            henshuflag = -1;
                            datasetflag = -1;
                        }
                        databaseAction(2, 1);
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
        }
        else {
            builder.setMessage(message);
            builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    databaseAction(4, 0);
                    datasetflag = -1;
                    databaseAction(2, 1);
                    Toast.makeText(getApplicationContext(), "削除しました", Toast.LENGTH_SHORT).show();
                    if(idList.size() == 0) {
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
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
    public void databaseAction(int flag, int dateflag) {
        DatabaseHelper helper = new DatabaseHelper(CalendarDayActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        if(flag == 1) {
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
        else if(flag == 2) {
            try {
                String sql = null;
                if(dateflag == 1) {
                    sql = "SELECT _id, menu, value, unit FROM ExerciseMenu WHERE date = '" + Day + "'";
                }
                else if(dateflag == 2){
                    sql = "SELECT _id, menu, value, unit FROM ExerciseMenu WHERE date = '" + Day + "' and complete = 1";
                }
                Cursor cursor = db.rawQuery(sql, null);
                int id;
                String menu = "";
                String value = "";
                String unit = "";
                idList = new ArrayList<>();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        TextView view = (TextView)super.getView(position, convertView, parent);
                        view.setTextSize(20);
                        return view;
                    }
                };
                while(cursor.moveToNext()) {
                    int idxNote;
                    idxNote = cursor.getColumnIndex("_id");
                    id = cursor.getInt(idxNote);
                    idxNote = cursor.getColumnIndex("menu");
                    menu = cursor.getString(idxNote);
                    idxNote = cursor.getColumnIndex("value");
                    value = cursor.getString(idxNote);
                    idxNote = cursor.getColumnIndex("unit");
                    unit = cursor.getString(idxNote);
                    idList.add(id);
                    System.out.println(idList);
                    adapter.add("" + menu + " " + value + unit);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                }
                registerForContextMenu(listView);
                datasetflag = -1;
            }
            finally {
                db.close();
            }
        }
        else if(flag == 3) {
            try {
                String sqlUpdate = "UPDATE Exercisemenu SET menu = '" + menuname + "', value = '" + menuvalue + "', unit = '" + menuunit + "' WHERE _id = " + idList.get(listPosition);
                db.execSQL(sqlUpdate);
            }
            finally {
                db.close();
            }
        }
        else {
            try {
                String sqlDelete = "DELETE FROM Exercisemenu WHERE _id = " + idList.get(listPosition);
                db.execSQL(sqlDelete);
            }
            finally {
                db.close();
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        listPosition = info.position;
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menuContexthenshu:
                henshuflag = 1;
                makeDialog(R.string.dialogtitle1, R.string.dialogmessage, R.string.dialogtouroku, R.string.dialogcancel, 1);
                break;
            case R.id.menuContextsakujo:
                makeDialog(0, R.string.dialogmessage2, R.string.dialogok, R.string.dialogcancel, 2);
                break;
        }
        return super.onContextItemSelected(item);
    }
    public void setDisplay() {
        setContentView(R.layout.activity_calendar_day);
        nomenu = (TextView)findViewById(R.id.NoMenu);
        FloatingActionButton plusButton = findViewById(R.id.plusbutton);
        listView = findViewById(R.id.listview);
        TextView title = (TextView)findViewById(R.id.daymenutitle);
        Day = intent.getStringExtra("key");
        yearSelected = Day.substring(0, 4);
        monthSelected = Day.substring(5, 7);
        daySelected = Day.substring(8, 10);
        w = Integer.parseInt(yearSelected) * 10000 + Integer.parseInt(monthSelected) * 100 + Integer.parseInt(daySelected);
        if(w >= Today) {
            title.setText(monthSelected + "月" + daySelected + "日のメニュー");//ここまで初期画面
            plusButton.setVisibility(View.VISIBLE);
            dateflag = 1;
        }
        else {
            title.setText(monthSelected + "月" + daySelected + "日の実績");
            plusButton.setVisibility(View.INVISIBLE);
            nomenu.setText(R.string.jisseki);
            dateflag = 2;
        }
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDialog(R.string.dialogtitle1, R.string.dialogmessage, R.string.dialogtouroku, R.string.dialogcancel, 1);
            }
        });
    }
}