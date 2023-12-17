package com.example.examlab.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.examlab.StarterPages.WalkThroughPage1;
import com.example.examlab.StarterPages.WalkThroughPage2;
import com.example.examlab.StarterPages.WalkThroughPage3;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_PAGES = 3;
    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
        return new WalkThroughPage1();
            case 1:
        return new WalkThroughPage2();
            case 2:
        return new WalkThroughPage3();
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}

