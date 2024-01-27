package com.petya.teeth.app.db;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DataActivity extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase database;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DBHelper(this);
        database = dbHelper.getReadableDatabase();

    }

    @Override
    protected void onDestroy() {
        database.close();
        dbHelper.close();
        super.onDestroy();
    }
}
