package com.example.examlab.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.examlab.ContentView.books_fragment;
import com.example.examlab.ContentView.notes_fragment;
import com.example.examlab.ContentView.test_fragment;
import com.example.examlab.ContentView.video_fragment;

public class fragment_Adapter extends FragmentStateAdapter {
    public fragment_Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }
    @NonNull
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new video_fragment();
            case 2:
                return new test_fragment();
            case 3:
                return new books_fragment();
        }
        return new notes_fragment();
    }
    @Override
    public int getItemCount() {
        return 4;
    }
}
