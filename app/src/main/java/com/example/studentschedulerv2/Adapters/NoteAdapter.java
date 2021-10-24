package com.example.studentschedulerv2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentschedulerv2.Entities.Note;
import com.example.studentschedulerv2.R;
import com.example.studentschedulerv2.UI.NoteDetails;

import java.util.List;

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteViewHolder> {

    class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView noteTitleView;
        private final TextView noteContentView;

        private NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitleView = itemView.findViewById(R.id.note_title_textview);
            noteContentView = itemView.findViewById(R.id.note_content_textview);

            // onClickListener to go here
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                final Note selectedNote = mNotes.get(position);

                Intent intent = new Intent(context, NoteDetails.class);
                intent.putExtra("noteId", selectedNote.getId());
                intent.putExtra("title", selectedNote.getTitle());
                intent.putExtra("content", selectedNote.getContent());
                intent.putExtra("courseId", selectedNote.getCourseId());
                context.startActivity(intent);
            });
        }
    }

    public static class NoteDiff extends DiffUtil.ItemCallback<Note> {

        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId() == newItem.getId();
        }
    }

    private List<Note> mNotes;
    private final Context context;

    public NoteAdapter(@NonNull DiffUtil.ItemCallback<Note> diffCallback, Context context) {
        super(diffCallback);
        this.context = context;
    }

    @NonNull
    @Override
    public NoteAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.NoteViewHolder holder, int position) {
        if (mNotes != null) {
            Note current = mNotes.get(position);
            holder.noteTitleView.setText(current.getTitle());
            holder.noteContentView.setText(current.getContent());
        } else {
            holder.noteTitleView.setText("No notes.");
        }
    }

    public void setNotes(List<Note> notes) {
        mNotes = notes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mNotes != null)
            return mNotes.size();
        else return 0;
    }
}
