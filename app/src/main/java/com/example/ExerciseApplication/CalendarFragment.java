package com.example.ExerciseApplication;

import android.content.Context;
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

    private Context context;
    private  int page;
    private Intent intent;
    private int month;
    private String yearmonth = "";
    private String yearmonthfirst;
    private TextView tw;

    public CalendarFragment(Context context, int page) {
        // Required empty public constructor
        this.context = context;
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
        Calendarfunction cf;
        TextView calendarTitle;
        TextView[] textcl = new TextView[42];

        //Intent[] intent = new Intent[42];
        View view = inflater.inflate(R.layout.fragment_calendar, null);
        int clId;
        //int i = 0;
        String calendarViewname;
        for(int i = 0; i < textcl.length; i++) {
            calendarViewname = "c" + i;
            clId = getResources().getIdentifier(calendarViewname, "id", getActivity().getPackageName());
            textcl[i] = (TextView)view.findViewById(clId); //ID紐づけ
            //intent[i] = new Intent()
        }
        calendarTitle = (TextView)view.findViewById(R.id.calendartitle);
        cf = new Calendarfunction(page, context);//二重処理？後で直す
        yearmonth = cf.CalendarSet(textcl, calendarTitle);//ここまでカレンダー生成
        yearmonthfirst = yearmonth;
        //CreateDayFragment(textcl);
        int i;
        for(i = 0; textcl[i].getText().equals(" "); i++);
        for( ;  i < textcl.length && !(textcl[i].getText().equals(" ")); i++) {
            textcl[i].setClickable(true);
            tw = textcl[i];
            textcl[i].setOnClickListener(this);
            System.out.println("空白" + i + "text" + textcl[i].getText());
        }
        return view;
    }
    public void onClick(View view) {
        String day = "";
        yearmonth = yearmonthfirst;
        TextView t = (TextView)view;
        day = t.getText().toString();
        AlphaAnimation anime = new AlphaAnimation(0, 2);
        anime.setDuration(200);
        t.startAnimation(anime);
        if(day.length() == 1) day = "0" + day;
        yearmonth += day;
        Intent intent = new Intent(getActivity(), CalendarDayActivity.class);
        intent.putExtra("key", yearmonth);
        startActivity(intent);
    }
}