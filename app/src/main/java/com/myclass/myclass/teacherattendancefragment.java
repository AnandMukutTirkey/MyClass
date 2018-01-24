package com.myclass.myclass;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by anand on 17/01/18.
 */

public class teacherattendancefragment extends Fragment {

    Button takeattendance,viewattendance;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.teacherattendance,container,false);

        takeattendance = (Button)rootView.findViewById(R.id.takeattendance);
        viewattendance = (Button)rootView.findViewById(R.id.previousattendances);

        takeattendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),teacherattendance.class));
            }
        });

        viewattendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),teacherpreviousattendance.class));
            }
        });

        return rootView;
    }
}
