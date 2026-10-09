package com.example.thuchanhh;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private Context context;
    private List<Note> noteList;

    public NoteAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    public void setNotes(List<Note> noteList) {
        this.noteList = noteList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        
        holder.tvItemTitle.setText(note.getTitle());
        
        if (note.getContent() == null || note.getContent().trim().isEmpty()) {
            holder.tvItemPreview.setText("Không có nội dung");
        } else {
            holder.tvItemPreview.setText(note.getContent());
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        holder.tvItemDate.setText("Cập nhật: " + sdf.format(new Date(note.getUpdatedAt())));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NoteDetailActivity.class);
                intent.putExtra("NOTE_ID", note.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteList != null ? noteList.size() : 0;
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemTitle;
        TextView tvItemPreview;
        TextView tvItemDate;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemTitle = itemView.findViewById(R.id.tvItemTitle);
            tvItemPreview = itemView.findViewById(R.id.tvItemPreview);
            tvItemDate = itemView.findViewById(R.id.tvItemDate);
        }
    }
}