package com.seregil13.literarytracker.sqlite;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author Alec
 */
public class LiteraryTrackerProvider extends ContentProvider {

    private static final String LOG_TAG = LiteraryTrackerProvider.class.getSimpleName();

    private static final int GENRE = 100;

    private static final int LIGHT_NOVEL = 200;
    private static final int LIGHT_NOVEL_GENRE = 201;
    private static final int LIGHT_NOVEL_DETAIL = 202;

//    private static final int BOOK = 300;
//    private static final int BOOK_GENRE = 301;
//    private static final int BOOK_DETAIL = 302;
//
//    private static final int MANGA = 400;
//    private static final int MANGA_GENRE = 401;
//    private static final int MANGA_DETAIL = 402;

    private static final UriMatcher uriMatcher = buildUriMatcher();
    private LiteraryTrackerDbHelper dbHelper;

    private Context context;

    private static final SQLiteQueryBuilder lightNovelQueryBuilder;

    static {
        lightNovelQueryBuilder = new SQLiteQueryBuilder();
        lightNovelQueryBuilder.setTables(
                LiteraryTrackerContract.LightNovelEntry.TABLE_NAME + " INNER JOIN " +
                        LiteraryTrackerContract.LightNovelGenreEntry.TABLE_NAME + " ON " +
                        LiteraryTrackerContract.LightNovelEntry.TABLE_NAME + "." + LiteraryTrackerContract.LightNovelEntry._ID + " = " +
                        LiteraryTrackerContract.LightNovelGenreEntry.TABLE_NAME + "." + LiteraryTrackerContract.LightNovelGenreEntry.COLUMN_LIGHTNOVEL_ID
        );
    }

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = LiteraryTrackerContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, LiteraryTrackerContract.PATH_GENRES, GENRE);

        matcher.addURI(authority, LiteraryTrackerContract.PATH_LIGHT_NOVEL, LIGHT_NOVEL);
        matcher.addURI(authority, LiteraryTrackerContract.PATH_LIGHT_NOVEL + "/#", LIGHT_NOVEL_DETAIL);

//        matcher.addURI(authority, LiteraryTrackerContract.PATH_BOOK, BOOK);
//
//        matcher.addURI(authority, LiteraryTrackerContract.PATH_MANGA, MANGA);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        this.dbHelper = new LiteraryTrackerDbHelper(getContext());
        this.context = getContext();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor;

        // TODO: Rest of it
        switch (uriMatcher.match(uri)) {
            case LIGHT_NOVEL:
                cursor = dbHelper.getReadableDatabase().query(LiteraryTrackerContract.LightNovelEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case LIGHT_NOVEL_DETAIL:
                cursor = lightNovelQueryBuilder.query(dbHelper.getReadableDatabase(), projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver != null)
            cursor.setNotificationUri(contentResolver, uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case LIGHT_NOVEL:
                return LiteraryTrackerContract.LightNovelEntry.CONTENT_TYPE;
            case LIGHT_NOVEL_DETAIL:
                return LiteraryTrackerContract.LightNovelEntry.CONTENT_ITEM_TYPE;
//            case BOOK:
//                return LiteraryTrackerContract.BookEntry.CONTENT_TYPE;
//            case BOOK_DETAIL:
//                return LiteraryTrackerContract.BookEntry.CONTENT_ITEM_TYPE;
//            case MANGA:
//                return LiteraryTrackerContract.MangaEntry.CONTENT_TYPE;
//            case MANGA_DETAIL:
//                return LiteraryTrackerContract.MangaEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        Uri returnUri = null;
        switch (uriMatcher.match(uri)) {
            case LIGHT_NOVEL: {
                long _id = db.insert(LiteraryTrackerContract.LightNovelEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = LiteraryTrackerContract.LightNovelEntry.buildLightNovelDetailUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case LIGHT_NOVEL_GENRE: {
                long _id = db.insert(LiteraryTrackerContract.LightNovelGenreEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = LiteraryTrackerContract.LightNovelGenreEntry.buildLightNovelDetailUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

        }

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
