package com.example.ExerciseApplication;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class FragmentCalendarAdapter extends FragmentStatePagerAdapter {
    Context context;
    public FragmentCalendarAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int i) {
        //System.out.println("i:" + i);
        return new CalendarFragment(context, i);
    }

    @Override
    public int getCount() {
        return 21;
    }

}
