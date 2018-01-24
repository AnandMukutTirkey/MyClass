package com.myclass.myclass;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by anand on 17/01/18.
 */

public class teacherquiz extends Fragment {

    public FirebaseDatabase mFirebasedatabase;
    private DatabaseReference quizReference;
    Button createquiz,rankbutton;
    EditText codeE;
    String code;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.teacherquiz,container,false);

        createquiz = (Button)rootView.findViewById(R.id.createquiz);
        rankbutton = (Button)rootView.findViewById(R.id.getrank);
        codeE=(EditText) rootView.findViewById(R.id.rqid);

        createquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(),teachercreatequiz.class));
            }
        });

        rankbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code=codeE.getText().toString();
                if(code.matches("") || code.length()<6){
                    Toast.makeText(getContext(),"Please Enter valid quiz code",Toast.LENGTH_SHORT).show();
                    return;
                }
                quizReference = FirebaseDatabase.getInstance().getReference().child("Quizzes");
                quizReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(code)) {
                            Intent intent = new Intent(getContext() , teacherrankquiz.class);

                            intent.putExtra("quizcode",code);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(getContext(),"Please Enter correct quiz code",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {


                    }
                });

                //  Intent intent = new Intent();
                // intent.putExtra("Quizcode", code);
                // startActivity(new Intent(getActivity(), teacherrankquiz.class));

            }
        });


        return rootView;
    }
}
