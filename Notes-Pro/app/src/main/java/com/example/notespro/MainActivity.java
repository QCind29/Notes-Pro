package com.example.notespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.notespro.Adapter.NotesAdapter;
import com.example.notespro.Model.Notes;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    NotesAdapter notesAdapter;
    ImageButton imgButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = findViewById(R.id.add_ic);
        imgButton = findViewById(R.id.imgbtMenu);
        imgButton.setOnClickListener(view -> showMenu());


        recyclerView = findViewById(R.id.re_cycler);

        floatingActionButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, NoteDetailActivity.class )));

        setUpRecyclerView();


    }

    private void showMenu() {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this,imgButton);
        popupMenu.getMenu().add("Log out");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle()=="Log out"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                    return true;

                }
                return false;
            }
        });
    }
    void setUpRecyclerView() {
         Query query = Utility.getCollectionReferenceForNotes().orderBy("time",Query.Direction.DESCENDING);
         FirestoreRecyclerOptions<Notes> options = new FirestoreRecyclerOptions.Builder<Notes>()
                 .setQuery(query,Notes.class).build();

         recyclerView.setLayoutManager(new LinearLayoutManager(this));
         notesAdapter = new NotesAdapter(options,this);
         recyclerView.setAdapter(notesAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        notesAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        notesAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        notesAdapter.notifyDataSetChanged();
    }
}