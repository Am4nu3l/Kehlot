package com.example.examlab.ContentView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.example.examlab.Adapters.video_list_adapter;
import com.example.examlab.Objects.Content;
import com.example.examlab.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;

public class video_fragment extends Fragment {
    private FirebaseFirestore database;
    private SharedPreferences courseIdentity;
    private ArrayList<Content> videoList=new ArrayList<>();
    private AbsListView listView;
    private video_list_adapter adapter;
    private YouTubePlayerView youTubePlayerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_video_fragment, container, false);
        database=FirebaseFirestore.getInstance();
        listView=view.findViewById(R.id.video_list);
        youTubePlayerView =view.findViewById(R.id.youtube);
        courseIdentity = getActivity().getSharedPreferences("COURSE_IDENTITY", Context.MODE_PRIVATE);
        new LoadCourse().execute();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                youTubePlayerView.getYouTubePlayerWhenReady(new YouTubePlayerCallback() {
                    @Override
                    public void onYouTubePlayer(@NonNull YouTubePlayer youTubePlayer) {
                        youTubePlayer.loadVideo(videoList.get(i).getLink(), 0);
                    }
                });
            }
        });
        return view;
    }
    private class LoadCourse extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            database.collection(courseIdentity.getString("final_path", ""))
                    .document(courseIdentity.getString("course_id", "null"))
                    .collection("Video")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> tasked) {
                            Log.d("task", "onComplete: " + tasked.getResult().size());
                            for (DocumentSnapshot snapshots : tasked.getResult()) {
                                Log.d("mytask", "onComplete: " + snapshots.getId());
                                Content content=new Content(snapshots.get("title").toString(),snapshots.get("link").toString());
                                videoList.add(content);
                            }
                            adapter=new video_list_adapter(getActivity(),videoList);
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
    }
}