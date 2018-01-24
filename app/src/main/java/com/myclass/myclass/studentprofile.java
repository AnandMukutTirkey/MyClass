package com.myclass.myclass;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by anand on 16/01/18.
 */
import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.myclass.myclass.Student.mFirebasedatabase;

public class studentprofile extends Fragment {

    private Button quizButton,logout;
    private ChildEventListener childEventListener;
    private EditText quiz_code;
    private Dialog myDialog;
    private String code;
    private String email;
    private User student;
    private TextView name,dept,id,mail,subject;
    private DatabaseReference quizReference,reference;
    private View rootView;
    private ImageView image;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.studentprofile,container,false);
        //student = (User)getArguments().getSerializable("MyData");

        email = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","").replace("@","");
        reference = mFirebasedatabase.getReference("users");
        subject = (TextView) rootView.findViewById(R.id.subjectvalue);
        dept =(TextView) rootView.findViewById(R.id.deptValue);
        id =(TextView) rootView.findViewById(R.id.sidValue);
        mail =(TextView) rootView.findViewById(R.id.emailValue);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot dataSnapshot1 = dataSnapshot.child(email);
                //dataSnapshot1.getValue(User.class);
                // Log.d("",dataSnapshot1.getValue().toString());
                student = dataSnapshot1.getValue(User.class);
                name = (TextView) rootView.findViewById(R.id.nameValue);
                name.setText(student.getName());

                dept.setText(student.getDepartment());

                id.setText(student.getId());

                mail.setText(student.getEmail());

                image = (ImageView)rootView.findViewById(R.id.imageView);

                subject.setText(student.getSubject());

                Picasso.with(getContext()).load(student.getImageurl()).fit().centerCrop().into(image);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        quizButton = (Button) rootView.findViewById(R.id.quizButton);
        logout = (Button)rootView.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                getActivity().finish();
                Intent intent = new Intent(getContext(),MainActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                getContext().getApplicationContext().startActivity(intent);
            }
        });

        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog = new Dialog(getActivity());
                myDialog.setContentView(R.layout.quiz_dialogue);
                myDialog.setCancelable(false);
                Button access=(Button)myDialog.findViewById(R.id.accessQuiz);
                quiz_code = (EditText) myDialog.findViewById(R.id.code);
                myDialog.show();

                access.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        code=quiz_code.getText().toString();

                        if(code.matches("") || code.length()<6){
                            Toast.makeText(getContext(),"Please Enter valid quiz code",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        quizReference = mFirebasedatabase.getReference().child("Quizzes");
                        quizReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild(code)) {
                                    if(dataSnapshot.child(code).child("StudentsInfo").hasChild(email))
                                    {
                                        Toast.makeText(getContext(),"Quiz Attempted",Toast.LENGTH_SHORT).show();
                                        quiz_code.setText("");
                                        return;
                                    }
                                    Iterable<DataSnapshot> questions = dataSnapshot.child(code).child("questions").getChildren();
                                    // QuizData qd = .getValue(QuizData.class);
                                    ArrayList<QuizQuestionData> qq =new ArrayList<>();
                                    for (DataSnapshot contact : questions) {
                                        QuizQuestionData c = contact.getValue(QuizQuestionData.class);
                                        qq.add(c);

                                    }
                                    myDialog.dismiss();
                                    Intent intent = new Intent(getContext() , QuizMainActivity.class);
                                    intent.putExtra("questionsList",qq);
                                    intent.putExtra("quiz_code",code);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(getContext(),"Please Enter valid quiz code",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {


                            }
                        });
                        //  Query phoneQuery = mDatabaseReference.orderByChild("quiz_code").equalTo("111111");
                    }
                });



                myDialog.setOnKeyListener(new Dialog.OnKeyListener() {

                    @Override
                    public boolean onKey(DialogInterface arg0, int keyCode,
                                         KeyEvent event) {
                        // TODO Auto-generated method stub
                        if (keyCode == KeyEvent.KEYCODE_BACK) {

                            myDialog.dismiss();
                        }
                        return true;
                    }
                });


            }
        });



        return rootView;
    }
}
