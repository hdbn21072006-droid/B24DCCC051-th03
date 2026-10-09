package com.example.thuchanhh;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private NoteAdapter noteAdapter;
    private List<Note> noteList;

    private RecyclerView rvNotes;
    private TextView tvEmptyList;
    private EditText etSearch;
    private Button btnAddNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        noteList = new ArrayList<>();

        rvNotes = findViewById(R.id.rvNotes);
        tvEmptyList = findViewById(R.id.tvEmptyList);
        etSearch = findViewById(R.id.etSearch);
        btnAddNote = findViewById(R.id.btnAddNote);

        noteAdapter = new NoteAdapter(this, noteList);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        rvNotes.setAdapter(noteAdapter);

        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString().trim();
                if (keyword.isEmpty()) {
                    loadNotes();
                } else {
                    searchNotes(keyword);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String currentSearch = etSearch.getText().toString().trim();
        if (currentSearch.isEmpty()) {
            loadNotes();
        } else {
            searchNotes(currentSearch);
        }
    }

    private void loadNotes() {
        noteList = databaseHelper.getAllNotes();
        noteAdapter.setNotes(noteList);
        tvEmptyList.setText("Chưa có ghi chú nào. Hãy tạo mới!");
        checkEmptyList();
    }

    private void searchNotes(String keyword) {
        noteList = databaseHelper.searchNotes(keyword);
        noteAdapter.setNotes(noteList);
        tvEmptyList.setText("Không tìm thấy ghi chú nào phù hợp!");
        checkEmptyList();
    }

    private void checkEmptyList() {
        if (noteList == null || noteList.isEmpty()) {
            tvEmptyList.setVisibility(View.VISIBLE);
            rvNotes.setVisibility(View.GONE);
        } else {
            tvEmptyList.setVisibility(View.GONE);
            rvNotes.setVisibility(View.VISIBLE);
        }
    }
}