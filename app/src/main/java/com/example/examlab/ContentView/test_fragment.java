package com.example.examlab.ContentView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import androidx.fragment.app.Fragment;

import com.example.examlab.Adapters.test_list_adapter;
import com.example.examlab.Adapters.video_list_adapter;
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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;


public class test_fragment extends Fragment {
    private FirebaseFirestore database;
    private SharedPreferences courseIdentity;
    ArrayList<Content> titles=new ArrayList<>();
    private AbsListView listView;
    private video_list_adapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_test_fragment, container, false);
        database=FirebaseFirestore.getInstance();
        listView=view.findViewById(R.id.test_rows);
        courseIdentity = getActivity().getSharedPreferences("COURSE_IDENTITY", Context.MODE_PRIVATE);
        new LoadCourse().execute();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getActivity(),pdfopener.class);
                intent.putExtra("pdfLink",titles.get(i).getLink());
                intent.putExtra("title",courseIdentity.getString("course_id",""));
                intent.putExtra("subtitle",titles.get(i).getTitle());
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
                    .collection("Test")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> tasked) {
                            Log.d("task", "onComplete: " + tasked.getResult().size());
                            for (DocumentSnapshot snapshots : tasked.getResult()) {
                                Log.d("mytask", "onComplete: " + snapshots.getId());
                                Content content=new Content(snapshots.get("title").toString(),snapshots.get("link").toString());
                                titles.add(content);
                            }
                            adapter=new video_list_adapter(getActivity(),titles);
                            listView.setAdapter(adapter);

                        }
                    });
            return null;
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Log.d("deps", "I hate this: video");
        }
    }}