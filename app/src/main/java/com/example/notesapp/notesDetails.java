package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class notesDetails extends AppCompatActivity {        //display data of our note


    private TextView mTitleOfNoteDetail , mContentOfNoteDetail;
    private FloatingActionButton mgotoEditNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_details);

        mTitleOfNoteDetail = findViewById(R.id.TitleOfNoteDetail);
        mContentOfNoteDetail = findViewById(R.id.ContentOfNoteDetail);
        mgotoEditNote = findViewById(R.id.gotoEditNote);
        Toolbar toolbar = findViewById(R.id.toolBarOfNoteDetail);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent data=getIntent();


        mgotoEditNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), editNoteActivity.class);
                intent.putExtra("title", data.getStringExtra("title"));
                intent.putExtra("content", data.getStringExtra("content"));
                intent.putExtra("noteId", data.getStringExtra("noteId"));
                v.getContext().startActivity(intent);
            }
        });

        mContentOfNoteDetail.setText(data.getStringExtra("content"));
        mTitleOfNoteDetail.setText(data.getStringExtra("title"));


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
