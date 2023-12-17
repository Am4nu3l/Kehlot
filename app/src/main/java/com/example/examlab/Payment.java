package com.example.examlab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.ProgressBar;

import com.example.examlab.StarterPages.LoginPage;
import com.google.firebase.auth.FirebaseAuth;

import java.util.UUID;

public class Payment extends AppCompatActivity {
ProgressBar progressBar;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_payment);
    if(Build.VERSION.SDK_INT>=23){
      View decore=Payment.this.getWindow().getDecorView();
      if(decore.getSystemUiVisibility()!=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR){
        decore.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
      }
      else{
        decore.setSystemUiVisibility(0);
      }
    }
    WebView webView =findViewById(R.id.paymentWebView);
    progressBar=findViewById(R.id.spin_kit);
    progressBar.setVisibility(View.VISIBLE);
    webView.getSettings().setJavaScriptEnabled(true);
    webView.setWebViewClient(
        new WebViewClient() {
          @Override
          public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // Check if the URL is the confirmation page URL
            if (url.equals("https://kihilot-pay.onrender.com/pay")) {
              webView.loadUrl(url);
              return true;
            }
            Log.d("url", "shouldOverrideUrlLoading: " + url);
            return super.shouldOverrideUrlLoading(view, url);
          }

          @Override
          public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
          }

          @Override
          public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (url.equals("https://kihilot-pay.onrender.com/pay")) {
              Intent intent = new Intent(Payment.this, LoginPage.class);
              startActivity(intent);
            }
            String token = UUID.randomUUID().toString().replace("-", "");
            progressBar.setVisibility(View.GONE);
            SharedPreferences sharedPreferences =
                getSharedPreferences("USER_IDENTITY", MODE_PRIVATE);
            String email=sharedPreferences.getString("email",null);
            String javascript2 = "document.getElementById('tex_ref').value = '" + token + "';";
            String javascript3 = "document.getElementById('inp_amount').value = '" + "700" + "';";
            String javascript2_1 = "document.getElementById('tex_ref').readOnly = true;";
            String javascript1 = "document.getElementById('inp_email').readOnly = true;";
            String javascript4 = "document.getElementById('inp_amount').readOnly = true;";
            String javascript = "document.getElementById('inp_email').value = '" + email + "';";
            webView.evaluateJavascript(javascript2, null);
            webView.evaluateJavascript(javascript3, null);
            webView.evaluateJavascript(javascript2_1, null);
            webView.evaluateJavascript(javascript1, null);
            webView.evaluateJavascript(javascript4, null);
            webView.evaluateJavascript(javascript, null);
          }
        });
    webView.loadUrl("https://kihilot-pay.onrender.com");
    Button menu_drop = findViewById(R.id.more);
    Button back=findViewById(R.id.back);
    back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent=new Intent(Payment.this, MainActivity.class);
        startActivity(intent);
        finish();
      }
    });
    menu_drop.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        PopupMenu popupMenu=new PopupMenu(Payment.this,view);
        popupMenu.inflate(R.menu.more);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
          @Override
          public boolean onMenuItemClick(MenuItem menuItem) {
            FirebaseAuth mAuth=FirebaseAuth.getInstance();
            if(menuItem.getTitle().toString().equals("About")){
              Intent intent=new Intent(Payment.this, LoginPage.class);
              mAuth.signOut();
              startActivity(intent);

            }
            else if(menuItem.getTitle().toString().equals("Profile")){
              Intent intent=new Intent(Payment.this, MainActivity.class);
              startActivity(intent);

                       }
            return true;
          }
        });
      }
    });
  }
}
