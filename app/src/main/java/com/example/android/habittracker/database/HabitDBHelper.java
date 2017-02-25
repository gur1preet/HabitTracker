package com.example.android.habittracker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.habittracker.database.HabitContract.HabitEntry;

public class HabitDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tracker.db";
    private static final int DATABASE_VERSION = 1;

    public HabitDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_HABITS_TABLE = "CREATE TABLE " + HabitEntry.TABLE_NAME + " ("
                + HabitEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + HabitEntry.COLUMN_HABIT_NAME + " TEXT NOT NULL, "
                + HabitEntry.COLLUMN_HABIT_FREQUENCY + " INTEGER NOT NULL DEFAULT 0);";

        db.execSQL(SQL_CREATE_HABITS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
