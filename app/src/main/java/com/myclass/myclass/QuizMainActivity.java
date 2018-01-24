package com.myclass.myclass;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import static com.myclass.myclass.Student.mFirebasedatabase;

public class QuizMainActivity extends AppCompatActivity {
    public static FirebaseDatabase mFirebasedatabase;
    private DatabaseReference mDatabaseReference;
    ListView simpleList;
    private Button submitButton;
    private Dialog sDialog;
    private long right_answer,total_marks;
    private String right_ans;
    private String email,code;
    ArrayList<QuizQuestionData> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_main);
        simpleList = (ListView) findViewById(R.id.listview_questions);
        Bundle bundle=getIntent().getExtras();
        list = (ArrayList<QuizQuestionData>) getIntent().getSerializableExtra("questionsList");
        QuestionAdapter myAdapter=new QuestionAdapter(this,R.layout.activity_quiz_main,list);
        simpleList.setAdapter(myAdapter);

        code = bundle.getString("quiz_code");
        mFirebasedatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebasedatabase.getReference("Quizzes");

        email = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","").replace("@","");

        submitButton = (Button) findViewById(R.id.submitQuiz);

        right_answer=0;
        total_marks=0;

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sDialog = new Dialog(QuizMainActivity.this);
                sDialog.setContentView(R.layout.quiz_submission_output);
                sDialog.setCancelable(false);
                Button exit=(Button)sDialog.findViewById(R.id.exitQuiz);
                TextView tq =(TextView)sDialog.findViewById(R.id.correctSubmissionValue);
                TextView tm =(TextView)sDialog.findViewById(R.id.marksValue);

                for (int i = 0; i < QuestionAdapter.selectedQuizAnswer.size(); i++) {
                    right_ans=list.get(i).getCorrectanswer();

                    if((QuestionAdapter.selectedQuizAnswer.get(i)).equals(right_ans))
                    {
                        right_answer++;
                        total_marks+=list.get(i).getMarks();
                    }

                    else
                    {
                        Log.d("Yayyyyy::::",right_ans+" "+QuestionAdapter.selectedQuizAnswer.get(i));
                    }


                }

                HashMap<String,Long> map =new HashMap<>();
                //map.put(email,total_marks);
                 /*mDatabaseReference.addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {

                     }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {

                     }
                 });*/

                tq.setText(new Long(right_answer).toString());
                tm.setText(new Long(total_marks).toString());
                sDialog.show();
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        sDialog.dismiss();
                    }
                });
                mDatabaseReference.child(code).child("StudentsInfo").child(email).setValue(total_marks);
            }
        });
    }
}