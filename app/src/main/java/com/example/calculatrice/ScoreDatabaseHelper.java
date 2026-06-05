package com.example.calculatrice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ScoreDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MathGame.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_SCORES = "scores";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SCORE = "score";

    private static final String CREATE_TABLE_SCORES = 
            "CREATE TABLE " + TABLE_SCORES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME + " TEXT NOT NULL," +
                    COLUMN_SCORE + " INTEGER NOT NULL" +
                    ");";

    public ScoreDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SCORES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
        onCreate(db);
    }

    public long insertScore(String name, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_SCORE, score);
        return db.insert(TABLE_SCORES, null, values);
    }

    public List<Score> getTopScores(int limit) {
        List<Score> scores = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.query(
                TABLE_SCORES,
                new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_SCORE},
                null,
                null,
                null,
                null,
                COLUMN_SCORE + " DESC",
                String.valueOf(limit)
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Score score = new Score(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE))
                );
                scores.add(score);
            }
            cursor.close();
        }
        return scores;
    }

    public static class Score {
        private int id;
        private String name;
        private int score;

        public Score(int id, String name, int score) {
            this.id = id;
            this.name = name;
            this.score = score;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public int getScore() { return score; }
    }
}
