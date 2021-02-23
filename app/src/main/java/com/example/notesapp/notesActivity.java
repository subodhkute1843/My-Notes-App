package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class notesActivity extends AppCompatActivity {


    FloatingActionButton mcreateNoteFab;
    private FirebaseAuth firebaseAuth;


    RecyclerView mrecyclerview;

    //staggered grid layout manager
    StaggeredGridLayoutManager staggeredGridLayoutManager;


    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    //declare a variable of firestore recycler adapter type
    FirestoreRecyclerAdapter<firebasemodel , NoteViewHolder>  noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        firebaseAuth = FirebaseAuth.getInstance();
        mcreateNoteFab = findViewById(R.id.createNoteFab);

        //going to use only single users data
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        getSupportActionBar().setTitle("All Notes");

        //start new activity
        mcreateNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(notesActivity.this, createnote.class));
            }
        });


        //we have to load data for that specific user
        Query query = firebaseFirestore.collection("notes").document(firebaseUser.getUid())
                .collection("myNotes").orderBy("title" , Query.Direction.ASCENDING);

        //assign data to recycler view
        FirestoreRecyclerOptions<firebasemodel> allusersnotes = new FirestoreRecyclerOptions.Builder<firebasemodel>()
                .setQuery(query , firebasemodel.class).build();

        noteAdapter = new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(allusersnotes) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull final firebasemodel firebasemodel) {

                final ImageView popupButton = noteViewHolder.itemView.findViewById(R.id.menuPop);

                int colorcode = getRandomColor();         //this function returnthe color code
                noteViewHolder.mnote.setBackgroundColor(noteViewHolder.itemView.getResources().getColor(colorcode, null));

                //we have to set data on side on our recycler view
                noteViewHolder.notetitle.setText(firebasemodel.getTitle());
                noteViewHolder.notecontent.setText(firebasemodel.getContent());

                //delete note with id
                final String docId = noteAdapter.getSnapshots().getSnapshot(i).getId();


                //on click listener to every note
                noteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //we have to open note detail activity

                        Intent intent = new Intent(v.getContext(),notesDetails.class);
                        intent.putExtra("title" , firebasemodel.getTitle());
                        intent.putExtra("content" , firebasemodel.getContent());
                        intent.putExtra("noteId" ,docId);


                        v.getContext().startActivity(intent);
                        //Toast.makeText(getApplicationContext(), "this is clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                popupButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                        popupMenu.setGravity(Gravity.END);
                        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Intent intent = new Intent(v.getContext(),editNoteActivity.class);
                                intent.putExtra("title" , firebasemodel.getTitle());
                                intent.putExtra("content" , firebasemodel.getContent());
                                intent.putExtra("noteId" ,docId);
                                v.getContext().startActivity(intent);
                                return false;
                            }
                        });
                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                DocumentReference documentReference = firebaseFirestore.collection("notes")
                                        .document(firebaseUser.getUid()).collection("myNotes").document(docId);
                                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {


                                        Toast.makeText(v.getContext(), "note is deleted", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(v.getContext(), "failed to delete ", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                return false;
                            }
                        });

                        //show popup menu without it popup menu wont work
                        popupMenu.show();
                    }
                });

            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout , parent ,false);
                return new NoteViewHolder(view);
            }
        };


        //load data in recyclerview
        mrecyclerview = findViewById(R.id.recyclerView);
        mrecyclerview.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2 , StaggeredGridLayoutManager.VERTICAL);
        mrecyclerview.setLayoutManager(staggeredGridLayoutManager);
        //set whole data in recyclerview
        mrecyclerview.setAdapter(noteAdapter);


    }


    public class NoteViewHolder extends RecyclerView.ViewHolder{

        private TextView notetitle , notecontent;
        LinearLayout mnote;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            notetitle = itemView.findViewById(R.id.noteTitle);
            notecontent= itemView.findViewById(R.id.noteContent);
            mnote = itemView.findViewById(R.id.note);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.logout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(notesActivity.this  , MainActivity.class ));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (noteAdapter!= null){
            noteAdapter.stopListening();
        }
    }

    private int getRandomColor(){
        List<Integer> colorcode = new ArrayList<>();
        colorcode.add(R.color.khaki);
        colorcode.add(R.color.KESHARI);
        colorcode.add(R.color.JAMBHALA);
        colorcode.add(R.color.color3);
        colorcode.add(R.color.color4);
        colorcode.add(R.color.SKIN);
        colorcode.add(R.color.HIRWA);
        colorcode.add(R.color.PIWLA);
        colorcode.add(R.color.AKASHI);
        colorcode.add(R.color.color1);
        colorcode.add(R.color.color2);
        colorcode.add(R.color.grey);

        Random random = new Random();
        int number = random.nextInt(colorcode.size());
        return colorcode.get(number);

    }
}
















