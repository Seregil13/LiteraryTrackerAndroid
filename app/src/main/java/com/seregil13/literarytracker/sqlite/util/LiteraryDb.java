package com.seregil13.literarytracker.sqlite.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author seregil13
 */
public interface LiteraryDb {

    long insert(SQLiteDatabase db, String title, String author, String description, boolean completed, String whereToRead, String coverArtUrl);
    Cursor readDetails(SQLiteDatabase db, long id);
    long insertLiteraryGenre(SQLiteDatabase db, long literaryId, long genreId);
}
