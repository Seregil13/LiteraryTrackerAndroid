package com.seregil13.literarytracker.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import java.util.List;

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

    public long insertGenre(SQLiteDatabase db, String genreName) {
        ContentValues values = new ContentValues();
        values.put(LiteraryTrackerContract.GenresEntry.COLUMN_NAME, genreName);

        return db.insert(LiteraryTrackerContract.GenresEntry.TABLE_NAME, null, values);
    }

    public long insertLightNovel(SQLiteDatabase db, String title, String author, String description, boolean completed, String translatorSite, String coverArtUrl) {
        ContentValues values = new ContentValues();
        values.put(LiteraryTrackerContract.LightNovelEntry.COLUMN_TITLE, title);
        values.put(LiteraryTrackerContract.LightNovelEntry.COLUMN_AUTHOR, author);
        values.put(LiteraryTrackerContract.LightNovelEntry.COLUMN_DESCRIPTION, description);
        values.put(LiteraryTrackerContract.LightNovelEntry.COLUMN_COMPLETED, completed);
        values.put(LiteraryTrackerContract.LightNovelEntry.COLUMN_TRANSLATOR_SITE, translatorSite);
        values.put(LiteraryTrackerContract.LightNovelEntry.COLUMN_COVER_ART_URL, coverArtUrl);

        return db.insert(LiteraryTrackerContract.LightNovelEntry.TABLE_NAME, null, values);
    }

    public long insertLightNovelGenre(SQLiteDatabase db, long lightNovelId, long genreId) {

        ContentValues values = new ContentValues();

        values.put(LiteraryTrackerContract.LightNovelGenreEntry.COLUMN_LIGHTNOVEL_ID, lightNovelId);
        values.put(LiteraryTrackerContract.LightNovelGenreEntry.COLUMN_GENRE_ID, genreId);

        return db.insert(LiteraryTrackerContract.LightNovelGenreEntry.TABLE_NAME, null, values);
    }

    public Cursor readLightNovelDetails(SQLiteDatabase db, long id) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        String join = LiteraryTrackerContract.LightNovelEntry.TABLE_NAME +
                " LEFT JOIN " + LiteraryTrackerContract.LightNovelGenreEntry.TABLE_NAME +
                " ON " + LiteraryTrackerContract.LightNovelEntry.FULL_ID + " = " + LiteraryTrackerContract.LightNovelGenreEntry.COLUMN_LIGHTNOVEL_ID +
                " LEFT JOIN " + LiteraryTrackerContract.GenresEntry.TABLE_NAME +
                " ON " + LiteraryTrackerContract.LightNovelGenreEntry.COLUMN_GENRE_ID + " = " + LiteraryTrackerContract.GenresEntry.FULL_ID;

        String where = LiteraryTrackerContract.LightNovelEntry.FULL_ID + " = ?";
        String[] whereArgs = {
                String.valueOf(id)
        };

        builder.setTables(join);
        return builder.query(db, null, where, whereArgs, null, null, null);
    }




}
