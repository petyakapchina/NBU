package com.petya.teeth.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.petya.teeth.app.enums.ToothJawPosition;
import com.petya.teeth.app.enums.ToothState;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "teeth.db";
    private static final int DATABASE_VERSION = 1;
    public final static String TABLE_NAME = "tooth";
    public final static String UID = "_ID";
    public final static String COLUMN_JAW = "jaw";
    public final static String COLUMN_NUM = "number";
    public final static String COLUMN_STATE = "state";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE "
                + TABLE_NAME + " ("
                + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_JAW + " INTEGER NOT NULL, "
                + COLUMN_NUM + " INTEGER NOT NULL, "
                + COLUMN_STATE + " TEXT NOT NULL)";

        db.execSQL(SQL_CREATE_ITEMS_TABLE);

        insertData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    private void insertData(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        for (int tooth_index = 1; tooth_index <= 32; tooth_index++) {
            ToothJawPosition jaw = tooth_index <= 16 ? ToothJawPosition.UPPER : ToothJawPosition.LOWER;
            values.put(COLUMN_JAW, jaw.getId());
            values.put(COLUMN_NUM, tooth_index);
            values.put(COLUMN_STATE, ToothState.HEALTHY.getStringResId());
            db.insert(TABLE_NAME, null, values);
            values.clear();
        }
    }
}
