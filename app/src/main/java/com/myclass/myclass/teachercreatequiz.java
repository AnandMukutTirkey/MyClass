package com.myclass.myclass;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.Locale;
import java.util.Map;

public class teachercreatequiz extends AppCompatActivity {

    DatabaseReference reference;
    EditText question,option1,option2,option3,option4,marks;
    Spinner correctanswer;
    Button submit,add;
    Dialog myDialog;
    ArrayList<Question> questions = new ArrayList<>();
    ArrayList<String> students = new ArrayList<>();
    String email;

    String strquestion, stroption1, stroption2,stroption3, stroption4,strcorrectanswer;
    int strmarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachercreatequiz);

        question = (EditText)findViewById(R.id.question);
        option1 = (EditText)findViewById(R.id.option1);
        option2 = (EditText)findViewById(R.id.option2);
        option3 = (EditText)findViewById(R.id.option3);
        option4 = (EditText)findViewById(R.id.option4);
        correctanswer = (Spinner)findViewById(R.id.correctanswer);
        marks = (EditText)findViewById(R.id.marks);
        submit = (Button)findViewById(R.id.submit);
        add = (Button)findViewById(R.id.add);
        myDialog = new Dialog(teachercreatequiz.this);
        reference = FirebaseDatabase.getInstance().getReference();

        //fetch all the students of this teacher
        email = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","").replace("@","");
        reference.child("class").child(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> studentss = dataSnapshot.getChildren();
                for (DataSnapshot student :studentss){
                    students.add(student.getValue(String.class).replace("@","").replace(".",""));
                }
                students.add(email);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(question.getText())){
                    Toast.makeText(getApplicationContext(),"please enter the question",Toast.LENGTH_LONG).show();
                    return;
                }else if(TextUtils.isEmpty(option1.getText())){
                    Toast.makeText(getApplicationContext(),"please enter the option1",Toast.LENGTH_LONG).show();
                    return;
                }else if(TextUtils.isEmpty(option2.getText())){
                    Toast.makeText(getApplicationContext(),"please enter the option2",Toast.LENGTH_LONG).show();
                    return;
                }else if(TextUtils.isEmpty(option3.getText())){
                    Toast.makeText(getApplicationContext(),"please enter the option3",Toast.LENGTH_LONG).show();
                    return;
                }else if(TextUtils.isEmpty(option4.getText())){
                    Toast.makeText(getApplicationContext(),"please enter the option4",Toast.LENGTH_LONG).show();
                    return;
                }else if(TextUtils.isEmpty(correctanswer.getSelectedItem().toString())){
                    Toast.makeText(getApplicationContext(),"please select correct option",Toast.LENGTH_LONG).show();
                    return;
                }else if(TextUtils.isEmpty(marks.getText())){
                    Toast.makeText(getApplicationContext(),"please provide marks",Toast.LENGTH_LONG).show();
                    return;
                }else{

                    strquestion = question.getText().toString();
                    stroption1 = option1.getText().toString();
                    stroption2 = option2.getText().toString();
                    stroption3 = option3.getText().toString();
                    stroption4 = option4.getText().toString();
                    strcorrectanswer = correctanswer.getSelectedItem().toString();
                    strmarks = Integer.parseInt(marks.getText().toString().trim());

                    if (strcorrectanswer.equals("A")){
                        strcorrectanswer = stroption1;
                    }else if (strcorrectanswer.equals("B")){
                        strcorrectanswer = stroption2;
                    }else if (strcorrectanswer.equals("C")){
                        strcorrectanswer = stroption3;
                    }else if (strcorrectanswer.equals("D")){
                        strcorrectanswer = stroption4;
                    }

                    Log.d("mark",Integer.toString(strmarks));

                    Question ques = new Question(strquestion,stroption1,stroption2,stroption3,stroption4,strcorrectanswer,strmarks);

                    questions.add(ques);


                    //clear all fields
                    question.setText("");
                    option1.setText("");
                    option2.setText("");
                    option3.setText("");
                    option4.setText("");
                    marks.setText("");
                }

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(question.getText())){
                    Toast.makeText(getApplicationContext(),"please enter the question",Toast.LENGTH_LONG).show();
                    return;
                }else if(TextUtils.isEmpty(option1.getText())){
                    Toast.makeText(getApplicationContext(),"please enter the option1",Toast.LENGTH_LONG).show();
                    return;
                }else if(TextUtils.isEmpty(option2.getText())){
                    Toast.makeText(getApplicationContext(),"please enter the option2",Toast.LENGTH_LONG).show();
                    return;
                }else if(TextUtils.isEmpty(option3.getText())){
                    Toast.makeText(getApplicationContext(),"please enter the option3",Toast.LENGTH_LONG).show();
                    return;
                }else if(TextUtils.isEmpty(option4.getText())){
                    Toast.makeText(getApplicationContext(),"please enter the option4",Toast.LENGTH_LONG).show();
                    return;
                }else if(TextUtils.isEmpty(correctanswer.getSelectedItem().toString())){
                    Toast.makeText(getApplicationContext(),"please select correct option",Toast.LENGTH_LONG).show();
                    return;
                }else if(TextUtils.isEmpty(marks.getText())){
                    Toast.makeText(getApplicationContext(),"please provide marks",Toast.LENGTH_LONG).show();
                    return;
                }else{
                    myDialog.setContentView(R.layout.createquizdialog);
                    myDialog.setCancelable(false);
                    final EditText quizid = (EditText)myDialog.findViewById(R.id.quizid);
                    final EditText eventdate = (EditText)myDialog.findViewById(R.id.eventdate);
                    Button createquiz = (Button)myDialog.findViewById(R.id.createquiz);
                    myDialog.show();
                    strquestion = question.getText().toString();
                    stroption1 = option1.getText().toString();
                    stroption2 = option2.getText().toString();
                    stroption3 = option3.getText().toString();
                    stroption4 = option4.getText().toString();
                    strcorrectanswer = correctanswer.getSelectedItem().toString();
                    strmarks = Integer.parseInt(marks.getText().toString().trim());


                    if (strcorrectanswer.equals("A")){
                        strcorrectanswer = stroption1;
                    }else if (strcorrectanswer.equals("B")){
                        strcorrectanswer = stroption2;
                    }else if (strcorrectanswer.equals("C")){
                        strcorrectanswer = stroption3;
                    }else if (strcorrectanswer.equals("D")){
                        strcorrectanswer = stroption4;
                    }

                    Log.d("mark2",Integer.toString(strmarks));

                    Question que = new Question(strquestion,stroption1,stroption2,stroption3,stroption4,strcorrectanswer,strmarks);
                    questions.add(que);
                    createquiz.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (TextUtils.isEmpty(quizid.getText()) || (quizid.getText().toString().length() <6) ){
                                Toast.makeText(teachercreatequiz.this,"please provide a valid quiz id",Toast.LENGTH_LONG).show();
                                return;
                            }else if (TextUtils.isEmpty(eventdate.getText())){
                                Toast.makeText(teachercreatequiz.this,"please provide a event date",Toast.LENGTH_LONG).show();
                                return;
                            }
                            int quesize = questions.size();
                            Log.d("error",Integer.toString(quesize));
                            int i = 0;
                            for(int j =0;j<questions.size();j++){
                                i += questions.get(j).getMarks();
                            }
                            Log.d("error",Integer.toString(i));

                            String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                            reference.child("Quizzes").child(quizid.getText().toString()).child("questions").setValue(questions);
                            reference.child("Quizzes").child(quizid.getText().toString()).child("upload_date").setValue(date);
                            reference.child("Quizzes").child(quizid.getText().toString()).child("Submission_date").setValue(eventdate.getText().toString());
                            reference.child("Quizzes").child(quizid.getText().toString()).child("total_marks").setValue(i);
                            //fetch all the students of this teacher from database and save them in an array

                            //iterate all the students of this teacher
                            for (String student:students){
                                Map<String,String> map = new HashMap<String,String>();
                                map.put("dateandtime",date);
                                map.put("description","quiz id "+quizid.getText().toString());
                                map.put("title","quiz");
                                reference.child("Events").child(student).push().setValue(map);
                            }
                            myDialog.dismiss();
                            //finish();

                        }
                    });
                }
            }
        });
    }
}
