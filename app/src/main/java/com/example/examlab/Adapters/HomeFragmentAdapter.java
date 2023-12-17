package com.example.examlab.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.examlab.Home_Page_Fragments.*;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class HomeFragmentAdapter extends FragmentStateAdapter {

    public HomeFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new Forum();
            case 2:
                return new Todo();
            case 3:
                return new Profile();
        }
        return new Home();
    }
    @Override
    public int getItemCount() {
        return 4;
    }
}
