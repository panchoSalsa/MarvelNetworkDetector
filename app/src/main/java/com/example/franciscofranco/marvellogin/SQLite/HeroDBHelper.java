package com.example.franciscofranco.marvellogin.SQLite;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HeroDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Hero.db";
    private static final int DATABASE_VERSION = 1;
    public static final String HERO_TABLE_NAME = "Heroes";
    public static final String HERO_COLUMN_ID = "_id";
    public static final String HERO_COLUMN_NAME = "name";
    public static final String HERO_COLUMN_IMG = "img";

    public HeroDBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + HERO_TABLE_NAME + "(" +
                        HERO_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        HERO_COLUMN_NAME + " TEXT, " +
                        HERO_COLUMN_IMG + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + HERO_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertHero(String name, String img) {
        Log.d("FRANCO_DEBUG", "inside insertHero");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HERO_COLUMN_NAME, name);
        contentValues.put(HERO_COLUMN_IMG, img);
        db.insert(HERO_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getAllHeroes() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + HERO_TABLE_NAME, null );
        return res;
    }
}
