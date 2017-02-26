package com.example.android.habittracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.habittracker.database.HabitContract;
import com.example.android.habittracker.database.HabitDBHelper;

public class MainActivity extends AppCompatActivity {

    private EditText mHabitNameEditText;
    private EditText mFrequencyEditText;

    private HabitDBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHabitNameEditText = (EditText)findViewById(R.id.habit_name);
        mFrequencyEditText = (EditText)findViewById(R.id.frequency_edit);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertHabit();
            }
        });

        mDBHelper = new HabitDBHelper(this);
        displayDatabase();
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabase();
    }

    private void displayDatabase(){
        mDBHelper = new HabitDBHelper(this);

        Cursor cursor = readDatabase();

        TextView display = (TextView)findViewById(R.id.text_view);

        try {
            display.setText("The habits table contain "+cursor.getCount()+"\n");
            display.append(HabitContract.HabitEntry._ID +" | " +
                    HabitContract.HabitEntry.COLUMN_HABIT_NAME + " | " +
                    HabitContract.HabitEntry.COLLUMN_HABIT_FREQUENCY + " | "
            );

            int idColumnIndex = cursor.getColumnIndex(HabitContract.HabitEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(HabitContract.HabitEntry.COLUMN_HABIT_NAME);
            int frequencyColumnIndex = cursor.getColumnIndex(HabitContract.HabitEntry.COLLUMN_HABIT_FREQUENCY);

            while (cursor.moveToNext()){
                int currentID = cursor.getInt(idColumnIndex);
                String currentHabitName = cursor.getString(nameColumnIndex);
                int currentFrequency = cursor.getInt(frequencyColumnIndex);

                display.append(("\n" + currentID + " | "
                + currentHabitName + " | "
                + currentFrequency));
            }
        }finally {
            cursor.close();
        }
    }

    private Cursor readDatabase(){
        Cursor temp;
        SQLiteDatabase db = mDBHelper.getReadableDatabase();

        String[] projection = {
                HabitContract.HabitEntry._ID,
                HabitContract.HabitEntry.COLUMN_HABIT_NAME,
                HabitContract.HabitEntry.COLLUMN_HABIT_FREQUENCY};
        temp = db.query(
                HabitContract.HabitEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);
        return temp;
    }

    private void insertHabit(){
        String nameText = mHabitNameEditText.getText().toString().trim();
        int frequencyText = Integer.parseInt(mFrequencyEditText.getText().toString().trim());

        mDBHelper = new HabitDBHelper(this);

        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HabitContract.HabitEntry.COLUMN_HABIT_NAME, nameText);
        values.put(HabitContract.HabitEntry.COLLUMN_HABIT_FREQUENCY, frequencyText);

        long newRowID = db.insert(HabitContract.HabitEntry.TABLE_NAME, null, values);

        if (newRowID == -1 ){
            Toast.makeText(this,"Error with Saving in Database",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,"Habit saved with row id: "+newRowID,Toast.LENGTH_SHORT).show();
        }
        displayDatabase();
    }

}
