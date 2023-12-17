package com.example.examlab.Adapters;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.examlab.Payment;
import com.example.examlab.R;

import java.util.ArrayList;

public class Course_list_Adapter extends BaseAdapter {
    private Button btn_search,menu;
    private ArrayList<String> title_list;
    private AppCompatActivity context;
    private Drawable draw;
    public Course_list_Adapter(AppCompatActivity context, ArrayList<String> title_list, Drawable drawable) {
        this.context=context;
        this.title_list= title_list;
        this.draw=drawable;
    }
    @Override
    public int getCount() {
        return title_list.size();
    }
    @Override
    public Object getItem(int i) {
        return i;
    }
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view.inflate(viewGroup.getContext(), R.layout.activity_course_list,null);
        v.callOnClick();

        ImageButton locker=v.findViewById(R.id.locker);
      SharedPreferences sharedPreferences=context.getSharedPreferences("USER_IDENTITY",MODE_PRIVATE);
      if(sharedPreferences.getString("status",null).equals("paid")){
          locker.setVisibility(View.GONE);
      }
      locker.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
//Toast.makeText(context, "You cant access the courses before payment", Toast.LENGTH_SHORT).show();
              AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
              AlertDialog alertDialog = alertDialogBuilder.create();
              alertDialogBuilder.setMessage("Do you want to proceed to payment?");
              alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                      Intent intent=new Intent(context, Payment.class);
                      context.startActivity(intent);
                  }
              });
              alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                      alertDialog.dismiss();
                  }
              });
              alertDialog.show();
          }
      });
        TextView txt = v.findViewById(R.id.course_title);
        ImageView img=v.findViewById(R.id.list_img);

        txt.setText(title_list.get(i));
        img.setImageResource(R.drawable.folder);
        return v;
    }

}