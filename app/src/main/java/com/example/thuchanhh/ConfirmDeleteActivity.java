package com.example.thuchanhh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ConfirmDeleteActivity extends AppCompatActivity {

    private Button btnCancelDelete;
    private Button btnConfirmDelete;
    
    private DatabaseHelper databaseHelper;
    private int noteId;
    private boolean isDeleting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_delete);

        databaseHelper = new DatabaseHelper(this);

        btnCancelDelete = findViewById(R.id.btnCancelDelete);
        btnConfirmDelete = findViewById(R.id.btnConfirmDelete);

        noteId = getIntent().getIntExtra("NOTE_ID", -1);
        if (noteId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy ghi chú!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnCancelDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnConfirmDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDeleting) {
                    return;
                }
                
                isDeleting = true;
                btnConfirmDelete.setEnabled(false);

                int rowsDeleted = databaseHelper.deleteNote(noteId);
                if (rowsDeleted > 0) {
                    Toast.makeText(ConfirmDeleteActivity.this, "Đã xóa ghi chú", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ConfirmDeleteActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ConfirmDeleteActivity.this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                    isDeleting = false;
                    btnConfirmDelete.setEnabled(true);
                }
            }
        });
    }
}