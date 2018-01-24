package com.myclass.myclass;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

public class Register extends AppCompatActivity implements View.OnClickListener{


    private Button register;
    private EditText email,repassword;
    private EditText password;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private Dialog dialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        register = (Button)findViewById(R.id.register);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        progressDialog = new ProgressDialog(this);
        repassword = (EditText)findViewById(R.id.repassword);

        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String emailId = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String repass = repassword.getText().toString().trim();
        if (TextUtils.isEmpty(emailId)){
            Toast.makeText(this,"Email is empty",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Password is empty",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.equals(pass,repass)){
            Toast.makeText(this,"Password doesn't match",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(emailId,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(Register.this,"User Registered",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            //finish();
                            dialog = new Dialog(Register.this);
                            dialog.setContentView(R.layout.usertype);
                            dialog.setCancelable(false);
                            dialog.show();
                            Button teacher = (Button)dialog.findViewById(R.id.teacher) ;
                            Button student = (Button)dialog.findViewById(R.id.student);
                            teacher.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                    finish();
                                    startActivity(new Intent(getApplicationContext(),getinfot.class));
                                }
                            });
                            student.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                    finish();
                                    startActivity(new Intent(getApplicationContext(),getinfo.class));
                                }
                            });



                        }else{
                            FirebaseException exp = (FirebaseException)task.getException();
                            progressDialog.dismiss();
                            Toast.makeText(Register.this,"Registration failed : " + exp.getMessage(),Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }

}
