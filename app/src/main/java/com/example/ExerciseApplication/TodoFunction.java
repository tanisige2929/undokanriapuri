package com.example.ExerciseApplication;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
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

import androidx.appcompat.app.AlertDialog;
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
    private int listPosition;
    private int henshuflag = -1;
    private boolean keikokuflag = false;
    private AlertDialog alertDialog;
    private String menuname = "";
    private String menuvalue = "";
    private String menuunit = "";
    private int datasetflag = -1;

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
                registerForContextMenu(listViewue);
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
    public void makeDialog(int title, int message, int ok, int cancel, int flag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TodoFunction.this);
        if(flag == 1) {
            LayoutInflater layoutInflater = LayoutInflater.from(TodoFunction.this);
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
                            databaseAction(1);
                            Toast.makeText(getApplicationContext(), "登録が完了しました", Toast.LENGTH_SHORT).show();
                            datasetflag = 0;
                            TextView nomenu = (TextView) findViewById(R.id.NoMenu);
                            nomenu.setVisibility(View.INVISIBLE);
                        } else {
                            databaseAction(3);
                            Toast.makeText(getApplicationContext(), "編集が完了しました", Toast.LENGTH_SHORT).show();
                            henshuflag = -1;
                            datasetflag = -1;
                        }
                        databaseAction(2);
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
                    databaseAction(4);
                    datasetflag = -1;
                    databaseAction(2);
                    Toast.makeText(getApplicationContext(), "削除しました", Toast.LENGTH_SHORT).show();
                    if(idList.size() == 0) {
                        System.out.println(idList.size());
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
    public void databaseAction(int flag) {
        DatabaseHelper helper = new DatabaseHelper(TodoFunction.this);
        SQLiteDatabase db = helper.getWritableDatabase();
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
                    listViewue.setAdapter(adapter);
                }
                registerForContextMenu(listViewue);
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
}
