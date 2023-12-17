package com.example.examlab.Adapters;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.examlab.R;

import java.util.ArrayList;
public class test_list_adapter extends BaseAdapter {
    private Button btn_search,menu;
    private ArrayList<String> title_list;
    private Activity context;
    public test_list_adapter(Activity context, ArrayList<String> lists) {
        this.context=context;
        this.title_list= lists;

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
        View v = view.inflate(viewGroup.getContext(), R.layout.activity_list_test,null);
        v.callOnClick();
        ImageView img=v.findViewById(R.id.list_img);
        TextView txt = v.findViewById(R.id.test_title);
        String testtitle=txt.getText()+" "+title_list.get(i);
        txt.setText(testtitle);
        img.setImageResource(R.drawable.ic_subject);
        return v;
    }

}