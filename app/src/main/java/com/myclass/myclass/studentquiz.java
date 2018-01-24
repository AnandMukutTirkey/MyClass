package com.myclass.myclass;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.myclass.myclass.Student.mFirebasedatabase;

/**
 * Created by anand on 16/01/18.
 */

public class studentquiz  extends Fragment{
    private ListView simpleList;
    private DatabaseReference reference;
    private String email,sDate;
    private int score,total;
    private ArrayList<AttemptedQuizStructure> quizzes;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.studentquiz,container,false);
        simpleList = (ListView) rootView.findViewById(R.id.quizlist);
        quizzes=new ArrayList<>();

        email = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","").replace("@","");
        reference = mFirebasedatabase.getReference("Quizzes");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {

                    if(dataSnapshot1.hasChild("StudentsInfo") && dataSnapshot1.child("StudentsInfo").hasChild(email))
                    {
                        Log.d("Yay",email);
                        sDate = dataSnapshot1.child("Submission_date").getValue().toString();
                        total =dataSnapshot1.child("total_marks").getValue(Integer.class);
                        score =dataSnapshot1.child("StudentsInfo").child(email).getValue(Integer.class);

                        quizzes.add(new AttemptedQuizStructure(dataSnapshot1.getKey(),Integer.toString(score),Integer.toString(total),sDate));
                        QuizAdapter myAdapter=new QuizAdapter(getContext(),R.layout.studentquiz,quizzes);
                        simpleList.setAdapter(myAdapter);
                    }
                    else
                    {
                        Log.d("yyyyyy",(dataSnapshot1.child("total_marks").getValue(Integer.class)).toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







        //  AttemptedQuizStructure myAdapter=new QuestionAdapter(this,R.layout.activity_quiz_main,list);
        //simpleList.setAdapter(myAdapter);
        return rootView;
    }
}

class QuizAdapter extends ArrayAdapter<AttemptedQuizStructure> {

    ArrayList<AttemptedQuizStructure> quizList;

    private TextView qid,score,tscore,sdate;
    public QuizAdapter(Context context, int textViewResourceId, ArrayList<AttemptedQuizStructure> objects) {
        super(context,textViewResourceId, objects);
        quizList = objects;
    }

    @Override
    public int getCount() {
        int t=quizList.size();
        Log.d("Size issss",Integer.toString(t));
        return quizList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.quizzes_view, null);

        qid=(TextView) v.findViewById(R.id.qid);
        qid.setText(quizList.get(position).getQuiz_id());

        score=(TextView) v.findViewById(R.id.score);
        score.setText(quizList.get(position).getScored());

        tscore=(TextView) v.findViewById(R.id.totalscore);
        tscore.setText(quizList.get(position).getTotal());

        sdate=(TextView) v.findViewById(R.id.sDate);
        sdate.setText(quizList.get(position).getsDate());

        return v;
    }
}
