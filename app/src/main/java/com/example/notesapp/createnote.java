package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class createnote extends AppCompatActivity {

    EditText mcreateTitleOfNote, mcreateContentOfNote;
    FloatingActionButton msaveNote;
    FirebaseAuth firebaseAuth ;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    ProgressBar mprogressBarOfCreateNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createnote);



        msaveNote = findViewById(R.id.saveNote);
        mcreateContentOfNote = findViewById(R.id.createContentOfNote);
        mcreateTitleOfNote = findViewById(R.id.createTitleOfNote);

        mprogressBarOfCreateNote = findViewById(R.id.progressBarOfCreateNote);

        Toolbar toolbar = findViewById(R.id.toolBarOfCreateNote);
        setSupportActionBar(toolbar);

        //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        msaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = mcreateTitleOfNote.getText().toString();
                String content = mcreateContentOfNote.getText().toString();

                //check user entered something or not
                if (title.isEmpty() || content.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Both Feild Are Required", Toast.LENGTH_SHORT).show();
                }
                //save notes
                else{

                    //progressbar
                    mprogressBarOfCreateNote.setVisibility(View.VISIBLE);

                    DocumentReference documentReference = firebaseFirestore.collection("notes")
                            .document(firebaseUser.getUid()).collection("myNotes").document();
                    //1st collection is book , 2nd document is like page
                    Map<String , Object> note = new HashMap<>();
                    note.put("title" , title);
                    note.put("content", content);

                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //mprogressBarOfCreateNote.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Note Created Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(createnote.this , notesActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //mprogressBarOfCreateNote.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Failed To Crate Note", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //to get back to home
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);


    }
}


