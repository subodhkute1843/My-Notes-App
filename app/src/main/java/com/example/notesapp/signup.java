package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signup extends AppCompatActivity {

    private EditText msignUpEmail , msignUpPassword;
    private RelativeLayout msignUp;
    private TextView mgotoLogin;

    private FirebaseAuth firebaseAuth;

    private ProgressBar mprogressBarOfSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().hide();

        msignUpEmail = findViewById(R.id.signUpEmail);
        msignUpPassword = findViewById(R.id.signUpPassword);
        msignUp = findViewById(R.id.signUp);
        mgotoLogin = findViewById(R.id.gotoLogin);

        mprogressBarOfSignup = findViewById(R.id.progressBarOfSignup);


        //take instance of firebase auth
        firebaseAuth  = FirebaseAuth.getInstance();

        mgotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup.this , MainActivity.class);
                startActivity(intent);
            }
        });

        msignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String mail = msignUpEmail.getText().toString().trim();
                String pass = msignUpPassword.getText().toString().trim();


                if (mail.isEmpty() || pass.isEmpty()){
                    Toast.makeText(signup.this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
                }
                else if (pass.length() < 7){
                    Toast.makeText(signup.this, "Password Should Be Greater Than 7 digits ", Toast.LENGTH_SHORT).show();
                }else{
                    //register user to firebase

                    firebaseAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                mprogressBarOfSignup.setVisibility(View.VISIBLE);
                                Toast.makeText(signup.this, "Registration Successful 😎", Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                            }else{
                                mprogressBarOfSignup.setVisibility(View.INVISIBLE);
                                Toast.makeText(signup.this, "Failed To Register", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });
    }


    // send email verification
    private void sendEmailVerification(){
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Toast.makeText(signup.this, "Verifivation Email is Sent, Verify and Login Again", Toast.LENGTH_SHORT).show();
                    sendEmailVerification();
                    firebaseAuth.signOut();
                    startActivity(new Intent(signup.this , MainActivity.class));
                }
            });
        }
        else{
            Toast.makeText(this, "Failed to send verification Email !!", Toast.LENGTH_SHORT).show();
        }
    }


}
