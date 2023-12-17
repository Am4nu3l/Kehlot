package com.example.examlab.StarterPages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examlab.MainActivity;
import com.example.examlab.R;
import com.example.examlab.ResetPassword;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginPage extends AppCompatActivity {
private Button signIn;
    private EditText email;
    private EditText password;
    private FirebaseAuth mAuth;
    private FirebaseFirestore database;
    TextView register;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        if(Build.VERSION.SDK_INT>=23){
            View decore=LoginPage.this.getWindow().getDecorView();
            if(decore.getSystemUiVisibility()!=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR){
                decore.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            else{
                decore.setSystemUiVisibility(0);
            }
        }
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        register=findViewById(R.id.goToRegister);
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseFirestore.getInstance();
        TextView forgotPassword=findViewById(R.id.forgotPassword);
        signIn=findViewById(R.id.signIn);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginPage.this, ResetPassword.class);
                startActivity(intent);
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    Toast.makeText(LoginPage.this, "email or password is empty", Toast.LENGTH_LONG).show();
                } else {
                    ProgressDialog progressBar = new ProgressDialog(LoginPage.this);
                    progressBar.setCancelable(true);
                    progressBar.setCanceledOnTouchOutside(false);
                    progressBar.setMessage("please wait...");
                    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressBar.setProgress(0);
                    progressBar.setMax(100);
                    progressBar.show();
                    mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> tasks) {
                            if (tasks.isSuccessful()) {
                                database.collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        sharedPreferences=getSharedPreferences("USER_IDENTITY",MODE_PRIVATE);
                                        for (DocumentSnapshot snapshot : task.getResult()) {
                                            if (snapshot.get("U_email").equals(tasks.getResult().getUser().getEmail())) {
                                                progressBar.dismiss();
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("user_id", snapshot.getId());
                                                editor.putString("usrFName", snapshot.get("U_fname").toString());
                                                editor.putString("usrMName", snapshot.get("U_mname").toString());
                                                editor.putString("usrDep", snapshot.get("U_department").toString());
                                                editor.putString("usrPn", snapshot.get("U_phone_number").toString());
                                                editor.putString("email", snapshot.get("U_email").toString());
                                                editor.putString("status", snapshot.get("status").toString());
                                                editor.commit();
                                                Intent intent = new Intent(LoginPage.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    }
                                });
                            } else {
                                progressBar.dismiss();
                                Toast.makeText(LoginPage.this, tasks.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });
    }

        public void Register () {
            Intent intent = new Intent(LoginPage.this, RegistrationPage.class);
            startActivity(intent);
            finish();
        }
    }
