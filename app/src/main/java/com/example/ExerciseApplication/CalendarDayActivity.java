package com.example.ExerciseApplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import java.util.Map;

public class CalendarDayActivity extends AppCompatActivity{

    private String DayTitle;
    private Intent intent;
    private String year;
    private String month;
    private String day;
    //private String date;
    //private int flag = 0;
    private List<String> list;
    private ArrayAdapter<String> adapterUnit;
    private ArrayList idList;
    private int selectedIndex = 0;
    private Context context;
    private AlertDialog alertDialog;
    private String menuname = "";
    private String menuvalue = "";
    private String menuunit = "";
    private ListView listView;
    private int henshuflag = -1;
    private int listPosition;
    private int datasetflag = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        context = this;
        setDisplay();

        datasetflag = 0;
        databaseAction(2);
        if(idList.size() != 0) {
            TextView nomenu = (TextView)findViewById(R.id.NoMenu);
            nomenu.setVisibility(View.INVISIBLE);
        }
        adapterUnit = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, list);


    }
    public void makeDialog(int title, int message, int ok, int cancel, int flag) {
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
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
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
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
    private List<String> makeDataList() {
        List<String> list = new ArrayList<String>();
        list.add("回");
        list.add("秒");
        list.add("分");
        //list.add("時間");
        list.add("m（㍍）");
        return  list;
    }
    public void databaseAction(int flag) {
        DatabaseHelper helper = new DatabaseHelper(CalendarDayActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        //System.out.println(DayTitle + menuname + menuvalue + menuunit);
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
                String sql = "SELECT _id, menu, value, unit FROM ExerciseMenu WHERE date = '" + DayTitle + "'";
                Cursor cursor = db.rawQuery(sql, null);
                int id;
                String menu = "";
                String value = "";
                String unit = "";
                //ListView listView = findViewById(R.id.listview);
                idList = new ArrayList<>();
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
                    idList.add(id);
                    System.out.println(idList);
                    //datasetflag = -1;
                    //System.out.println(menu + value + unit + "faoirwoarwa");
                    adapter.add("" + menu + " " + value + unit);
                    //System.out.println(adapter);
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
                //System.out.println(menuname + menuvalue + menuunit + idList.get(listPosition));
                String sqlUpdate = "UPDATE Exercisemenu SET menu = '" + menuname + "', value = '" + menuvalue + "', unit = '" + menuunit + "' WHERE _id = " + idList.get(listPosition);
                db.execSQL(sqlUpdate);
            }
            finally {
                db.close();
            }
        }
        else {
            try {
                //adapterUnit.notifyDataSetChanged();
                String sqlDelete = "DELETE FROM Exercisemenu WHERE _id = " + idList.get(listPosition);
                db.execSQL(sqlDelete);
                //setContentView(R.layout.activity_calendar_day);
                //idList.remove(listPosition);
                //System.out.println("unou" + idList + " " + idList.get(listPosition));
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
        //menuhoge = listView.getItemAtPosition(listPosition).toString();
        System.out.println("position" + listPosition);
        //Map<String, Object> menu = listView.get(listPosition);
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menuContexthenshu:
                henshuflag = 1;
                makeDialog(R.string.dialogtitle1, R.string.dialogmessage1, R.string.dialognext, R.string.dialogcancel, 1);
                break;
            case R.id.menuContextsakujo:
                makeDialog(0, R.string.dialogmessage4, R.string.dialogok, R.string.dialogcancel, 4);
                //adapter.notifyDataSetChanged();
                break;
        }
        return super.onContextItemSelected(item);
    }
    public void setDisplay() {
        setContentView(R.layout.activity_calendar_day);
        list = makeDataList();
        listView = findViewById(R.id.listview);
        TextView title = (TextView)findViewById(R.id.daymenutitle);
        DayTitle = intent.getStringExtra("key");
        year = DayTitle.substring(0, 4);
        month = DayTitle.substring(5, 7);
        day = DayTitle.substring(8, 10);
        title.setText(month + "月" + day + "日のメニュー");//ここまで初期画面
        FloatingActionButton plusButton = findViewById(R.id.plusbutton);

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDialog(R.string.dialogtitle1, R.string.dialogmessage1, R.string.dialognext, R.string.dialogcancel, 1);
            }
        });
    }
}