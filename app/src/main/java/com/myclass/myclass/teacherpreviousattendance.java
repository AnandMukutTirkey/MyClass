package com.myclass.myclass;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class teacherpreviousattendance extends AppCompatActivity {

    ArrayList<String> studentnames = new ArrayList<>();
    ArrayList<String> studentids = new ArrayList<>();
    ListView listView ;
    EditText date;
    Button button;
    DatabaseReference reference;
    static ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> allstudents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacherpreviousattendance);

        listView = (ListView)findViewById(R.id.list_attendance);
        date = (EditText)findViewById(R.id.pickdate);
        button = (Button)findViewById(R.id.submit);
        reference = FirebaseDatabase.getInstance().getReference();
        final ViewAttendanceAdapter attendanceAdapter = new ViewAttendanceAdapter(teacherpreviousattendance.this,R.layout.teacherevent,allstudents);

        final String teacheremail = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","").replace("@","");
        final String datee = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        listView.setAdapter(attendanceAdapter);

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
                            allstudents.add(dataSnapshot.getValue(String.class));
                            //reference.child("student"+studentid).setValue(dataSnapshot.getValue(String.class));
                            listView.invalidateViews();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("mystudents",databaseError.getMessage());
                            //reference.child("logs1").setValue(databaseError.getMessage()+"1");
                        }
                    });
                }
                listView.invalidateViews();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("mystudents",databaseError.getMessage());
                //reference.child("logs2").setValue(databaseError.getMessage()+"2");
            }
        });
        reference.child("attendance").child(teacheremail).child(datee).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> studentids = dataSnapshot.getChildren();
                for (DataSnapshot studentid : studentids){
                    String sid = studentid.getValue(String.class);
                    arrayList.add(sid);
                }
                listView.invalidateViews();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.clear();
                if (TextUtils.isEmpty(date.getText().toString().trim()) ){
                    Toast.makeText(getApplicationContext(),"please enter valid date",Toast.LENGTH_SHORT).show();
                    return;
                }
                String datte = date.getText().toString().trim().replace("/","-");
                reference.child("attendance").child(teacheremail).child(datte).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Iterable<DataSnapshot> studentids = dataSnapshot.getChildren();
                        for (DataSnapshot studentid : studentids){
                            String sid = studentid.getValue(String.class);
                            arrayList.add(sid);
                        }
                        listView.invalidateViews();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });


    }

}


class ViewAttendanceAdapter extends ArrayAdapter<String> {
    ArrayList<String> events = new ArrayList<>();
    public ViewAttendanceAdapter(Context context, int textViewResourceId, ArrayList<String> objects){
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
        convertView = ((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.viewattendance,null);

        TextView view = (TextView)convertView.findViewById(R.id.textView);
        if (teacherpreviousattendance.arrayList.contains(events.get(position))){
            view.setText(events.get(position));
            view.setBackgroundColor(Color.GREEN);
        }else{
            view.setText(events.get(position));
            view.setBackgroundColor(Color.RED);
        }
        return convertView;
    }
}