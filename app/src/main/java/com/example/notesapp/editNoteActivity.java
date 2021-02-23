package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

public class editNoteActivity extends AppCompatActivity {

    Intent data;
    EditText mEditTitle , mEditiContentofnote;
    FloatingActionButton mSaveEditNote;

    FirebaseAuth firebaeAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        mEditTitle = findViewById(R.id.editTitleOfNote);
        mEditiContentofnote = findViewById(R.id.editContentOfNote);
        mSaveEditNote = findViewById(R.id.saveEditNote);
        data=getIntent();

        //authrentication of firebase for edit button
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        Toolbar toolbar = findViewById(R.id.toolBarOfeditNOte);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mSaveEditNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(editNoteActivity.this, "save button clicked", Toast.LENGTH_SHORT).show();
                String newTitle = mEditTitle.getText().toString();
                String newContent = mEditiContentofnote.getText().toString();


                if (newTitle.isEmpty() || newContent.isEmpty()){
                    Toast.makeText(getApplicationContext(), "something is empty!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    DocumentReference documentReference = firebaseFirestore.collection("notes")
                            .document(firebaseUser.getUid()).collection("myNotes").document(data.getStringExtra("noteId"));
                    Map<String , Object> note = new HashMap<>();
                    note.put("title",newTitle);
                    note.put("content",newContent);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(editNoteActivity.this, "note updated", Toast.LENGTH_SHORT).show();

                            //other method of intent
                            Intent intent = new Intent(editNoteActivity.this , notesActivity.class);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(editNoteActivity.this, "failed to update", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        //set previous data
        String notetitle = data.getStringExtra("title");
        String notecontent= data.getStringExtra("content");
        mEditiContentofnote.setText(notecontent);
        mEditTitle.setText(notetitle);
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
