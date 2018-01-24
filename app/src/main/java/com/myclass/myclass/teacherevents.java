package com.myclass.myclass;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.EventLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by anand on 17/01/18.
 */

public class teacherevents extends Fragment {

    DatabaseReference reference;
    ListView listView;
    ArrayList<Event> list = new ArrayList<>();
    TextView button;
    Button back;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.teacherevent,container,false);


        button = (TextView) rootView.findViewById(R.id.back);
        back = (Button)rootView.findViewById(R.id.backk);
        String userid = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace("@","").replace(".","");
        reference = FirebaseDatabase.getInstance().getReference("Events").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> events = dataSnapshot.getChildren();
                for (DataSnapshot event :events){
                    list.add(event.getValue(Event.class));
                    listView.invalidateViews();
                }
//                eventAdapter.notifyDataSetChanged();
                listView.invalidateViews();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        final EventAdapter eventAdapter = new EventAdapter(getContext(),R.layout.teacherevent,list);
        listView = (ListView)rootView.findViewById(R.id.list_events);
        listView.setAdapter(eventAdapter);

//
//        list.add(new Event("title1","date1","description1"));
//        list.add(new Event("title2","date2","description2"));
//        list.add(new Event("title3","date3","description3"));
//        list.add(new Event("title4","date4","description4"));
//        list.add(new Event("title5","date5","description5"));



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getActivity().getSupportFragmentManager().beginTransaction().remove(teacherevents.this).commitNow();
                getFragmentManager().beginTransaction().remove(teacherevents.this).commit();
                //startActivity(new Intent(getContext(),Teacher.class));
            }
        });





        return rootView;
    }


    
}


class EventAdapter extends ArrayAdapter<Event>{
    ArrayList<Event> events = new ArrayList<>();
    public EventAdapter(Context context, int textViewResourceId, ArrayList<Event> objects){
        super(context,textViewResourceId, objects);
        events = objects;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = ((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.event,null);
        TextView title,dateandtime,description;
        title = (TextView)convertView.findViewById(R.id.title);
        dateandtime = (TextView)convertView.findViewById(R.id.dateandtime);
        description = (TextView)convertView.findViewById(R.id.description);

        title.setText(events.get(position).getTitle());
        dateandtime.setText(events.get(position).getDateandtime());
        description.setText(events.get(position).getDescription());

        return convertView;
    }
}
