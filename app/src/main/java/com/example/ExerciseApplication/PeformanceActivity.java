package com.example.ExerciseApplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;

public class PeformanceActivity extends AppCompatActivity {

    private String[] spinerItems = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    private EditText jissekinen;
    private Spinner jissekituki;
    private Button kensaku;
    private Calendar calendar;
    private int year;
    private int month;
    private String yearString;
    private String monthString;
    private TextView text, keikoku;
    private DatabaseHelper helper;
    private SQLiteDatabase db;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peformance);
        jissekinen = findViewById(R.id.jissekinen);
        jissekituki = findViewById(R.id.jissekituki);
        kensaku = findViewById(R.id.kensaku);
        text = findViewById(R.id.nengetu);
        keikoku = findViewById(R.id.jissekikeikoku);
        listView = findViewById(R.id.jissekilist);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinerItems) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setGravity(Gravity.RIGHT);
                ((TextView) v).setTextSize(20);
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView,parent);
                ((TextView) v).setGravity(Gravity.CENTER);
                return v;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jissekituki.setAdapter(adapter);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        jissekinen.setText("" + year);
        jissekinen.setSelection(jissekinen.getText().length());
        jissekituki.setSelection(month);
        kensaku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yearString = jissekinen.getText().toString();
                monthString = (String)jissekituki.getSelectedItem();
                if(monthString.length() == 1) monthString = "0" + monthString;
                text.setText(yearString + "年" + monthString + "月の実績一覧");
                text.setVisibility(View.VISIBLE);
                ArrayList arrayList = Database();
                ArrayAdapter adapter = new ArrayAdapter<String>(PeformanceActivity.this, android.R.layout.simple_list_item_1, arrayList) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        TextView view = (TextView)super.getView(position, convertView, parent);
                        view.setTextSize(20);
                        return view;
                    }
                };
                listView.setAdapter(adapter);
                if(arrayList.size() == 0) {
                    keikoku.setVisibility(View.VISIBLE);
                }
                else {
                    keikoku.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    public ArrayList Database() {
        String yearmonth = yearString + "-" + monthString + "-";
        helper = new DatabaseHelper(PeformanceActivity.this);
        db = helper.getWritableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            String sql = "SELECT date, menu, value, unit FROM ExerciseMenu WHERE date like '%" + yearmonth + "%' AND complete = 1 ORDER BY date ASC";
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                String w = "";
                int idxNote;
                idxNote = cursor.getColumnIndex("date");
                w += cursor.getString(idxNote) + " ";
                w = w.replace("-", "/");
                idxNote = cursor.getColumnIndex("menu");
                w += cursor.getString(idxNote) + " ";
                idxNote = cursor.getColumnIndex("value");
                w += cursor.getString(idxNote) + " ";
                idxNote = cursor.getColumnIndex("unit");
                w += cursor.getString(idxNote);
                arrayList.add(w);
            }
        }
        finally {
            db.close();
        }
        return arrayList;
    }
}