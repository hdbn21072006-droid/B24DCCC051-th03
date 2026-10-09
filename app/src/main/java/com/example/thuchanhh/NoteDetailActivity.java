package com.example.thuchanhh;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteDetailActivity extends AppCompatActivity {

    private TextView tvNoteTitle;
    private TextView tvNoteCreatedAt;
    private TextView tvNoteUpdatedAt;
    private TextView tvNoteContent;

    private Button btnBack;
    private Button btnEditNote;
    private Button btnDeleteNote;

    private DatabaseHelper databaseHelper;
    private Note currentNote;
    private int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        databaseHelper = new DatabaseHelper(this);

        tvNoteTitle = findViewById(R.id.tvNoteTitle);
        tvNoteCreatedAt = findViewById(R.id.tvNoteCreatedAt);
        tvNoteUpdatedAt = findViewById(R.id.tvNoteUpdatedAt);
        tvNoteContent = findViewById(R.id.tvNoteContent);

        btnBack = findViewById(R.id.btnBack);
        btnEditNote = findViewById(R.id.btnEditNote);
        btnDeleteNote = findViewById(R.id.btnDeleteNote);

        noteId = getIntent().getIntExtra("NOTE_ID", -1);
        if (noteId == -1) {
            Toast.makeText(this, "Không tìm thấy dữ liệu ghi chú!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnEditNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentNote != null) {
                    Intent intent = new Intent(NoteDetailActivity.this, EditNoteActivity.class);
                    intent.putExtra("NOTE_ID", currentNote.getId());
                    startActivity(intent);
                }
            }
        });

        btnDeleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentNote != null) {
                    Intent intent = new Intent(NoteDetailActivity.this, ConfirmDeleteActivity.class);
                    intent.putExtra("NOTE_ID", currentNote.getId());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (noteId != -1) {
            loadNoteData();
        }
    }

    private void loadNoteData() {
        currentNote = databaseHelper.getNoteById(noteId);

        if (currentNote == null) {
            Toast.makeText(this, "Ghi chú không còn tồn tại!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvNoteTitle.setText(currentNote.getTitle());
        
        if (currentNote.getContent() == null || currentNote.getContent().trim().isEmpty()) {
            tvNoteContent.setText("(Không có nội dung)");
        } else {
            tvNoteContent.setText(currentNote.getContent());
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        tvNoteCreatedAt.setText("Ngày tạo: " + sdf.format(new Date(currentNote.getCreatedAt())));
        tvNoteUpdatedAt.setText("Cập nhật lần cuối: " + sdf.format(new Date(currentNote.getUpdatedAt())));
    }
}