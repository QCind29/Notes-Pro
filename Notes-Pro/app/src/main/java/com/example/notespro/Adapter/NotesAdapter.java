package com.example.notespro.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notespro.Model.Notes;
import com.example.notespro.NoteDetailActivity;
import com.example.notespro.R;
import com.example.notespro.Utility;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NotesAdapter extends FirestoreRecyclerAdapter <Notes, NotesAdapter.NotesViewHolder> {
    Context context;
    Notes notes;

    public NotesAdapter(@NonNull FirestoreRecyclerOptions<Notes> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NotesViewHolder holder, int position, @NonNull Notes notes) {
        holder.txtTitle.setText(notes.getTitle());
        holder.txtContent.setText(notes.getContent());
        holder.txtTime.setText(notes.getTime());
        String docId = this.getSnapshots().getSnapshot(position).getId();

        holder.itemView.setOnClickListener((v) ->{
            Intent intent = new Intent(context, NoteDetailActivity.class);
//            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("title",notes.getTitle());
            intent.putExtra("content",notes.getContent());
            intent.putExtra("docId",docId);
            context.startActivity(intent);
        } );
        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
               // contextMenu.setHeaderTitle("Select");
                contextMenu.add(0,R.id.ctDelete,0,"Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                        DocumentReference documentReference;
                        documentReference = Utility.getCollectionReferenceForNotes().document(docId);
                        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Utility.showToast(context, "Note have been deleted");

                                }else {
                                    Utility.showToast(context, "Failed!");

                                }
                            }
                        });

                        notifyDataSetChanged();
                        return false;
                    }
                });
            }
        });

    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_item, parent,false);
        return new NotesViewHolder(view);
    }

    class NotesViewHolder extends RecyclerView.ViewHolder{
        TextView txtTitle, txtContent, txtTime;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtContent = itemView.findViewById(R.id.txtContent);
            txtTime = itemView.findViewById(R.id.txtTime);



        }
    }
}
