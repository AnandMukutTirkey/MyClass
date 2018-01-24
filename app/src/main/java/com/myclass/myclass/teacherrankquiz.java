package com.myclass.myclass;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class teacherrankquiz extends AppCompatActivity {
    public ListView listView;
    public ArrayList<Rank> arr;
    public ArrayList<String> arrS;
    public long c=0;
    public String rqid;
    public FirebaseDatabase mFirebasedatabase;
    private DatabaseReference mDatabaseReference;
    long n;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacherrankquiz);
        rqid=getIntent().getStringExtra("quizcode");

        arr=new ArrayList<>();
        arrS=new ArrayList<>();
       // arr.add(new Rank(c,"Ram"));

       mFirebasedatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebasedatabase.getReference("Quizzes").child(rqid).child("StudentsInfo");
        Query query =mDatabaseReference.orderByValue();
        n = 0;

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                n = dataSnapshot.getChildrenCount();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    //sarr.add(dataSnapshot1.getKey());
                    Log.d("Rank  ",dataSnapshot1.getKey());

                    arr.add(new Rank((int) (n-(c++)),dataSnapshot1.getKey()));
                }
                Collections.reverse(arr);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listView=(ListView) findViewById(R.id.srankList);

        RankAdapter adapter =new RankAdapter(this,R.layout.rankview,arr);
        listView.setAdapter(adapter);
    }
}

class Rank
{
    int val;
    String name;

    public Rank(int val, String name) {
        this.val = val;
        this.name = name;
    }


    public int getVal() {
        return val;
    }

    public String getName() {
        return name;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public void setName(String name) {
        this.name = name;
    }
}
/*

Iterable<DataSnapshot> questions = dataSnapshot.getChildren();
                            // QuizData qd = .getValue(QuizData.class);
                            ArrayList<String> qq =new ArrayList<>();
                            for (DataSnapshot contact : questions) {
                                String c = contact.getKey();
                                qq.add(c);

                            }
 */

class RankAdapter extends ArrayAdapter<Rank>
{
 public TextView myrank,myname;
    ArrayList<Rank> rans=new ArrayList<>();
    public RankAdapter(Context context, int textViewResourceId, ArrayList<Rank> objects) {
        super(context,textViewResourceId, objects);
        rans = objects;

    }

    @Override
    public int getCount() {
        return rans.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.rankview, null);


       myrank=(TextView) v.findViewById(R.id.myrank);
        myrank.setText(Integer.toString(rans.get(position).getVal()));

        myname=(TextView) v.findViewById(R.id.myname);
        myname.setText(rans.get(position).getName());
        return v;
    }

}