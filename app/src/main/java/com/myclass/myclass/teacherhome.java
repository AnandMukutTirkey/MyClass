package com.myclass.myclass;

import android.app.usage.UsageEvents;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by anand on 17/01/18.
 */

public class teacherhome extends Fragment {

    StorageReference storageReference;
    Button events ;
    Button logout;
    TextView name,department,subject,id,emaill;
    ImageButton imageButton;
    DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.teacherhome,container,false);
        events = (Button)rootView.findViewById(R.id.eventsButton);
        logout = (Button)rootView.findViewById(R.id.logout);
        storageReference = FirebaseStorage.getInstance().getReference();
        name = (TextView)rootView.findViewById(R.id.nameValue);
        department = (TextView)rootView.findViewById(R.id.deptValue);
        subject = (TextView)rootView.findViewById(R.id.subjectvalue);
        id = (TextView)rootView.findViewById(R.id.sidValue);
        emaill = (TextView)rootView.findViewById(R.id.emailValue);
        imageButton = (ImageButton)rootView.findViewById(R.id.imageButton);



        final String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","").replace("@","");
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(email);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TeacherInfo teacher = dataSnapshot.getValue(TeacherInfo.class);
                name.setText(teacher.getName());
                department.setText(teacher.getDepartment());
                subject.setText(teacher.getSubject());
                id.setText(teacher.getId());
                emaill.setText(teacher.getEmail());
                Picasso.with(getContext()).load(teacher.getImageurl()).fit().centerCrop().into(imageButton);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(getContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });


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


        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"save to proceed",Toast.LENGTH_LONG).show();
                Log.d("fragment","proceed");
                //startActivity(new Intent(getContext(),teacherevents.class));
                Fragment fragment = new teacherevents();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_content_teacher,fragment);
                //transaction.addToBackStack("teacherevents").commit();
                transaction.commit();
            }
        });

        return rootView;
    }
}
