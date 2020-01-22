package com.example.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpUser extends AppCompatActivity implements View.OnClickListener{

    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button signUpButton;

    String emailEntered;
    String passwordEnter;
    String passwordConfirm;


    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_user2);

        firebaseAuth=FirebaseAuth.getInstance();
        email=findViewById(R.id.emailText);
        password=findViewById(R.id.passwordText);
        confirmPassword=findViewById(R.id.passwordConfirm);
        signUpButton=findViewById(R.id.signupButton);


        signUpButton.setOnClickListener(this);

    }


    private void signinUser(){
        emailEntered = email.getText().toString().trim();
        passwordEnter = password.getText().toString().trim();
        passwordConfirm=confirmPassword.getText().toString().trim();

        if (emailEntered.isEmpty()) {
            email.setError("Email is empty!");
        } else if (passwordEnter.isEmpty()){
            password.setError("Password is empty");
        }else if(passwordConfirm.isEmpty()){
            confirmPassword.setError("Confirm password is empty");
        }else if(emailEntered.isEmpty() && passwordEnter.isEmpty() && passwordConfirm.isEmpty()) {
            email.setError("Email is empty!");
            password.setError("Password is empty");
        }else if(emailEntered.isEmpty() || passwordEnter.isEmpty() || passwordConfirm.isEmpty()){
            Toast.makeText(getApplicationContext(),"Some fields are empty!",Toast.LENGTH_SHORT).show();
        }else if(!passwordEnter.equals(passwordConfirm)){
            confirmPassword.setError("Passwords don't match");
        }else{
            firebaseAuth.createUserWithEmailAndPassword(emailEntered,passwordEnter).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(SignUpUser.this,"Registered Successfully! Please check email for verification.",Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(SignUpUser.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(SignUpUser.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

    @Override
    public void onClick(View v) {


        if (v == signUpButton) {
            signinUser();
        }
    }
}
