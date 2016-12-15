package com.example.franciscofranco.marvellogin.Home;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.franciscofranco.marvellogin.R;
import com.example.franciscofranco.marvellogin.SQLite.HeroDBHelper;

public class Storage extends AppCompatActivity {

    private ListView listView;
    HeroDBHelper dbHelper;
    SimpleCursorAdapter cursorAdapter;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        dbHelper = new HeroDBHelper(this);

        cursor = dbHelper.getAllHeroes();

        String [] columns = new String[] {
                HeroDBHelper.HERO_COLUMN_ID,
                HeroDBHelper.HERO_COLUMN_NAME
        };

        int [] widgets = new int[] {
                R.id.name,
                R.id.urlLink
        };

        cursorAdapter = new SimpleCursorAdapter(this, R.layout.storage_row,
                cursor, columns, widgets, 0);

        listView = (ListView)findViewById(R.id.storageList);
        listView.setAdapter(cursorAdapter);

    }
}
