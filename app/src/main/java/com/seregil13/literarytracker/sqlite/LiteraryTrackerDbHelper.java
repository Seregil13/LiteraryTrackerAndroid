package com.seregil13.literarytracker.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LiteraryTrackerDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "literarytracker.db";

    public LiteraryTrackerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LiteraryTrackerContract.LightNovelEntry.CREATE_TABLE);
        db.execSQL(LiteraryTrackerContract.BookEntry.CREATE_TABLE);
        db.execSQL(LiteraryTrackerContract.MangaEntry.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
