package com.petya.teeth.app;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.petya.teeth.app.db.DBHelper;
import com.petya.teeth.app.enums.ToothState;
import com.petya.teeth.app.models.ToothModel;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ToothDetailsActivity extends AbstractAppCompatActivity {
    DBHelper dbHelper;
    SQLiteDatabase database;
    TextView textView;
    Spinner spinner;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tooth_details);

        textView = findViewById(R.id.toothId);
        spinner = findViewById(R.id.toothStatuses);
        saveButton = findViewById(R.id.saveStatusButton);

        Intent intent = getIntent();
        if (intent != null) {
            ToothModel clickedItemData = (ToothModel) intent.getSerializableExtra("clickedItemData");
            textView.setText(clickedItemData.getToothPosition().getJaw() + " " + clickedItemData.getToothPosition().getNumber());

            ArrayAdapter<ToothState> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_item,
                    ToothState.values()
            );

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            int position = Arrays.stream(ToothState.values())
                    .filter(value -> value == clickedItemData.getToothState())
                    .findFirst()
                    .map(value -> Arrays.asList(ToothState.values()).indexOf(value))
                    .orElse(-1);

            spinner.setAdapter(adapter);
            spinner.setSelection(position, true);

            dbHelper = new DBHelper(this);
            database = dbHelper.getWritableDatabase();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToothState selectedEnum = (ToothState) spinner.getSelectedItem();

                    ContentValues values = new ContentValues();
                    values.put(DBHelper.COLUMN_STATE, selectedEnum.getStringResId());
                    String selection = "_id = ?";
                    String[] selectionArgs = {String.valueOf(clickedItemData.getUid())};
                    database.update(DBHelper.TABLE_NAME, values, selection, selectionArgs);

                    Toast.makeText(ToothDetailsActivity.this, "Status Updated: " + selectedEnum, Toast.LENGTH_SHORT).show();

                }
            });
        }
    }
}