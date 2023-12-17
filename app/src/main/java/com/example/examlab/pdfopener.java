package com.example.examlab;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.examlab.ContentView.BrowsContent;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class pdfopener extends AppCompatActivity {
private PDFView pdfView;
    ProgressDialog pd;
    private Button back,download;
    String pdfLink;
TextView titleview;
    TextView subtitleview;
    Button menu_drop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfopener);
        if(Build.VERSION.SDK_INT>=23){
            View decore=pdfopener.this.getWindow().getDecorView();
            if(decore.getSystemUiVisibility()!=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR){
                decore.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            else{
                decore.setSystemUiVisibility(0);
            }
        }
        haveStoragePermission();
        titleview=findViewById(R.id.title_in_pdf_view);
        subtitleview=findViewById(R.id.sub_title_in_pdf_view);
        String sub=getIntent().getStringExtra("subtitle");
        String title_of_pdfview=getIntent().getStringExtra("title");
        pdfLink=getIntent().getStringExtra("pdfLink");
        back=findViewById(R.id.back);
        download=findViewById(R.id.menu);
        titleview.setText(title_of_pdfview);
        subtitleview.setText(sub);
        pdfView=findViewById(R.id.pdfview);
        Log.d("nigga", "onCreate: "+pdfLink);
        pd = new ProgressDialog(pdfopener.this,R.style.MyProgressTheme);
        pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pd.show();
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadPdf(sub,pdfLink);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(pdfopener.this, BrowsContent.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        new RetrivePdfStream().execute(pdfLink);
    }
    class RetrivePdfStream extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            }
            catch (IOException e) {
                return null;
            }
            return inputStream;
        }
        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream).enableAnnotationRendering(true).load();
            pd.dismiss();
        }
    }
    private void downloadPdf(String filename, String downloadUrlOfImage){
        try{
            DownloadManager dm = (DownloadManager)getApplication().getSystemService(getApplication().DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(downloadUrlOfImage);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(filename)
                    .setMimeType("pdf/pdf") // Your file type. You can use this code to download other file types also.
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, File.separator + filename + ".pdf");
            dm.enqueue(request);
            Toast.makeText(getApplication(), "Saving....", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(getApplication(), "Saving..... failed."+e, Toast.LENGTH_SHORT).show();
        }
    }
    public  boolean haveStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("Permission error","You have permission");
                return true;
            } else {
                Log.e("Permission error","You have asked for permission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //you dont need to worry about these stuff below api level 23
            Log.e("Permission error","You already have the permission");
            return true;
        }
    }
}
