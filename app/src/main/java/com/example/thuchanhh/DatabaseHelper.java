package com.example.thuchanhh;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notes_db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NOTES = "notes";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_CREATED_AT = "created_at";
    private static final String COLUMN_UPDATED_AT = "updated_at";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NOTES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT NOT NULL, " +
                COLUMN_CONTENT + " TEXT, " +
                COLUMN_CREATED_AT + " INTEGER NOT NULL, " +
                COLUMN_UPDATED_AT + " INTEGER NOT NULL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    public long insertNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_CONTENT, note.getContent());
        values.put(COLUMN_CREATED_AT, note.getCreatedAt());
        values.put(COLUMN_UPDATED_AT, note.getUpdatedAt());

        long id = db.insert(TABLE_NOTES, null, values);
        db.close();
        return id;
    }

    public List<Note> getAllNotes() {
        List<Note> noteList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NOTES + " ORDER BY " + COLUMN_UPDATED_AT + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Note note = new Note();
                    note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                    note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                    note.setContent(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)));
                    note.setCreatedAt(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT)));
                    note.setUpdatedAt(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_UPDATED_AT)));
                    noteList.add(note);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return noteList;
    }

    public Note getNoteById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTES,
                new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_CONTENT, COLUMN_CREATED_AT, COLUMN_UPDATED_AT},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        Note note = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                note.setContent(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)));
                note.setCreatedAt(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT)));
                note.setUpdatedAt(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_UPDATED_AT)));
            }
            cursor.close();
        }
        db.close();
        return note;
    }

    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, note.getTitle());
        values.put(COLUMN_CONTENT, note.getContent());
        values.put(COLUMN_UPDATED_AT, note.getUpdatedAt());

        int rowsAffected = db.update(TABLE_NOTES, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
        return rowsAffected;
    }

    public int deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_NOTES, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected;
    }

    public List<Note> searchNotes(String keyword) {
        List<Note> noteList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NOTES +
                " WHERE " + COLUMN_TITLE + " LIKE ? OR " + COLUMN_CONTENT + " LIKE ? COLLATE NOCASE" +
                " ORDER BY " + COLUMN_UPDATED_AT + " DESC";

        String searchString = "%" + keyword + "%";
        Cursor cursor = db.rawQuery(query, new String[]{searchString, searchString});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Note note = new Note();
                    note.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                    note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                    note.setContent(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)));
                    note.setCreatedAt(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT)));
                    note.setUpdatedAt(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_UPDATED_AT)));
                    noteList.add(note);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return noteList;
    }
}