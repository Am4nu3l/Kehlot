package com.example.examlab.ContentView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.KeyboardView;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examlab.Adapters.fragment_Adapter;
import com.example.examlab.MainActivity;
import com.example.examlab.R;
import com.example.examlab.pdfopener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BrowsContent extends AppCompatActivity {
  private TabLayout t;
  private ViewPager2 v;
  private EditText ed_text;
  private Button menu_drop, back_btn;
  fragment_Adapter adapter;
  TextView title, tab_title;
  private SharedPreferences courseIdentity;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_brows_content);
    courseIdentity = getSharedPreferences("COURSE_IDENTITY", MODE_PRIVATE);
     isOnline();
    if (Build.VERSION.SDK_INT >= 23) {
      View decore = BrowsContent.this.getWindow().getDecorView();
      if (decore.getSystemUiVisibility() != View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) {
        decore.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
      } else {
        decore.setSystemUiVisibility(0);
      }
    }
    title = findViewById(R.id.titles_in_main_view);
    title.setText(courseIdentity.getString("user_id", ""));
    t = findViewById(R.id.tabLayout);
    v = findViewById(R.id.viewPager);
    back_btn = findViewById(R.id.back);
    tab_title = findViewById(R.id.tab_text);
    FragmentManager fragmentManager = getSupportFragmentManager();
    adapter = new fragment_Adapter(fragmentManager, getLifecycle());
    v.setAdapter(adapter);
    String[] titles = {"Notes", "Videos", "Practice", "Books"};
    tab_title.setText("Notes");
    t.addTab(t.newTab().setIcon(R.drawable.ic_note));
    t.addTab(t.newTab().setIcon(R.drawable.ic_play));
    t.addTab(t.newTab().setIcon(R.drawable.ic_subject));
    t.addTab(t.newTab().setIcon(R.drawable.ic_book_3));
    t.addOnTabSelectedListener(
        new TabLayout.OnTabSelectedListener() {
          @Override
          public void onTabSelected(TabLayout.Tab tab) {
            v.setCurrentItem(tab.getPosition());
            tab_title.setText(titles[tab.getPosition()]);
          }

          @Override
          public void onTabUnselected(TabLayout.Tab tab) {}

          @Override
          public void onTabReselected(TabLayout.Tab tab) {}
        });
    v.registerOnPageChangeCallback(
        new ViewPager2.OnPageChangeCallback() {
          @Override
          public void onPageSelected(int position) {
            super.onPageSelected(position);
            t.selectTab(t.getTabAt(position));
          }
        });
    menu_drop = findViewById(R.id.menu);
    menu_drop.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
            popupMenu.inflate(R.menu.more);
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {
                  @Override
                  public boolean onMenuItemClick(MenuItem menuItem) {
                    //                        Intent intent=new
                    // Intent(BrowsContent.this,settings.class);
                    //                        startActivity(intent);
                    return true;
                  }
                });
          }
        });
    back_btn.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Intent intent = new Intent(BrowsContent.this, MainActivity.class);
            startActivity(intent);
          }
        });
  }

  public boolean isOnline() {
    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
      return true;
    } else {
      Toast.makeText(this, "You Are Not Connected to The Internet", Toast.LENGTH_LONG).show();
      return false;
    }
  }
       }