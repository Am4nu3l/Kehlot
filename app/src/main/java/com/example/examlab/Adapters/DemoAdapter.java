package com.example.examlab.Adapters;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examlab.Objects.Todos;
import com.example.examlab.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class DemoAdapter extends RecyclerView.Adapter<DemoVn> {
    ArrayList<Todos> todos=new ArrayList<>();
    private List<Todos> dataList;
private SharedPreferences sharedPreferences;

    // Constructor and other necessary methods
    public DemoAdapter(ArrayList<Todos> todos, SharedPreferences sharedPreferences) {
        this.todos = todos;
        this.sharedPreferences=sharedPreferences;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    @NonNull
    @Override
    public DemoVn onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_todo_row,parent,false);
        return new DemoVn(view).linkAdapter(this);
    }
    public ArrayList<Todos> getDataList() {
        return todos;
    }
    public void setDataList(ArrayList<Todos> dataList) {
        this.todos = dataList;
    }
    @Override
    public void onBindViewHolder(@NonNull DemoVn holder, int position) {
       holder.todoContent.setText(todos.get(position).getDescription());
       holder.timeSpan.setText(todos.get(position).getTimeHour()+":"+todos.get(position).getTimeMinute());
    }
    @Override
    public int getItemCount() {
        return todos.size();
    }
}
class DemoVn extends RecyclerView.ViewHolder {
    TextView timeSpan;
      CheckBox todoContent;
    private DemoAdapter adapter;

    public DemoVn(@NonNull View itemView) {
        super(itemView);
        timeSpan=itemView.findViewById(R.id.timeSpan);
        todoContent=itemView.findViewById(R.id.content);
        itemView.findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.todos.remove(getAdapterPosition());
                adapter.notifyItemRemoved(getAdapterPosition());
                Gson gson = new Gson();
                String json = gson.toJson(adapter.todos);
                SharedPreferences.Editor editor = adapter.getSharedPreferences().edit();
                editor.putString("todoListKey", json);
                editor.commit();
            }
        });
    }
    public DemoVn linkAdapter(DemoAdapter adapter) {
        this.adapter = adapter;
        return this;
    }
}
