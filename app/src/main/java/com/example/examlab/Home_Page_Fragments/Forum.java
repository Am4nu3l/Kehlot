package com.example.examlab.Home_Page_Fragments;

import static android.content.Context.BIND_AUTO_CREATE;
import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examlab.Adapters.MyBackgroundService;
import com.example.examlab.Adapters.MyQuestionAdapter;
import com.example.examlab.Adapters.RecyclerTouchListener;
import com.example.examlab.MyQandR;
import com.example.examlab.Objects.Question;
import com.example.examlab.R;
import com.example.examlab.pdfopener;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class Forum extends Fragment {

    private LinearLayoutManager layoutManager;
    ArrayList<Question> questions = new ArrayList<>();
    ArrayList<Question> responses = new ArrayList<>();
    MyQuestionAdapter myQuestionAdapter;
    ExpandableLayout expandableLayout;
    TextView questionExpand;
    FirebaseFirestore database;
    private RecyclerView questionRecyclerView;
    private String cont = "Java is a programming language and computing platform first released by Sun Microsystems in 1995.";
    private int counter = 0;
    private SharedPreferences sharedPreferences;
    private BottomSheetDialog bottomSheetDialog;
    Gson gson = new Gson();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        View bottomSheet=getLayoutInflater().inflate(R.layout.comment_section,null,false);
        TextView lastQuestion=view.findViewById(R.id.lastQuestion);
        TextView goToMyQAndR=view.findViewById(R.id.goToMyQAndR);
        ProgressBar progressBar = view.findViewById(R.id.spin_kit);
        ThreeBounce doubleBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        bottomSheetDialog=new BottomSheetDialog(getContext());
        questionRecyclerView = view.findViewById(R.id.questionRv);
        Button uploadQuestion = view.findViewById(R.id.uploadQuestion);
        TextView searchBar = view.findViewById(R.id.searchBar);
        expandableLayout = view.findViewById(R.id.expandable_layout);
        questionExpand = view.findViewById(R.id.myQuestion);
        database = FirebaseFirestore.getInstance();
        int collaps = R.drawable.collaps;
        int drop = R.drawable.dropdown;
        goToMyQAndR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           Intent intent=new Intent(getActivity(), MyQandR.class);
startActivity(intent);
getActivity().finish();
            }
        });
        questionExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter == 0) {
                    expandableLayout.expand();
                    questionExpand.setCompoundDrawablesWithIntrinsicBounds(0, 0, collaps, 0);
                    questionExpand.setCompoundDrawablePadding(8);
                    counter = +1;
                } else if (counter == 1) {
                    expandableLayout.collapse();
                    questionExpand.setCompoundDrawablesWithIntrinsicBounds(0, 0, drop, 0);
                    questionExpand.setCompoundDrawablePadding(8);
                    counter = 0;
                }
            }
        });
        ProgressDialog  pd = new ProgressDialog(bottomSheetDialog.getContext(),R.style.MyProgressTheme);
        pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pd.show();
        //user full name;
        sharedPreferences = getActivity().getSharedPreferences("USER_IDENTITY", MODE_PRIVATE);
        String fullName=sharedPreferences.getString("usrFName","")+"\t"+sharedPreferences.getString("usrMName","");
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager  layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        //uploading question
        uploadQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searchBar.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "cant post empty question", Toast.LENGTH_LONG).show();
                }
                else {
                    uploadQuestion.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    String question = searchBar.getText().toString();
                    HashMap<String, String> userQuestion = new HashMap<>();
                    userQuestion.put("questioner", sharedPreferences.getString("user_id", ""));
                    userQuestion.put("questionerName", fullName);
                    userQuestion.put("question", question);
                    String hashMapJson = gson.toJson(userQuestion);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user_info_json", hashMapJson);
                    editor.commit();
                    //post question
                    database.collection("Question").document()
                            .set(userQuestion).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    searchBar.setText("");
                                    Toast.makeText(getActivity(), "Question Posted", Toast.LENGTH_SHORT).show();
                                    uploadQuestion.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    String hashMapJson = sharedPreferences.getString("user_info_json", null);
                                    HashMap<String, Object> yourHashMap = gson.fromJson(hashMapJson, new TypeToken<HashMap<String, Object>>(){}.getType());
                                    lastQuestion.setText(yourHashMap.get("question").toString());
                                }
                            });
                }
            }
        });
        //list questions
        database.collection("Question").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot snapshot:task.getResult()){
                        if(!snapshot.get("questioner").toString().equals(sharedPreferences.getString("user_id",""))){
                            Question qq=new Question(snapshot.get("questionerName").toString()
                                    ,snapshot.get("questioner").toString(), snapshot.getId(), snapshot.get("question").toString());
                            questions.add(qq);
                        }
                    }
                    pd.dismiss();
                    myQuestionAdapter.notifyDataSetChanged();
                }
            }
        });
        EditText feedback=bottomSheet.findViewById(R.id.feedbackBar);
        Button uploadFeedback=bottomSheet.findViewById(R.id.uploadFeedback);
        RecyclerView commentRv=bottomSheet.findViewById(R.id.commentRv);
        Drawable drawable = getResources().getDrawable(R.drawable.line);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(questionRecyclerView.getContext(), layoutManager.getOrientation());
        DividerItemDecoration dividerItemDecoration2 = new DividerItemDecoration(commentRv.getContext(), layoutManager2.getOrientation());
        dividerItemDecoration.setDrawable(drawable);
       dividerItemDecoration2.setDrawable(drawable);
        questionRecyclerView.addItemDecoration(dividerItemDecoration2);
        commentRv.addItemDecoration(dividerItemDecoration);
        myQuestionAdapter = new MyQuestionAdapter(questions);
        questionRecyclerView.setLayoutManager(layoutManager);
        questionRecyclerView.setAdapter(myQuestionAdapter);
questions.clear();
        questionRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                questionRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
              ProgressDialog  pd = new ProgressDialog(bottomSheetDialog.getContext(),R.style.MyProgressTheme);
                pd.setCancelable(false);
                pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                pd.show();
                bottomSheetDialog.setContentView(bottomSheet);
                bottomSheetDialog.show();
                bottomSheetDialog.setCanceledOnTouchOutside(true);
                bottomSheetDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                        //load the responses for the clicked question by referring it's id from the Question Class
                        database.collection("Question")
                                .document(questions.get(position).getQuestionId())
                                .collection("Feedback").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            for (DocumentSnapshot snapshot:task.getResult()){
                                                Question qq=new Question(snapshot.get("responderName").toString()
                                                        ,null, snapshot.getId(), snapshot.get("response").toString());
                                                responses.add(qq);
                                            }
                                           myQuestionAdapter.notifyDataSetChanged();
                                            pd.dismiss();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                commentRv.setLayoutManager(layoutManager2);
                myQuestionAdapter = new MyQuestionAdapter(responses);
                commentRv.setAdapter(myQuestionAdapter);
                responses.clear();
                //upload feedback for the selected question from here/cause we need the position of the selected question
                uploadFeedback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(feedback.getText().toString().isEmpty()){
                            Toast.makeText(getActivity(), "cant post empty response", Toast.LENGTH_LONG).show();
                        }
                        else {
                            uploadQuestion.setVisibility(View.GONE);
                            progressBar.setVisibility(View.VISIBLE);
                            HashMap<String,String > answer=new HashMap<>();
                            answer.put("responderName",fullName);
                            answer.put("response",feedback.getText().toString());
                            database.collection("Question")
                                    .document(questions.get(position).getQuestionId())
                                    .collection("Feedback").add(answer).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            feedback.setText("");
                                            Toast.makeText(getActivity(), "uploaded", Toast.LENGTH_LONG).show();
                                            uploadQuestion.setVisibility(View.VISIBLE);
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    });
                        }
                    }
                });
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));



        //list question
        return view;
    }

}
//        questionRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                    @Override
//                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                        LinearLayoutManager layoutManager=LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
//                        int totalItemCount = layoutManager.getItemCount();
//                        int lastVisible = layoutManager.findLastVisibleItemPosition();
//
//                        boolean endHasBeenReached = lastVisible + 5 >= totalItemCount;
//                        if (totalItemCount > 0 && endHasBeenReached) {
//                            //you have reached to the bottom of your recycler view
//                            if (expandableLayout.isExpanded()) {
//                                expandableLayout.collapse();
//                                questionExpand.setCompoundDrawablesWithIntrinsicBounds(0, 0, drop, 0);
//                                questionExpand.setCompoundDrawablePadding(8);
//                                counter=0;
//                            }
//                        }
//                    }
//                });
//            }
//        });