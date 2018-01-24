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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class getinfot extends AppCompatActivity {

    EditText name,id;
    Spinner department,subject;
    Button submit;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    DatabaseReference databaseReference ;
    private static final int GALLERY_INTENT = 2;
    private String imageUrl ;
    ImageButton pickt ;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getinfot);

        name = (EditText)findViewById(R.id.name);
        id = (EditText)findViewById(R.id.id);
        department = (Spinner)findViewById(R.id.department);
        subject = (Spinner)findViewById(R.id.subject);
        submit = (Button)findViewById(R.id.submit);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        pickt = (ImageButton)findViewById(R.id.pickt);
        progressDialog = new ProgressDialog(this);
        storageReference = FirebaseStorage.getInstance().getReference();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = name.getText().toString().trim();
                String userid = id.getText().toString().trim();
                String userdepartment = department.getSelectedItem().toString().trim();
                String useremail = firebaseAuth.getCurrentUser().getEmail().toString();

                String usersubject = subject.getSelectedItem().toString().trim();

                if (TextUtils.isEmpty(username)){
                    Toast.makeText(getinfot.this,"Please provide username",Toast.LENGTH_LONG).show();
                    return;
                }else if (TextUtils.isEmpty(userid)){
                    Toast.makeText(getinfot.this,"Please provide userid",Toast.LENGTH_LONG).show();
                    return;
                }else if (TextUtils.isEmpty(userdepartment)){
                    Toast.makeText(getinfot.this,"Please provide department",Toast.LENGTH_LONG).show();
                    return;
                }else if (TextUtils.isEmpty(usersubject)){
                    Toast.makeText(getinfot.this,"Please provide year",Toast.LENGTH_LONG).show();
                    return;
                }else if (TextUtils.isEmpty(imageUrl)){
                    Toast.makeText(getinfot.this,"Please provide a profile picture",Toast.LENGTH_LONG).show();
                    return;
                }

                progressDialog.setMessage("Saving Informations ...");
                progressDialog.show();

                TeacherInfo user = new TeacherInfo(useremail,username,userid,userdepartment,usersubject,"Teacher",imageUrl);
                databaseReference.child("users").child(useremail.replace(".","").replace("@","")).setValue(user);
                databaseReference.child("course").child(usersubject).setValue(useremail.replace("@","").replace(".",""));

                progressDialog.dismiss();
                Toast.makeText(getinfot.this,"Information Saved",Toast.LENGTH_LONG).show();

                //finish();
                startActivity(new Intent(getinfot.this,Teacher.class));
            }
        });

        pickt.setOnClickListener(new View.OnClickListener() {
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
            pickt.setImageURI(uri);
            String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","").replace("@","");
            StorageReference filepath = storageReference.child("profileimage").child(email);
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getinfot.this,"Image Uploded",Toast.LENGTH_LONG).show();
                    imageUrl = taskSnapshot.getDownloadUrl().toString();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getinfot.this,"Uploding failed : "+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });

        }else{
            Toast.makeText(getinfot.this,"This image can't be uploaded.",Toast.LENGTH_LONG).show();
        }
    }
}
