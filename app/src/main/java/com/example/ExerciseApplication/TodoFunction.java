package com.example.ExerciseApplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class TodoFunction extends AppCompatActivity {
    private Context context;
    private Calendar calendar;
    private String DayTitle;
    private String w;
    private int dateNow;
    private int yearNow;
    private int monthNow;
    private String dayString;
    private DatabaseHelper helper;
    private SQLiteDatabase db;
    private ArrayList idList;
    private ListView listViewue;
    private ListView listViewsita;
    private ArrayList itemue;
    private ArrayList itemsita;
    private ArrayAdapter adapterue;
    private ArrayAdapter adaptersita;

    TodoFunction(Context context, ListView listViewue, ListView listViewsita) {
        this.context = context;
        this.listViewue = listViewue;
        this.listViewsita = listViewsita;
        calendar = Calendar.getInstance();
        dateNow = calendar.get(Calendar.DATE);
        yearNow = calendar.get(Calendar.YEAR);
        monthNow = calendar.get(Calendar.MONTH);
        monthNow++;
        dayString = "" + yearNow + "-";
        w = "" + monthNow;
        if(w.length() == 1) dayString += "0" + w + "-";
        else dayString += w + "-";
        w = "" + dateNow;
        if(w.length() == 1) dayString += "0" + w;
        else dayString += w;
        System.out.println(dayString);

        itemue = new ArrayList<String>();
        itemsita = new ArrayList<String>();

    }

    public void todoDatabase(int flag, int position) {
        helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
        if(flag == 1) {
            try {
                String sql = "SELECT _id, menu, value, unit FROM ExerciseMenu WHERE date = '" + dayString + "' AND complete = -1";
                Cursor cursor = db.rawQuery(sql, null);
                int id;
                String menu = "";
                String value = "";
                String unit = "";
                idList = new ArrayList<>();
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
                    itemue.add("" + menu + " " + value + unit);
                    System.out.println(itemue);
                }
                adapterue = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_single_choice, itemue) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        TextView view = (TextView)super.getView(position, convertView, parent);
                        view.setTextSize(20);
                        return view;
                    }
                };
                listViewue.setAdapter(adapterue);
                sql = "SELECT _id, menu, value, unit FROM ExerciseMenu WHERE date = '" + dayString + "' AND complete = 1";
                cursor = db.rawQuery(sql, null);
                menu = "";
                value = "";
                unit = "";
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
                    itemsita.add("" + menu + " " + value + unit);
                    System.out.println(itemsita);
                }
                adaptersita = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, itemsita) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        TextView view = (TextView)super.getView(position, convertView, parent);
                        view.setTextSize(20);
                        return view;
                    }
                };
                listViewsita.setAdapter(adaptersita);
            }
            finally {
                db.close();
            }
        }
        else if(flag == 2) {
            try {
                String sqlUpdate = "UPDATE Exercisemenu SET complete = 1 WHERE _id = " + idList.get(position);
                db.execSQL(sqlUpdate);
            }
            finally {
                db.close();
            }
        }
    }
    public void adapterChange(String w) {
        adapterue.remove(w);
        adapterue.notifyDataSetChanged();
        adaptersita.add(w);
        adaptersita.notifyDataSetChanged();
    }
}
