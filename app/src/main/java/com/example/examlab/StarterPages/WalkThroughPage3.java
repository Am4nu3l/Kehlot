package com.example.examlab.StarterPages;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.examlab.R;

public class WalkThroughPage3 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_walk_through_page3, container, false);
        Button getStarted=view.findViewById(R.id.getStarted);
getStarted.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(getActivity(),LoginPage.class);
        startActivity(intent);
        getActivity().finish();
    }
});
        return view;
    }
}