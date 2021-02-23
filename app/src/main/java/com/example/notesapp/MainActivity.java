package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class MainActivity extends AppCompatActivity {

    private Button mgotoSignUp;
    private TextView mgotoForgetPass;
    private EditText mloginMail , mloginPassword;
    private RelativeLayout mlogin;

    private FirebaseAuth firebaseAuth;

    private ProgressBar mprogressBarOfMainActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        mloginMail = findViewById(R.id.loginMail);
        mloginPassword = findViewById(R.id.loginPassword);
        mlogin = findViewById(R.id.login);
        mgotoSignUp = findViewById(R.id.gotoSignUp);
        mgotoForgetPass = findViewById(R.id.gotoForgetPass);

        mprogressBarOfMainActivity = findViewById(R.id.progressBarOfMainActivity);


        firebaseAuth = FirebaseAuth.getInstance();
        //we want to login with current user account
        //for that take the instance of user also

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null){
            finish();
            startActivity(new Intent(MainActivity.this , notesActivity.class));
        }


        mgotoSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this , signup.class));
            }
        });


        mgotoForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this , forgetpassword.class);
                startActivity(intent2);
            }
        });

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mail = mloginMail.getText().toString().trim();
                String pass = mloginPassword.getText().toString().trim();

                if (mail.isEmpty() || pass.isEmpty()){
                    Toast.makeText(MainActivity.this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
                }else{
                    //log in the user

                    mprogressBarOfMainActivity.setVisibility(View.VISIBLE);

                    firebaseAuth.signInWithEmailAndPassword(mail , pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            if (task.isSuccessful()){
                                checkMailVerification();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Account Does Not Exist !!", Toast.LENGTH_SHORT).show();
                                mprogressBarOfMainActivity.setVisibility(View.INVISIBLE);
                            }
                        }
                    });


                }
            }
        });
    }

    private void checkMailVerification(){

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser.isEmailVerified() == true){
            Toast.makeText(getApplicationContext(), "Logged In" , Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this , notesActivity.class));
        }else{

            mprogressBarOfMainActivity.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Verify Your Mail First", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}
