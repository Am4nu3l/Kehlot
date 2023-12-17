package com.example.examlab.StarterPages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.content.res.ColorStateList;
import android.view.View;
import androidx.core.content.ContextCompat;

import com.example.examlab.Adapters.MyPagerAdapter;
import com.example.examlab.MainActivity;
import com.example.examlab.R;
import com.google.android.material.tabs.TabLayout;

public class WalkThroughPages extends AppCompatActivity {
    View view1,view2,view3;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_through_pages);
        SharedPreferences sharedPreferences=getSharedPreferences("FON",MODE_PRIVATE);
        if(Build.VERSION.SDK_INT>=23){
            View decore= this.getWindow().getDecorView();
            if(decore.getSystemUiVisibility()!=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR){
                decore.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            else{
                decore.setSystemUiVisibility(0);
            }
        }
        view1=findViewById(R.id.view1);
        view2=findViewById(R.id.view2);
        view3=findViewById(R.id.view3);
        ViewPager viewPager = findViewById(R.id.viewPager);
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        int yellow = ContextCompat.getColor(this, R.color.yellow);
        int blue = ContextCompat.getColor(this, R.color.blue);
        view1.setBackgroundTintList(ColorStateList.valueOf(yellow));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @SuppressLint("ResourceAsColor")
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        view1.setBackgroundTintList(ColorStateList.valueOf(yellow));
                        view3.setBackgroundTintList(ColorStateList.valueOf(blue));
                        view2.setBackgroundTintList(ColorStateList.valueOf(blue));
                        break;
                    case 1:
                        view2.setBackgroundTintList(ColorStateList.valueOf(yellow));
                        view1.setBackgroundTintList(ColorStateList.valueOf(blue));
                        view3.setBackgroundTintList(ColorStateList.valueOf(blue));
                        break;
                    case 2:
                        view3.setBackgroundTintList(ColorStateList.valueOf(yellow));
                        view1.setBackgroundTintList(ColorStateList.valueOf(blue));
                        view2.setBackgroundTintList(ColorStateList.valueOf(blue));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // This method is called when the scroll state changes.
                // You can perform any necessary actions based on the scroll state.
            }
        });
    }
}