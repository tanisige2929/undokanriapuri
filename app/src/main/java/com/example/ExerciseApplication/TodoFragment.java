package com.example.ExerciseApplication;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment todoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodoFragment newInstance(String param1, String param2) {
        TodoFragment fragment = new TodoFragment();
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
        //todo機能
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        final ListView listViewue = (ListView)view.findViewById(R.id.todaymenulist);
        final ListView listViewsita = (ListView)view.findViewById(R.id.todaycompletemenulist);
        final TodoFunction todaymenu = new TodoFunction(getActivity(), listViewue, listViewsita);
        todaymenu.todoDatabase(1, -1);
        //listViewue.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right);
                animation.setDuration(50);
                listViewue.getChildAt(position).startAnimation(animation);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listViewue.remove();
                    }
                }), animation.getDuration();*/
                todaymenu.todoDatabase(2, position);
                todaymenu.adapterChange(parent.getItemAtPosition(position).toString());
                //System.out.println(parent.getItemAtPosition(position).toString());
                //adapter.notifyDataSetChanged();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

}