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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText email;
    private EditText password;
    private Button loginButton;
    private TextView signInlink;

    String emailEntered;
    String passwordEnter;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth=FirebaseAuth.getInstance();
        email=findViewById(R.id.emailText);
        password=findViewById(R.id.passwordText);
        loginButton=findViewById(R.id.signupButton);
        signInlink=findViewById(R.id.signinLink);

        if(firebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        loginButton.setOnClickListener(this);
        signInlink.setOnClickListener(this);

    }


    private void loginUser(){
        emailEntered = email.getText().toString().trim();
        passwordEnter = password.getText().toString().trim();

        if (emailEntered.isEmpty()) {
            email.setError("Email is empty!");
        } else if (passwordEnter.isEmpty()) {
            password.setError("Password is empty");
        } else if (emailEntered.isEmpty() && passwordEnter.isEmpty()) {
            email.setError("Email is empty!");
            password.setError("Password is empty");
        } else {
            firebaseAuth.signInWithEmailAndPassword(emailEntered,passwordEnter).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Welcome!",Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),"Error, try again!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {

        if (v == loginButton) {
            loginUser();
        }else if(v==signInlink){
            startActivity(new Intent(getApplicationContext(),SignUpUser.class));
        }
    }
}
