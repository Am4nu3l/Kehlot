package com.example.examlab.ContentView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.examlab.Adapters.Course_list_Adapter;
import com.example.examlab.Adapters.Note_List_Adapter;
import com.example.examlab.Home_Page_Fragments.Home;
import com.example.examlab.Objects.Content;
import com.example.examlab.R;
import com.example.examlab.pdfopener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class notes_fragment extends Fragment {

    private SharedPreferences courseIdentity;
private FirebaseFirestore database;
private String depDocument;
    ArrayList<Content> contents=new ArrayList<>();
    Note_List_Adapter adapter;
    private ArrayList<Content> courseList=new ArrayList<>();
    private AbsListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes_fragment, container, false);
        courseIdentity = getActivity().getSharedPreferences("COURSE_IDENTITY", Context.MODE_PRIVATE);
        database = FirebaseFirestore.getInstance();
        listView=view.findViewById(R.id.notes_list);
        new LoadCourse().execute();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getActivity(),pdfopener.class);
                intent.putExtra("pdfLink",courseList.get(i).getLink());
                intent.putExtra("title",courseIdentity.getString("course_id",""));
                intent.putExtra("subtitle",courseList.get(i).getTitle());
                startActivity(intent);
            }
        });
    return view;
    }
    private class LoadCourse extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            database.collection(courseIdentity.getString("final_path", ""))
                    .document(courseIdentity.getString("course_id", "null"))
                    .collection("Note")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> tasked) {
                            depDocument=tasked.getResult().getMetadata().toString();
                            Log.d("task", "onComplete: " + tasked.getResult().size());
                            for (DocumentSnapshot snapshots : tasked.getResult()) {
                                Log.d("mytask", "onComplete: " + snapshots.getId());
                                  Content content=new Content(snapshots.get("title").toString(),snapshots.get("link").toString());
                                  courseList.add(content);
                            }
                            adapter=new Note_List_Adapter(getActivity(),courseList);
                            listView.setAdapter(adapter);
                        }
                    });
            return null;
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Log.d("deps", "I hate this: ");
        }
    }

}