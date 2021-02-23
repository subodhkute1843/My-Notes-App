package com.example.notesapp;

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
import com.google.firebase.auth.FirebaseAuth;

import java.io.FileReader;

public class forgetpassword extends AppCompatActivity {

    private EditText mforgotPass;
    private Button mpasswordRecoverBtn;
    private TextView mgoBackToLogin;


    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);

        //to hide action bar
        getSupportActionBar().hide();

        mforgotPass = findViewById(R.id.forgotPass);
        mpasswordRecoverBtn= findViewById(R.id.passwordRecoverBtn);
        mgoBackToLogin = findViewById(R.id.goBackToLogin);

        //take instance
        firebaseAuth = FirebaseAuth.getInstance();


        mgoBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(forgetpassword.this ,MainActivity.class);
                startActivity(intent);
            }
        });

        mpasswordRecoverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mail = mforgotPass.getText().toString().trim();
                if(mail.isEmpty()){
                    Toast.makeText(forgetpassword.this, "Enter Your Mail First", Toast.LENGTH_SHORT).show();
                }else{
                    //we have to send password recover email

                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(forgetpassword.this, "Mail Sent , You Can Recover Your Password Using Mail",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(forgetpassword.this , MainActivity.class));
                            }else{
                                Toast.makeText(forgetpassword.this, "email is wrong or Accountt not exist ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }
}
