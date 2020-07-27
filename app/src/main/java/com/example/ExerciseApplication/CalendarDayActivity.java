package com.example.ExerciseApplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarDayActivity extends AppCompatActivity{

    private String DayTitle;
    private Intent intent;
    private String year;
    private String month;
    private String day;
    private String date;
    private ArrayList data = new ArrayList<>();
    private int flag = 0;
    //private AlertDialog.Builder builder;
    //private AlertDialog.Builder builderhoge;
    private List<String> list;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> listAdapter;
    private Button button;
    private int selectedIndex = 0;
    private Context context;
    private AlertDialog alertDialog;
    private String menuname = "";
    private String menuvalue = "";
    private String menuunit = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_day);
         context = this;
        //helper = new DatabaseHelper(CalendarDayActivity.this);
        //db = helper.getWritableDatabase();
        list = makeDataList();

        TextView title = (TextView)findViewById(R.id.daymenutitle);
        TextView nomenu = (TextView)findViewById(R.id.NoMenu);
        nomenu.setVisibility(View.INVISIBLE);
        //if(nomenu.)
        intent = getIntent();
        DayTitle = intent.getStringExtra("key");
        year = DayTitle.substring(0, 4);
        month = DayTitle.substring(5, 7);
        day = DayTitle.substring(8, 10);
        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy/mm/dd", Locale.JAPAN);
        try {
            date =  sdf.parse(DayTitle);
            System.out.println(date);
        }catch(ParseException e) {
            e.printStackTrace();
        }*/
        //date = Date.valueOf(DayTitle);
        //System.out.println("aiueo" + DayTitle + year + " " + month + " " + day);
        title.setText(month + "月" + day + "日のメニュー");//ここまで初期画面

        /*ListView menuList = findViewById(R.id.listview);//運動メニュー一覧登録済み
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);

        menuList.setAdapter(listAdapter);*/
        databaseAction(2);


        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, list);


        FloatingActionButton plusButton = findViewById(R.id.plusbutton);

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDialog(R.string.dialogtitle1, R.string.dialogmessage1, R.string.dialognext, R.string.dialogcancel, 1);
            }
        });
    }
    public void makeDialog(int title, int message, int ok, int cancel, int flag) {
        //menuname = "";
        //menuvalue = "";
        //menuunit = "";
        //builderhoge = builder;
        AlertDialog.Builder builder = new AlertDialog.Builder(CalendarDayActivity.this);
        //builderhoge = new AlertDialog.Builder(CalendarDayActivity.this);

        //builderhoge.setTitle(title).setMessage(message).setView(editText);
        //builder.setPositiveButton(ok, new DialogButtonClickListener());
        if(flag == 1) {
            final EditText editText = new EditText(CalendarDayActivity.this);
            editText.setGravity(1);
            builder.setTitle(title).setMessage(message);
            builder.setView(editText);
            builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    menuname = editText.getText().toString();
                    System.out.println(menuname);
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
                    System.out.println(menuvalue);
                    makeDialog(R.string.dialogtitle1, R.string.dialogmessage3, R.string.dialogok, R.string.dialogcancel, 3);
                }
            });
            builder.setNeutralButton(cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "キャンセルしました", Toast.LENGTH_SHORT).show();
                }
            });
            /*builder.setSingleChoiceItems(adapter, selectedIndex, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int index) {
                    selectedIndex = index;
                    String item = adapter.getItem(index);
                    button.setText(item);
                }
            });*/
        }
        else {
            builder.setTitle(title);
            builder.setSingleChoiceItems(adapter, selectedIndex, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int index) {
                    selectedIndex = index;
                    menuunit = adapter.getItem(index);
                }
            });
            builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //System.out.println(menuunit);
                    databaseAction(1);
                    databaseAction(2);
                    Toast.makeText(getApplicationContext(), "登録が完了しました", Toast.LENGTH_SHORT).show();
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
        alertDialog = builder.create();
        alertDialog.show();
    }
    private List<String> makeDataList() {
        List<String> list = new ArrayList<String>();
        list.add("回");
        list.add("秒");
        list.add("分");
        list.add("時間");
        list.add("m（㍍）");
        return  list;
    }
    public void databaseAction(int flag) {
        DatabaseHelper helper = new DatabaseHelper(CalendarDayActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        System.out.println(DayTitle + menuname + menuvalue + menuunit);
        if(flag == 1) {
            try {
                String sqlInsert = "INSERT INTO Exercisemenu (date, menu, value, unit, complete) VALUES (?, ?, ?, ?, ?)";
                SQLiteStatement stmt = db.compileStatement(sqlInsert);
                stmt.bindString(1, DayTitle);
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
                String sql = "SELECT id, menu, value, unit FROM ExerciseMenu WHERE date = '" + DayTitle + "'";
                Cursor cursor = db.rawQuery(sql, null);
                int id;
                String menu = "";
                String value = "";
                String unit = "";
                ListView listView = findViewById(R.id.listview);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
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

                    adapter.add("" + menu + " " + value + unit);
                    listView.setAdapter(adapter);
                }
                registerForContextMenu(listView);

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
}