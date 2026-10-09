package com.example.thuchanhh;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditNoteActivity extends AppCompatActivity {

    private EditText etEditTitle;
    private EditText etEditContent;
    private Button btnSaveChanges;
    private Button btnCancelEdit;

    private DatabaseHelper databaseHelper;
    private Note currentNote;
    private int noteId;
    private boolean isSaving = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        databaseHelper = new DatabaseHelper(this);

        etEditTitle = findViewById(R.id.etEditTitle);
        etEditContent = findViewById(R.id.etEditContent);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnCancelEdit = findViewById(R.id.btnCancelEdit);

        noteId = getIntent().getIntExtra("NOTE_ID", -1);
        if (noteId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy ghi chú!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadNoteData();

        btnCancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSaving) {
                    return;
                }

                String newTitle = etEditTitle.getText().toString().trim();
                String newContent = etEditContent.getText().toString().trim();

                if (newTitle.isEmpty()) {
                    etEditTitle.setError("Tiêu đề không được để trống!");
                    etEditTitle.requestFocus();
                    return;
                }

                isSaving = true;
                btnSaveChanges.setEnabled(false);

                currentNote.setTitle(newTitle);
                currentNote.setContent(newContent);
                currentNote.setUpdatedAt(System.currentTimeMillis());

                int rowsAffected = databaseHelper.updateNote(currentNote);

                if (rowsAffected > 0) {
                    Toast.makeText(EditNoteActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditNoteActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                    isSaving = false;
                    btnSaveChanges.setEnabled(true);
                }
            }
        });
    }

    private void loadNoteData() {
        currentNote = databaseHelper.getNoteById(noteId);

        if (currentNote == null) {
            Toast.makeText(this, "Ghi chú không còn tồn tại!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etEditTitle.setText(currentNote.getTitle());
        etEditContent.setText(currentNote.getContent());
    }
}