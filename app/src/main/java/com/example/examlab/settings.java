package com.example.examlab;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class settings extends AppCompatActivity{
    private Button menu_drop,back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if(Build.VERSION.SDK_INT>=23){
            View decore=settings.this.getWindow().getDecorView();
            if(decore.getSystemUiVisibility()!=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR){
                decore.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            else{
                decore.setSystemUiVisibility(0);
            }
        }
//        back_btn=findViewById(R.id.back);
//        back_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(settings.this,MainActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}