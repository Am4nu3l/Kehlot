package com.example.examlab.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examlab.Home_Page_Fragments.Todo;
import com.example.examlab.Objects.Todos;
import com.example.examlab.R;

import java.util.ArrayList;



public class MyTodoAdapter extends RecyclerView.Adapter<com.example.examlab.Adapters.MyTodoAdapter.ViewHolder> {
        private ArrayList<Todos> todos;

        public static class ViewHolder extends RecyclerView.ViewHolder  {
            private TextView timeSpan,remove;
            private CheckBox content;
            public ViewHolder(View view) {
                super(view);
                content =view.findViewById(R.id.content);
                timeSpan =view.findViewById(R.id.timeSpan);
                remove=view.findViewById(R.id.remove);
            }


            public TextView getContent() {
                return content;
            }
            public TextView getRemove() {
                return remove;
            }
            public TextView getTimeSpan() {
                return timeSpan;
            }

        }
        public MyTodoAdapter(ArrayList<Todos>todos) {
            this.todos=todos;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public com.example.examlab.Adapters.MyTodoAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item=
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.activity_todo_row, viewGroup, false);
            return new com.example.examlab.Adapters.MyTodoAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull com.example.examlab.Adapters.MyTodoAdapter.ViewHolder holder, int position) {
            holder.getContent().setText(todos.get(position).getDescription());
            holder.getTimeSpan().setText(todos.get(position).getTimeHour()+":"+todos.get(position).getTimeMinute());
        }
        @Override
        public int getItemCount() {
            return todos.size();
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



