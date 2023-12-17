package com.example.examlab.Home_Page_Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.hardware.lights.LightsManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examlab.Adapters.Course_list_Adapter;
import com.example.examlab.Adapters.LectureAdapter;
import com.example.examlab.Adapters.RecyclerTouchListener;
import com.example.examlab.ContentView.BrowsContent;
import com.example.examlab.MainActivity;
import com.example.examlab.Objects.Lecture;
import com.example.examlab.R;
import com.example.examlab.StarterPages.RegistrationPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends Fragment {
private ListView listView;
private FirebaseFirestore database;
private ArrayList<String>courseList=new ArrayList<>();
private ArrayList<Lecture>lectures=new ArrayList<>();
private SharedPreferences userIdentity,courseIdentity;
    View view;
    private String depDocument="Department";
    private  RecyclerView lectureRv;
    TextView facebook,instagram,name,description;
    CircleImageView lr_image;
  private LinearLayoutManager layoutManager;
    private int counter = 0;
    private ExpandableLayout expandableLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     view= inflater.inflate(R.layout.fragment_home, container, false);
        String[] titles=getResources().getStringArray(R.array.courses);
        facebook=view.findViewById(R.id.facebook);
        instagram=view.findViewById(R.id.instagram);
        name=view.findViewById(R.id.lr_name);
        description=view.findViewById(R.id.lr_description);
        lr_image=view.findViewById(R.id.lr_picture);
        listView=view.findViewById(R.id.courseListView);
        TextView controller=view.findViewById(R.id.controller);
        expandableLayout = view.findViewById(R.id.expandable_layout);
        layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
    lectureRv=view.findViewById(R.id.lecturerv);
        lectureRv.setLayoutManager(layoutManager);
        int collaps = R.drawable.collaps;
        int drop = R.drawable.dropdown;
        Drawable drawable = getResources().getDrawable(R.drawable.line2);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(lectureRv.getContext(), layoutManager.getOrientation());
       dividerItemDecoration.setDrawable(drawable);
        lectureRv.addItemDecoration(dividerItemDecoration);
        userIdentity=getActivity().getSharedPreferences("USER_IDENTITY", Context.MODE_PRIVATE);
        courseIdentity=getActivity().getSharedPreferences("COURSE_IDENTITY", Context.MODE_PRIVATE);
        database=FirebaseFirestore.getInstance();
        new LoadCourse().execute();
        controller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter == 0) {
                    expandableLayout.expand();
                    controller.setCompoundDrawablesWithIntrinsicBounds(0, 0, collaps, 0);
                    controller.setCompoundDrawablePadding(8);
                    counter = +1;
                } else if (counter == 1) {
                    expandableLayout.collapse();
                    controller.setCompoundDrawablesWithIntrinsicBounds(0, 0, drop, 0);
                    controller.setCompoundDrawablePadding(8);
                    counter = 0;
                }
            }
        });
        lectureRv.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                lectureRv,new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                expandableLayout.expand();
                controller.setCompoundDrawablesWithIntrinsicBounds(0, 0, collaps, 0);
                controller.setCompoundDrawablePadding(8);
                counter = +1;
                name.setText(lectures.get(position).getFullName());
                description.setText(lectures.get(position).getDescription());
                Picasso.get().load(lectures.get(position).getImage()).into(lr_image);
                facebook.setText(lectures.get(position).getFaceBook());
                instagram.setText(lectures.get(position).getInstaGram());
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));
listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        SharedPreferences.Editor editor=courseIdentity.edit();
        editor.putString("final_path",depDocument);
        editor.putString("course_id",courseList.get(i));
        editor.commit();
        Intent intent=new Intent(getActivity(), BrowsContent.class);
        startActivity(intent);
    }
});
        return view;
    }
    private class LoadCourse extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            database.collection("Department").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(DocumentSnapshot snapshot:task.getResult()){
                            if(snapshot.get("Dep_name").equals(userIdentity.getString("usrDep",""))){
                                depDocument=depDocument+"/"+snapshot.getId()+"/Course";
                            }
                        }
                        Log.d("depdoc", "onCreateView: "+depDocument);
                        database.collection(depDocument).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for(DocumentSnapshot snapshot:task.getResult()){
                                    Log.d("dep", "onCreateView: "+snapshot.getId());
                                    courseList.add(snapshot.getId());
                                }
                                Course_list_Adapter adapter=new Course_list_Adapter((AppCompatActivity) getActivity(),courseList,getResources().getDrawable(R.drawable.folder));
                                listView.setAdapter(adapter);
                            }
                        });
                    }
                }
            });
            database.collection("Lecture").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

for (DocumentSnapshot snapshot:task.getResult()){
Lecture lecture=new Lecture(snapshot.getString("fullName"), snapshot.getString("facebook"),snapshot.getString("instagram"),
        Uri.parse(snapshot.getString("link")),snapshot.getString("description"));
lectures.add(lecture);
}
LectureAdapter adapter=new LectureAdapter(lectures);
lectureRv.setAdapter(adapter);
adapter.notifyDataSetChanged();
                }
            });
            return null;
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Log.d("deps", "onCreateView: "+userIdentity.getString("user_dep","null"));
        }
    }
}