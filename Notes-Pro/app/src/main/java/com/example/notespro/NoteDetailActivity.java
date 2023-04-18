package com.example.notespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.notespro.Model.Notes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import java.lang.annotation.Documented;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class NoteDetailActivity extends AppCompatActivity {
    ImageButton imageButton;
    EditText edTitle, edContent;
    TextView pagetitle;
    String title, content, id;
    boolean isEditmode = false;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        edTitle = findViewById(R.id.edTitle);
        edContent = findViewById(R.id.edContent);

        pagetitle = findViewById(R.id.page_title);

        imageButton = findViewById(R.id.imgbtSave);

        imageButton.setOnClickListener(view -> saveNote());

        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        id = getIntent().getStringExtra("docId");

        edTitle.setText(title);
        edContent.setText(content);

        if(id!=null ){
            isEditmode = true;
        }
        if(isEditmode = true){
            pagetitle.setText("Edit note");
        }

    }

     void saveNote() {
        String Title = edTitle.getText().toString();
        String Content = edContent.getText().toString();
         java.util.Date currentDate = new Date();
         SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
         String dateString = dateFormat.format(currentDate);


//         if(Title == null || Title.isEmpty()){
         if(Title == null){
            edTitle.setError("Title can not be empty");
            return;
        }
         Notes notes = new Notes();
         notes.setTitle(Title);
         notes.setContent(Content);
         notes.setTime(dateString);

         saveNotetoFirebase(notes);

    }

     void saveNotetoFirebase(Notes notes) {
         DocumentReference documentReference;
         if(isEditmode){
             //documentReference = Utility.getCollectionReferenceForNotes().document(id);
             documentReference = Utility.getCollectionReferenceForNotes().document();
         }else{
             documentReference = Utility.getCollectionReferenceForNotes().document();
         }

         documentReference.set(notes).addOnCompleteListener(new OnCompleteListener<Void>() {
             @Override
             public void onComplete(@NonNull Task<Void> task) {
                 if(task.isSuccessful()){
                     Utility.showToast(NoteDetailActivity.this, "Note have been added");
                     finish();
                 }else {
                     Utility.showToast(NoteDetailActivity.this, "Failed!");

                 }
             }
         });
    }
}