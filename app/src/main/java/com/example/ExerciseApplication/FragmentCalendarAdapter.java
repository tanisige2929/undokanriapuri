package com.example.ExerciseApplication;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class FragmentCalendarAdapter extends FragmentStatePagerAdapter {
    MainActivity mainactivity;
    public FragmentCalendarAdapter(FragmentManager fm, MainActivity mainactivity) {
        super(fm);
        this.mainactivity = mainactivity;
    }

    @Override
    public Fragment getItem(int i) {
        //System.out.println("i:" + i);
        return new CalendarFragment(mainactivity, i);
    }

    @Override
    public int getCount() {
        return 21;
    }

}
