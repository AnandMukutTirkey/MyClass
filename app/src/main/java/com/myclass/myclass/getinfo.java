package com.myclass.myclass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class getinfo extends AppCompatActivity {

    EditText name,id;
    Button submit;
    Spinner batch,department,year,subject;
    ImageButton pick;
    StorageReference storageReference;
    private static final int GALLERY_INTENT = 2;
    private String imageUrl ;
    ProgressDialog progressDialog;


    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getinfo);

        name = (EditText)findViewById(R.id.name);
        id = (EditText)findViewById(R.id.id);
        batch = (Spinner)findViewById(R.id.batch);
        department = (Spinner)findViewById(R.id.department);
        year = (Spinner)findViewById(R.id.year);
        subject = (Spinner)findViewById(R.id.subject);
        submit = (Button)findViewById(R.id.submit);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        pick = (ImageButton)findViewById(R.id.pick);
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = name.getText().toString().trim();
                String userid = id.getText().toString().trim();
                String userbatch = batch.getSelectedItem().toString().trim();
                String userdepartment = department.getSelectedItem().toString().trim();
                String useryear = year.getSelectedItem().toString().trim();
                final String useremail = firebaseAuth.getCurrentUser().getEmail().toString();
                String usersubject = subject.getSelectedItem().toString().trim();

                if (TextUtils.isEmpty(username)){
                    Toast.makeText(getinfo.this,"Please provide username",Toast.LENGTH_LONG).show();
                    return;
                }else if (TextUtils.isEmpty(userid)){
                    Toast.makeText(getinfo.this,"Please provide userid",Toast.LENGTH_LONG).show();
                    return;
                }else if (TextUtils.isEmpty(userdepartment)){
                    Toast.makeText(getinfo.this,"Please provide department",Toast.LENGTH_LONG).show();
                    return;
                }else if (TextUtils.isEmpty(useryear)){
                    Toast.makeText(getinfo.this,"Please provide year",Toast.LENGTH_LONG).show();
                    return;
                }else if (TextUtils.isEmpty(imageUrl)){
                    Toast.makeText(getinfo.this,"Please provide a profile picture",Toast.LENGTH_LONG).show();
                    return;
                }else if (TextUtils.isEmpty(usersubject)){
                    Toast.makeText(getinfo.this,"please provide your subject",Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setMessage("Saving Informations ...");
                progressDialog.show();

                User user = new User(useremail,userid,username,useryear,userdepartment,userbatch,"Student",imageUrl,usersubject);
                databaseReference.child("users").child(useremail.replace(".","").replace("@","")).setValue(user);
                FirebaseDatabase.getInstance().getReference().child("course").child(usersubject).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String teachermail = dataSnapshot.getValue(String.class).replace("@","").replace(".","");
                        //Toast.makeText(getinfo.this,"teachermail "+teachermail,Toast.LENGTH_LONG).show();
                        databaseReference.child("class").child(teachermail).child(useremail.replace(".","").replace("@","")).setValue(useremail);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Toast.makeText(getinfo.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

                progressDialog.dismiss();
                Toast.makeText(getinfo.this,"Information Saved",Toast.LENGTH_LONG).show();
                //finish();
                startActivity(new Intent(getinfo.this,Student.class));
            }
        });

        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            Uri uri = data.getData();
            pick.setImageURI(uri);
            String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","").replace("@","");
            StorageReference filepath = storageReference.child("profileimage").child(email);
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getinfo.this,"Image Uploded",Toast.LENGTH_LONG).show();
                    imageUrl = taskSnapshot.getDownloadUrl().toString();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getinfo.this,"Image uploding failed : "+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });

        }else{
            Toast.makeText(getinfo.this,"This image can't be uploaded.",Toast.LENGTH_LONG).show();
        }
    }
}
