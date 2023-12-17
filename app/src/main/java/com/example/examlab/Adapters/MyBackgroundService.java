package com.example.examlab.Adapters;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.examlab.Objects.Question;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

    public class MyBackgroundService extends Service {

        private ArrayList<Question> questions;
        private MyQuestionAdapter adapter;
        private FirebaseFirestore database;

        @Override
        public void onCreate() {
            super.onCreate();
            questions = new ArrayList<>();
            adapter = new MyQuestionAdapter(questions);
            database=FirebaseFirestore.getInstance();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Log.d("ser", "servise started: ");
                    database.collection("Question").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot snapshot:task.getResult()){
                        Question qq=new Question(snapshot.get("questionerName").toString()
                                ,snapshot.get("questioner").toString(), snapshot.getId(), snapshot.get("question").toString());
                        questions.add(qq);
                    }
                }
            }
        });
            adapter.notifyDataSetChanged();

            // Return the appropriate start command value
            return START_STICKY;
        }

        public ArrayList<Question> getQuestions() {
            return questions;
        }
        public class LocalBinder extends Binder {
            public MyBackgroundService getService() {
                return MyBackgroundService.this;
            }
        }
        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }


