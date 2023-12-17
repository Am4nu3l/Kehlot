package com.example.examlab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.examlab.Adapters.MyQuestionAdapter;
import com.example.examlab.Adapters.RecyclerTouchListener;
import com.example.examlab.Objects.Question;
import com.example.examlab.StarterPages.LoginPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class MyQandR extends AppCompatActivity {
  private LinearLayoutManager layoutManager;
  ArrayList<Question> questions = new ArrayList<>();
  private FirebaseFirestore database;
  MyQuestionAdapter myQuestionAdapter;
  ArrayList<Question> responses = new ArrayList<>();
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_qand_r);
    Drawable drawable = getResources().getDrawable(R.drawable.line);
    database=FirebaseFirestore.getInstance();
    SharedPreferences sharedPreferences = getSharedPreferences("USER_IDENTITY", MODE_PRIVATE);
    View bottomSheet=getLayoutInflater().inflate(R.layout.my_section,null,false);
    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
    RecyclerView questionRecyclerView = findViewById(R.id.questionRv);
    Button back=findViewById(R.id.back);
    ProgressDialog  pd = new ProgressDialog(bottomSheetDialog.getContext(),R.style.MyProgressTheme);
    pd.setCancelable(false);
    pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
    pd.show();
    back.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent=new Intent(MyQandR.this, MainActivity.class);
        startActivity(intent);
        finish();
      }
    });
    Button menu_drop = findViewById(R.id.more);
    menu_drop.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        PopupMenu popupMenu=new PopupMenu(MyQandR.this,view);
        popupMenu.inflate(R.menu.more);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
          @Override
          public boolean onMenuItemClick(MenuItem menuItem) {
            if(menuItem.getTitle().toString().equals("About")){
              Intent intent=new Intent(MyQandR.this, About.class);
              startActivity(intent);
              finish();
            }
            else if(menuItem.getTitle().toString().equals("Profile")){
              Intent intent=new Intent(MyQandR.this, MainActivity.class);
              startActivity(intent);
              finish();
                     }
            return true;
          }
        });
      }
    });
    database.collection("Question").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
      @Override
      public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if(task.isSuccessful()){
          for(DocumentSnapshot snapshot:task.getResult()){
            if(snapshot.get("questioner").toString().equals(sharedPreferences.getString("user_id",""))){
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
    layoutManager = new LinearLayoutManager(MyQandR.this, LinearLayoutManager.VERTICAL, false);
    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(questionRecyclerView.getContext(), layoutManager.getOrientation());
    questionRecyclerView.addItemDecoration(dividerItemDecoration);
    questionRecyclerView.setLayoutManager(layoutManager);
    myQuestionAdapter = new MyQuestionAdapter(questions);
    questionRecyclerView.setAdapter(myQuestionAdapter);
    questions.clear();
    questionRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
            questionRecyclerView, new RecyclerTouchListener.ClickListener() {
      @Override
      public void onClick(View view, int position) {
        ProgressDialog pd = new ProgressDialog(bottomSheetDialog.getContext(),R.style.MyProgressTheme);
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
        RecyclerView commentRv=bottomSheet.findViewById(R.id.commentRv);
        LinearLayoutManager  layoutManager2 = new LinearLayoutManager(MyQandR.this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration2 = new DividerItemDecoration(commentRv.getContext(), layoutManager2.getOrientation());
        dividerItemDecoration2.setDrawable(drawable);
        commentRv.setLayoutManager(layoutManager2);
        myQuestionAdapter = new MyQuestionAdapter(responses);
        commentRv.setAdapter(myQuestionAdapter);
        responses.clear();
      }
      @Override
      public void onLongClick(View view, int position) {

      }
    }));
  }
}
