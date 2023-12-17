package com.example.examlab.Adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.examlab.Objects.Question;
import com.example.examlab.R;

import java.util.ArrayList;
public class MyQuestionAdapter extends RecyclerView.Adapter<MyQuestionAdapter.ViewHolder> {
    private    ArrayList<Question>questions;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView usrName,content;
        public ViewHolder(View view) {
            super(view);
            usrName =view.findViewById(R.id.userName);
            content =view.findViewById(R.id.content);        }
        public TextView getUsrName() {
            return usrName;
        }
        public TextView getContent() {
            return content;
        }
    }
    public MyQuestionAdapter(ArrayList<Question>questions) {
       this.questions=questions;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item=
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_question_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyQuestionAdapter.ViewHolder holder, int position) {
        holder.getContent().setText(questions.get(position).getContent());
        holder.getUsrName().setText(questions.get(position).getUsrName());
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
}


