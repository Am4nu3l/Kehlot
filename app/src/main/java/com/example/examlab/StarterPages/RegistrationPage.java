package com.example.examlab.StarterPages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examlab.Objects.User;
import com.example.examlab.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class RegistrationPage extends AppCompatActivity {
Spinner Department;
    ArrayAdapter<String> adapter;
    ArrayList<String>departmentList=new ArrayList<>();
    FirebaseFirestore database;
    EditText firstName,middleNam,email,phoneNumber,passWord;
    private Button signUp,backToSignIn;
    private TextView selectedDepartment;
    SharedPreferences userIdentity;
    String[] array;
    private HashMap<String, String> users=new HashMap<String, String>();
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);
        Department=findViewById(R.id.department);
        database=FirebaseFirestore.getInstance();
        firstName=findViewById(R.id.firstname);
        progressBar=findViewById(R.id.progressBar);
        signUp=findViewById(R.id.signUp);
        mAuth=FirebaseAuth.getInstance();
        backToSignIn=findViewById(R.id.backToSignIn);
        middleNam=findViewById(R.id.middle_name);
        email=findViewById(R.id.email);
        phoneNumber=findViewById(R.id.phoneNumber);
        passWord=findViewById(R.id.confirmPassword);
        EditText creatPassword=findViewById(R.id.createPassword);
        Button back=findViewById(R.id.backToSignIn);
        selectedDepartment=findViewById(R.id.selectedDepartment);
        new LoadDepartment().execute();
        backToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
Intent intent=new Intent(RegistrationPage.this,LoginPage.class);
startActivity(intent);
finish();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User(
                        firstName.getText().toString(), middleNam.getText().toString(),
                        email.getText().toString(), Department.getSelectedItem().toString()
                        , phoneNumber.getText().toString(), passWord.getText().toString()
                );
                if (firstName.getText().toString().isEmpty()||middleNam.getText().toString().isEmpty()||
                        email.getText().toString().isEmpty()||phoneNumber.getText().toString().isEmpty()||
                        passWord.getText().toString().isEmpty()){
                    Toast.makeText(RegistrationPage.this, "Please Fill All The Fields!!", Toast.LENGTH_SHORT).show();
                }
                else if(!passWord.getText().toString().equals(creatPassword.getText().toString())){
                    Toast.makeText(RegistrationPage.this, "Password Mismatch!!", Toast.LENGTH_SHORT).show();
                }else{
                ProgressDialog progressBar = new ProgressDialog(RegistrationPage.this);
                progressBar.setCancelable(true);
                progressBar.setCanceledOnTouchOutside(false);
                progressBar.setMessage("please wait...");
                progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressBar.setProgress(0);
                progressBar.setMax(100);
                progressBar.show();
                users.put("U_fname", user.getFirstName());
                users.put("U_mname", user.getMiddleName());
                users.put("U_email", user.getEmail());
                users.put("U_department", user.getDepartment());
                users.put("U_phone_number", user.getPhoneNumber());
                users.put("U_password", user.getPassword());
                users.put("status", "");
                mAuth.createUserWithEmailAndPassword(email.getText().toString(), passWord.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            database.collection("User").add(users).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    progressBar.dismiss();
                                    Toast.makeText(RegistrationPage.this, "Registration Completed", Toast.LENGTH_LONG).show();
                                    Intent intent=new Intent(RegistrationPage.this,LoginPage.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        } else {
                            progressBar.dismiss();
                            Toast.makeText(RegistrationPage.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }}
            });
        }

    private class LoadDepartment extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            progressBar.setVisibility(View.VISIBLE);
            database.collection("Department").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(DocumentSnapshot snapshot:task.getResult()){
                            departmentList.add(snapshot.get("Dep_name").toString());
                        }
                    }
                    adapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, departmentList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Department.setAdapter(adapter);
                }
            });
            return null;
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
         progressBar.setVisibility(View.GONE);
            selectedDepartment.setVisibility(View.VISIBLE);
        }
    }
    }



