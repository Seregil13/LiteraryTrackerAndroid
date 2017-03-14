package com.seregil13.literarytracker.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import com.seregil13.literarytracker.sqlite.util.GenreDb;
import com.seregil13.literarytracker.sqlite.util.LightNovelDb;
import com.seregil13.literarytracker.sqlite.util.LiteraryDb;

public class LiteraryTrackerDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "literarytracker.db";

    public LiteraryTrackerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LiteraryTrackerContract.GenresEntry.CREATE_TABLE);
        db.execSQL(LiteraryTrackerContract.LightNovelEntry.CREATE_TABLE);
//        db.execSQL(LiteraryTrackerContract.BookEntry.CREATE_TABLE);
//        db.execSQL(LiteraryTrackerContract.MangaEntry.CREATE_TABLE);
        db.execSQL(LiteraryTrackerContract.LightNovelGenreEntry.CREATE_TABLE);
//        db.execSQL(LiteraryTrackerContract.BooksGenresEntry.CREATE_TABLE);
//        db.execSQL(LiteraryTrackerContract.MangaGenresEntry.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: don't obliterate the database when upgrading ...
        db.execSQL(LiteraryTrackerContract.LightNovelEntry.DELETE_TABLE);
        db.execSQL(LiteraryTrackerContract.GenresEntry.DELETE_TABLE);
        db.execSQL(LiteraryTrackerContract.LightNovelGenreEntry.DELETE_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
