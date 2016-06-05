package com.seregil13.literarytracker.sqlite;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author Alec
 */
public class LiteraryTrackerProvider extends ContentProvider {

    private static final String LOG_TAG = LiteraryTrackerProvider.class.getSimpleName();

    static final int GENRE = 100;

    static final int LIGHT_NOVEL = 200;
    static final int LIGHT_NOVEL_GENRE = 201;

    static final int BOOK = 300;
    static final int BOOK_GENRE = 301;

    static final int MANGA = 400;
    static final int MANGA_GENRE = 401;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = LiteraryTrackerContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, LiteraryTrackerContract.PATH_LIGHT_NOVEL, LIGHT_NOVEL);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        return null;
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
