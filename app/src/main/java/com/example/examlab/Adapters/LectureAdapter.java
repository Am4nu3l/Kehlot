package com.example.examlab.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.examlab.Objects.Lecture;
import com.example.examlab.R;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class LectureAdapter extends RecyclerView.Adapter<LectureAdapter.ViewHolder> {
    private ArrayList<Lecture> localDataSet;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private  CircleImageView couselorIMG;

        public ViewHolder(View view) {
            super(view);
            textView =view.findViewById(R.id.lecture_name);
            couselorIMG=view.findViewById(R.id.counselorImg);
        }
        public TextView getTextView() {
            return textView;
        }
        public CircleImageView getCouselorIMG() {
            return couselorIMG;
        }
    }
    public LectureAdapter(ArrayList<Lecture>  dataSet) {
        this.localDataSet = dataSet;
    }
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item=
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.singlelecturedesign, viewGroup, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull LectureAdapter.ViewHolder holder, int position) {
        holder.getTextView().setText(localDataSet.get(position).getFullName());
        Picasso.get().load(localDataSet.get(position).getImage()).into(holder.getCouselorIMG());
    }
    @Override
    public int getItemCount() {
        return localDataSet.size();
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

