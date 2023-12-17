package com.example.examlab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.examlab.Adapters.HomeFragmentAdapter;
import com.example.examlab.StarterPages.LoginPage;
import com.example.examlab.StarterPages.WalkThroughPages;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private com.example.examlab.Adapters.HomeFragmentAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager2 slideHome;
    FirebaseFirestore database;
    private FirebaseAuth mAuth;
    SharedPreferences sharedPreferences1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Build.VERSION.SDK_INT>=23){
            View decore=MainActivity.this.getWindow().getDecorView();
            if(decore.getSystemUiVisibility()!=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR){
                decore.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            else{
                decore.setSystemUiVisibility(0);
            }
        }
         sharedPreferences1=getSharedPreferences("USER_IDENTITY",MODE_PRIVATE);
        tabLayout = findViewById(R.id.tabLayout);
        slideHome = findViewById(R.id.viewPager);
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseFirestore.getInstance();
        Button payment=findViewById(R.id.payment);
        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new HomeFragmentAdapter(fragmentManager, getLifecycle());
        slideHome.setAdapter(adapter);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.home));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.world));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.todo));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.profile));
        if(sharedPreferences1.getString("status",null)!=null&&sharedPreferences1.getString("status",null).equals("paid")){
            payment.setVisibility(View.GONE);
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                slideHome.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        slideHome.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
        Button menu_drop = findViewById(R.id.more);
        menu_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu=new PopupMenu(MainActivity.this,view);
                popupMenu.inflate(R.menu.more);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getTitle().toString().equals("About")){
                            Intent intent=new Intent(MainActivity.this, About.class);
                            startActivity(intent);
                            finish();
                        }
                        else if(menuItem.getTitle().toString().equals("Profile")){
                            tabLayout.selectTab(tabLayout.getTabAt(3));            }
                        return true;
                    }
                });
            }
        });
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
Intent intent=new Intent(MainActivity.this,Payment.class);
startActivity(intent);
finish();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
                Intent intent = new Intent(MainActivity.this, WalkThroughPages.class);
                startActivity(intent);
                finish();
        }
    }
}