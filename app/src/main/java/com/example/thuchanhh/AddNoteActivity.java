package com.example.thuchanhh;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddNoteActivity extends AppCompatActivity {

    private EditText etTitle;
    private EditText etContent;
    private Button btnSaveNote;
    private Button btnCancel;
    
    private DatabaseHelper databaseHelper;
    private boolean isSaving = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        databaseHelper = new DatabaseHelper(this);

        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);
        btnSaveNote = findViewById(R.id.btnSaveNote);
        btnCancel = findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSaving) {
                    return;
                }

                String title = etTitle.getText().toString().trim();
                String content = etContent.getText().toString().trim();

                if (title.isEmpty()) {
                    etTitle.setError("Tiêu đề không được để trống!");
                    etTitle.requestFocus();
                    return;
                }

                isSaving = true;
                btnSaveNote.setEnabled(false);

                long currentTime = System.currentTimeMillis();
                Note note = new Note(title, content, currentTime, currentTime);
                
                long result = databaseHelper.insertNote(note);
                
                if (result != -1) {
                    Toast.makeText(AddNoteActivity.this, "Lưu ghi chú thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddNoteActivity.this, "Lưu thất bại, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    isSaving = false;
                    btnSaveNote.setEnabled(true);
                }
            }
        });
    }
}