package com.myclass.myclass;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class teacherattendance extends AppCompatActivity {


    ListView attendanceList;
    DatabaseReference reference;
    Button submit;
    static ArrayList<String> studentnames = new ArrayList<>();
    static ArrayList<String> studentids = new ArrayList<>();
    static Map<String,String> presentstudents = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacherattendance);

        Log.d("teacherattendance","here");

        /*studentnames.add("student 1");
        studentnames.add("student 2");*/

        presentstudents.clear();
        studentnames.clear();
        studentids.clear();
        final AttendanceAdapter attendanceAdapter = new AttendanceAdapter(teacherattendance.this,R.layout.teacherevent,studentnames);
        attendanceList = (ListView)findViewById(R.id.list_attendance);
        submit = (Button)findViewById(R.id.submit);
        final String teacheremail = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","").replace("@","");
        reference = FirebaseDatabase.getInstance().getReference();
        attendanceList.setAdapter(attendanceAdapter);

        //get all students of this teacher
        reference.child("class").child(teacheremail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> studentidss = dataSnapshot.getChildren();
                for (DataSnapshot studentid : studentidss){
                    studentids.add(studentid.getValue(String.class).replace(".","").replace("@",""));
                }

                for (final String studentid : studentids){
                    reference.child("users").child(studentid).child("name").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            studentnames.add(dataSnapshot.getValue(String.class));
                            //reference.child("student"+studentid).setValue(dataSnapshot.getValue(String.class));
                            attendanceList.invalidateViews();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("mystudents",databaseError.getMessage());
                            //reference.child("logs1").setValue(databaseError.getMessage()+"1");
                        }
                    });
                }
                attendanceList.invalidateViews();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("mystudents",databaseError.getMessage());
                //reference.child("logs2").setValue(databaseError.getMessage()+"2");
            }
        });

        //get their name

        //show in the listview


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                reference.child("attendance").child(teacheremail).child(date).setValue(presentstudents);
                presentstudents.clear();
                studentids.removeAll(studentids);
                studentnames.removeAll(studentnames);
                finish();

            }
        });

    }
}

class AttendanceAdapter extends ArrayAdapter<String> {
    ArrayList<String> events = new ArrayList<>();
    public AttendanceAdapter(Context context, int textViewResourceId, ArrayList<String> objects){
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
        convertView = ((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.attendance,null);

        CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.checkbox);
        checkBox.setText(events.get(position));

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (teacherattendance.presentstudents.containsKey(teacherattendance.studentids.get(position))){
                    teacherattendance.presentstudents.remove(teacherattendance.studentids.get(position));
                    Toast.makeText(getContext(),"entry deleted" + teacherattendance.studentids.get(position),Toast.LENGTH_LONG).show();
                }else{
                    teacherattendance.presentstudents.put(teacherattendance.studentids.get(position),teacherattendance.studentnames.get(position));
                    Toast.makeText(getContext(),"entry added" + teacherattendance.studentids.get(position),Toast.LENGTH_LONG).show();

                }
            }
        });

        return convertView;
    }
}