package com.example.ExerciseApplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MainActivity mainactivity;
    private  int page;
    private Intent intent;
    private int month;
    private String yearmonth = "";
    private String yearmonthfirst;
    private TextView tw;

    public CalendarFragment(MainActivity mainactivity, int page) {
        // Required empty public constructor
        this.mainactivity = mainactivity;
        this.page = page;
        System.out.println("page:" + page);
    }
    public CalendarFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Calendarfunction cl;
        TextView calendarTitle;
        TextView[] textcl = new TextView[42];

        //Intent[] intent = new Intent[42];
        View view = inflater.inflate(R.layout.fragment_calendar, null);
        int clId;
        //int i = 0;
        String calendarViewname;
        for(int i = 0; i < textcl.length; i++) {
            calendarViewname = "c" + i;
            clId = getResources().getIdentifier(calendarViewname, "id", mainactivity.getPackageName());
            textcl[i] = (TextView)view.findViewById(clId); //ID紐づけ
            //intent[i] = new Intent()
        }
        calendarTitle = (TextView)view.findViewById(R.id.calendartitle);
        cl = new Calendarfunction(page, textcl);//二重処理？後で直す
        yearmonth = cl.CalendarSet(textcl, calendarTitle);//ここまでカレンダー生成
        yearmonthfirst = yearmonth;
        //CreateDayFragment(textcl);
        int i;
        for(i = 0; textcl[i].getText().equals(" "); i++);
        for( ;  i < textcl.length && !(textcl[i].getText().equals(" ")); i++) {
            textcl[i].setClickable(true);
            tw = textcl[i];
            textcl[i].setOnClickListener(this);
            System.out.println("空白" + i + "text" + textcl[i].getText());
            /*textcl[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textcl[i].setBackgroundColor(Color.GRAY);
                    Intent intent = new Intent(getActivity(), CalendarDayActivity.class);
                    intent.putExtra("key", monthday);
                    startActivity(intent);
                }
            });*/
        }
        return view;
    }
    /*public void CreateDayFragment(TextView[] text) {
        for(int i = 0; i < text.length; i++) {
            text[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Fragment DayFragment;
                    FragmentManager fragmentManager = getFragmentManager();
                    if(fragmentManager != null) {
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        //fragmentTransaction.replace(R.id.CalendarFragment, );
                        fragmentTransaction.commit();
                    }
                }
            });
        }
    }*/
    public void onClick(View view) {
        String day = "";
        yearmonth = yearmonthfirst;
        TextView t = (TextView)view;
        /*if(view == tw) {
            t = tw;
        }*/
        //TextView t = (TextView)view;
        day = t.getText().toString();
        //t.setBackgroundColor(Color.GRAY);
        AlphaAnimation anime = new AlphaAnimation(0, 2);
        anime.setDuration(200);
        t.startAnimation(anime);
        //t.setBackgroundColor(Color.WHITE);
        //CharSequence dayText = t.getText();
        //int day = Integer.parseInt(s);
        if(day.length() == 1) day = "0" + day;
        yearmonth += day;
        Intent intent = new Intent(getActivity(), CalendarDayActivity.class);
        intent.putExtra("key", yearmonth);
        //intent.putExtra("color", t);
        startActivity(intent);
        //view.setBackgroundColor(Color.WHITE);
    }
}