package com.petya.teeth.app;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.petya.teeth.app.adapters.LowerToothViewAdapter;
import com.petya.teeth.app.adapters.ToothViewAdapter;
import com.petya.teeth.app.adapters.UpperToothViewAdapter;
import com.petya.teeth.app.db.DBHelper;
import com.petya.teeth.app.enums.ToothJawPosition;
import com.petya.teeth.app.enums.ToothState;
import com.petya.teeth.app.models.ToothModel;
import com.petya.teeth.app.models.ToothPositionModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AbstractAppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        RecyclerView recyclerViewUpper = findViewById(R.id.recyclerViewUpper);
        recyclerViewUpper.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        RecyclerView recyclerViewLower = findViewById(R.id.recyclerViewLower);
        recyclerViewLower.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<ToothModel> teethListUpper = new ArrayList<>();
        List<ToothModel> teethListLower = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + DBHelper.TABLE_NAME, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.UID));
            ToothJawPosition jaw = ToothJawPosition.getById(cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_JAW)));
            int number = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NUM));
            ToothPositionModel position = new ToothPositionModel(jaw, number);
            ToothState state = ToothState.getByKey(cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_STATE)));
            ToothModel tooth = new ToothModel(id, position, state);

            if (ToothJawPosition.UPPER.equals(jaw)) {
                teethListUpper.add(tooth);
            } else {
                teethListLower.add(tooth);
            }
        }

        ToothViewAdapter adapter = new UpperToothViewAdapter(this, teethListUpper);
        recyclerViewUpper.setAdapter(adapter);
        ToothViewAdapter adapterLower = new LowerToothViewAdapter(this, teethListLower);
        recyclerViewLower.setAdapter(adapterLower);
    }

    @Override
    protected void onDestroy() {
        database.close();
        dbHelper.close();
        super.onDestroy();
    }
}