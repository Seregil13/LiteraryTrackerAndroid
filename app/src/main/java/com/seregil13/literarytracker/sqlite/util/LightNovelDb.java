package com.seregil13.literarytracker.sqlite.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.seregil13.literarytracker.sqlite.LiteraryTrackerContract;

import java.util.List;

/**
 * Helper class for dealing with the Light Novel table and the many-to-many table for genres
 *
 * @author seregil13
 */
public class LightNovelDb {

    /**
     * Inserts a new row into the Light Novel table
     * @param db The writable database to write the data to.
     * @param title The title of the Light Novel.
     * @param author The author of the Light Novel.
     * @param description The synopsis of the Light Novel.
     * @param completed Whether the Light Novel has been completed or not.
     * @param whereToRead Where to read the Light Novel
     * @param coverArtUrl A url for the image of the Light Novel
     * @return the id of the new Light Novel
     */
    public static long insert(SQLiteDatabase db, String title, String author, String description, boolean completed, String whereToRead, String coverArtUrl) {
        ContentValues values = new ContentValues();
        values.put(LiteraryTrackerContract.LightNovelEntry.COLUMN_TITLE, title);
        values.put(LiteraryTrackerContract.LightNovelEntry.COLUMN_AUTHOR, author);
        values.put(LiteraryTrackerContract.LightNovelEntry.COLUMN_DESCRIPTION, description);
        values.put(LiteraryTrackerContract.LightNovelEntry.COLUMN_COMPLETED, completed);
        values.put(LiteraryTrackerContract.LightNovelEntry.COLUMN_TRANSLATOR_SITE, whereToRead);
        values.put(LiteraryTrackerContract.LightNovelEntry.COLUMN_COVER_ART_URL, coverArtUrl);

        return db.insert(LiteraryTrackerContract.LightNovelEntry.TABLE_NAME, null, values);
    }

    /**
     * Inserts a new row into the Light Novel table
     * @param db The writable database to write the data to.
     * @param title The title of the Light Novel.
     * @param author The author of the Light Novel.
     * @param description The synopsis of the Light Novel.
     * @param completed Whether the Light Novel has been completed or not.
     * @param whereToRead Where to read the Light Novel
     * @param coverArtUrl A url for the image of the Light Novel
     * @param genres The genres for the Light Novel
     * @return the id of the new Light Novel
     */
    public static long insert(SQLiteDatabase db, String title, String author, String description,
                              boolean completed, String whereToRead, String coverArtUrl,
                              List<String> genres) {
        ContentValues values = new ContentValues();
        values.put(LiteraryTrackerContract.LightNovelEntry.COLUMN_TITLE, title);
        values.put(LiteraryTrackerContract.LightNovelEntry.COLUMN_AUTHOR, author);
        values.put(LiteraryTrackerContract.LightNovelEntry.COLUMN_DESCRIPTION, description);
        values.put(LiteraryTrackerContract.LightNovelEntry.COLUMN_COMPLETED, completed);
        values.put(LiteraryTrackerContract.LightNovelEntry.COLUMN_TRANSLATOR_SITE, whereToRead);
        values.put(LiteraryTrackerContract.LightNovelEntry.COLUMN_COVER_ART_URL, coverArtUrl);

        long id = db.insert(LiteraryTrackerContract.LightNovelEntry.TABLE_NAME, null, values);

        for (String genre : genres)
            insertLiteraryGenre(db, id, GenreDb.getIdFromName(db, genre));

        return id;
    }

    /**
     * Gets the information relating to the Light Novel with the specified id
     * @param db The readable database to get the data from.
     * @param id The id of the Light Novel to get the details for.
     * @return A cursor with the data.
     */
    public static Cursor readDetails(SQLiteDatabase db, long id) {
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

    /**
     * Insert a record in the many-to-many table for a representing a genre for the Light Novel
     * @param db The writable database to write to.
     * @param literaryId The id of the Light Novel
     * @param genreId The id for the genre
     * @return The id of the genre-lightnovel entry
     */
    public static long insertLiteraryGenre(SQLiteDatabase db, long literaryId, long genreId) {
        ContentValues values = new ContentValues();

        values.put(LiteraryTrackerContract.LightNovelGenreEntry.COLUMN_LIGHTNOVEL_ID, literaryId);
        values.put(LiteraryTrackerContract.LightNovelGenreEntry.COLUMN_GENRE_ID, genreId);

        return db.insert(LiteraryTrackerContract.LightNovelGenreEntry.TABLE_NAME, null, values);
    }

    /**
     * Updates the given Light Novel (id) with the new data.
     * @param db The writable database.
     * @param id The id of the Light Novel to update.
     * @param title The new title of the Light Novel.
     * @param author The new author of the Light Novel.
     * @param description The new synopsis of the Light Novel.
     * @param completed The new completion status of the Light Novel.
     * @param whereToRead The new place to read the Light Novel
     * @return The number of rows affected.
     */
    public static long updateDetails(SQLiteDatabase db, int id, String title, String author,
                                     String description, boolean completed, String whereToRead,
                                     String coverArtUrl) {
        ContentValues values = new ContentValues();

        values.put(LiteraryTrackerContract.LightNovelEntry.COLUMN_TITLE,title);
        values.put(LiteraryTrackerContract.LightNovelEntry.COLUMN_AUTHOR, author);
        values.put(LiteraryTrackerContract.LightNovelEntry.COLUMN_DESCRIPTION, description);
        values.put(LiteraryTrackerContract.LightNovelEntry.COLUMN_COMPLETED, completed);
        values.put(LiteraryTrackerContract.LightNovelEntry.COLUMN_TRANSLATOR_SITE, whereToRead);
        values.put(LiteraryTrackerContract.LightNovelEntry.COLUMN_COVER_ART_URL, coverArtUrl);

        String selection = LiteraryTrackerContract.LightNovelEntry._ID + " = ?";
        String[] selectionArgs = { (String.valueOf(id)) };

        return db.update(LiteraryTrackerContract.LightNovelEntry.TABLE_NAME, values, selection, selectionArgs);
    }
}
