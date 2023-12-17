package com.example.examlab.Home_Page_Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.examlab.Adapters.DemoAdapter;
import com.example.examlab.Adapters.NotificationScheduler;
import com.example.examlab.Objects.Todos;
import com.example.examlab.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Todo extends Fragment {
    private DemoAdapter demoAdapter;
    private static final int PERMISSION_REQUEST_CODE=123;
    public interface RecyclerViewClickListener {


        void onClick(View view, int position);
    }
    private RecyclerViewClickListener recyclerViewClickListener;
private FloatingActionButton addTodo;
    private Button save;
    private Button cancel;
    private RecyclerView todoRecyclerView;
    private ArrayList<Todos>todoList=new ArrayList<>();
    private LinearLayoutManager layoutManager;
private Todos todos;
private TimePicker timePicker;
    private TextView todoContent;
    int timeh,timem;
private RadioGroup pickTimeSpan;
    private  Dialog dialog;
    private NotificationScheduler notificationScheduler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_todo, container, false);
        addTodo =view.findViewById(R.id.addTodo);
        SharedPreferences sharedPreferences =getActivity().getSharedPreferences("MyPrefs", getActivity().MODE_PRIVATE);
        Gson gson = new Gson();
      dialog = new Dialog(getActivity());
        todoRecyclerView=view.findViewById(R.id.todoRv);
        Drawable drawable=getResources().getDrawable(R.drawable.line);
        layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(todoRecyclerView.getContext(),layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(drawable);
        todoRecyclerView.addItemDecoration(dividerItemDecoration);
        todoRecyclerView.setLayoutManager(layoutManager);
    addTodo.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            dialog.setContentView(R.layout.dialogbox);
            dialog
                .getWindow()
                .setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);
            dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
            save = dialog.findViewById(R.id.save);
            cancel = dialog.findViewById(R.id.cancel);
            todoContent = dialog.findViewById(R.id.todoContent);
            timePicker = dialog.findViewById(R.id.timePicker);
            Date date=new Date();
              SimpleDateFormat sdf = new SimpleDateFormat("a", Locale.getDefault());
              String amPmIndicator = sdf.format(date);
            save.setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    if (todoContent.getText().toString().equals("")) {
                      Toast.makeText(
                              getActivity(),
                              "You Have to Enter Task Description",
                              Toast.LENGTH_LONG)
                          .show();
                    }
                    else {
                      timeh = timePicker.getHour();
                      timem = timePicker.getMinute();
                      dialog.dismiss();
                      todos = new Todos(todoContent.getText().toString(), timeh, timem, todoList.size());
                      todoList.add(todos);
                      demoAdapter = new DemoAdapter(todoList, sharedPreferences);
                      todoRecyclerView.setAdapter(demoAdapter);
                      demoAdapter.notifyItemInserted(todoList.size() - 1);
                      String json = gson.toJson(demoAdapter.getDataList());
                      SharedPreferences.Editor editor = sharedPreferences.edit();
                      editor.putString("todoListKey", json);
                      editor.commit();
                      //schedule for notification
                        NotificationScheduler.scheduleNotification(getActivity(), todoList.get(todoList.size() - 1).getTimeHour(), todoList.get(todoList.size() - 1).getTimeMinute()
                                ,todoList.get(todoList.size() - 1).getDescription(),todoList.get(todoList.size() - 1).getId());
                        Toast.makeText(getActivity(), " todo scheduled successfully", Toast.LENGTH_SHORT).show();
                    }
                  }
                });
            cancel.setOnClickListener(
                new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
                  }
                });
            dialog.show();
          }
        });
        todoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0 ){
                    if(addTodo.isShown()) addTodo.hide();
                } else {
                    if(!addTodo.isShown()) addTodo.show();
                }
            }
        });
        String savedJson = sharedPreferences.getString("todoListKey", null);
        if (savedJson != null) {
            Type type = new TypeToken<ArrayList<Todos>>() {}.getType();
            todoList = gson.fromJson(savedJson, type);
            demoAdapter=new DemoAdapter(todoList,sharedPreferences);
            todoRecyclerView.setAdapter(demoAdapter);
        } else {
            todoList = new ArrayList<>();
        }

        return view;
    }


   
}