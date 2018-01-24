package com.myclass.myclass;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by anand on 16/01/18.
 */

public class studentattendance extends Fragment{

    ListView listView;
    TextView textView;
    DatabaseReference reference;
    String subject,teacher;
    ArrayList<String> sheet = new ArrayList<>();
    int presentCount,absentCount;
    double percent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.studentattendance,container,false);

        listView = (ListView)rootView.findViewById(R.id.studentattendancelist);
        sheet.clear();
        presentCount=0;
        absentCount=0;
        textView = (TextView)rootView.findViewById(R.id.percentage);
        percent = (double)(presentCount*100)/(presentCount+absentCount);
        textView.setText(String.valueOf(percent)+" %");
        reference = FirebaseDatabase.getInstance().getReference();
        final ViewAttendanceAdapter attendanceAdapter = new ViewAttendanceAdapter(getContext(),R.layout.studentattendance,sheet);
        listView.setAdapter(attendanceAdapter);


        final String userid = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","").replace("@","");
        reference.child("users").child(userid).child("subject").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                subject = dataSnapshot.getValue(String.class);
                reference.child("course").child(subject).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        teacher = dataSnapshot.getValue(String.class).replace("@","").replace(".","");
                        reference.child("attendance").child(teacher).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //Map<String,String> map = dataSnapshot.getValue(Map.class);
                                Iterable<DataSnapshot> abc = dataSnapshot.getChildren();
                                for (DataSnapshot abcd : abc){
                                    String date = abcd.getKey();
                                    Map<String,Object> map = (Map<String, Object>) abcd.getValue();
                                    Log.d("map",map.toString());

                                    if (map.containsKey(userid)){
                                        //attendancesheet.put(date,"present");
                                        sheet.add(date+" :: "+"present");
                                        presentCount++;
                                    }else{
                                        //attendancesheet.put(date,"absent");
                                        sheet.add(date+" :: "+ "absent");
                                        absentCount++;
                                    }
                                    listView.invalidateViews();
                                    percent = (double)(presentCount*100)/(presentCount+absentCount);
                                    textView.setText(String.valueOf(percent)+" %");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        return rootView;
    }
}



class ViewAttendanceAdapterStudent extends ArrayAdapter<String> {
    ArrayList<String> events = new ArrayList<>();
    public ViewAttendanceAdapterStudent(Context context, int textViewResourceId, ArrayList<String> objects){
        super(context,textViewResourceId, objects);
        events = objects;
    }


    @Override
    public int getCount() {
        return events.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = ((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.studentviewattendance,null);

        TextView view = (TextView)convertView.findViewById(R.id.attendancestudent);

        view.setText(events.get(position));


        return convertView;
    }
}