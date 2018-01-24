package com.myclass.myclass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.session.MediaSession;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity  {

    private Button login;
    private EditText email;
    private EditText password;
    private Button signup,forgot;
    private Button about;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    static String type;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null){
            //profile activity here
        }
        login = (Button)findViewById(R.id.login);
        signup = (Button)findViewById(R.id.signup);
        about = (Button)findViewById(R.id.forgot);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        progressDialog = new ProgressDialog(this);
        forgot = (Button)findViewById(R.id.forgot);


        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(email.getText().toString().trim())){
                    Toast.makeText(MainActivity.this,"Please provide email in the field and click here",Toast.LENGTH_LONG).show();
                    return;
                }
                FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this,"Password reset link sent to email "+email.getText().toString().trim(),Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(MainActivity.this,"failed",Toast.LENGTH_LONG).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,e.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailid = email.getText().toString().trim();
                String pass = password.getText().toString().trim();
                progressDialog.setMessage("logging in ...");
                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(emailid,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","").replace("@","");
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(email).child("type");
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    type = dataSnapshot.getValue().toString();
                                    if (type.equals("Teacher")){
                                        progressDialog.dismiss();
                                        finish();
                                        startActivity(new Intent(getApplicationContext(),Teacher.class));
                                    }else if (type.equals("Student")){
                                        progressDialog.dismiss();
                                        finish();
                                        startActivity(new Intent(getApplicationContext(),Student.class));
                                    }else{
                                        progressDialog.dismiss();
                                        Log.e("mytab","unknown type of user" + type);
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            //Toast.makeText(MainActivity.this,"logged in" + type,Toast.LENGTH_LONG).show();
                        }else{
                            progressDialog.dismiss();
                            try {
                                throw task.getException();
                            }catch (FirebaseAuthUserCollisionException e){
                                //Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                Log.d("error",e.getMessage());
                            }catch (FirebaseNetworkException e){
                                //Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                Log.d("error",e.getLocalizedMessage());
                            }catch (Exception e){
                                //Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                Log.d("error",e.getLocalizedMessage());
                            }
                        }
                    }
                });
            }
        });
    }
}
