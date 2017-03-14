package com.seregil13.literarytracker.sqlite.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.seregil13.literarytracker.sqlite.LiteraryTrackerContract;

/**
 * @author seregil13
 */
public class GenreDb {

    public static long insert(SQLiteDatabase db, String name) {
        ContentValues values = new ContentValues();
        values.put(LiteraryTrackerContract.GenresEntry.COLUMN_NAME, name);

        return db.insert(LiteraryTrackerContract.GenresEntry.TABLE_NAME, null, values);
    }

    public static Cursor read(SQLiteDatabase db, long id) {

        String[] columns = { LiteraryTrackerContract.GenresEntry.COLUMN_NAME };
        String where = LiteraryTrackerContract.GenresEntry._ID + " = ?";
        String[] whereArgs = { String.valueOf(id) };

        return db.query(LiteraryTrackerContract.GenresEntry.TABLE_NAME, columns, where, whereArgs, null, null, null);
    }

    public static Cursor read(SQLiteDatabase db, String name) {
        String[] columns = { LiteraryTrackerContract.GenresEntry.COLUMN_NAME };
        String where = LiteraryTrackerContract.GenresEntry.COLUMN_NAME + " = ?";
        String[] whereArgs = { String.valueOf(name) };

        return db.query(LiteraryTrackerContract.GenresEntry.TABLE_NAME, columns, where, whereArgs, null, null, null);
    }

    /**
     * Returns the id of the Genre specified by name.
     *
     * @param db The readable database to search in.
     * @param name The name of the genre to get the id for.
     * @return The id of the genre specified or -1 if the genre name doesn't exist.
     */
    public static long getIdFromName(SQLiteDatabase db, String name) {
        String[] columns = { LiteraryTrackerContract.GenresEntry._ID };
        String where = LiteraryTrackerContract.GenresEntry.COLUMN_NAME + " = ?";
        String[] whereArgs = { String.valueOf(name) };

        Cursor cursor = db.query(LiteraryTrackerContract.GenresEntry.TABLE_NAME, columns, where, whereArgs, null, null, null);

        long id = -1;
        while (cursor.moveToNext())
            id = cursor.getInt(cursor.getColumnIndexOrThrow(LiteraryTrackerContract.GenresEntry._ID));

        cursor.close();
        return id;
    }
}
