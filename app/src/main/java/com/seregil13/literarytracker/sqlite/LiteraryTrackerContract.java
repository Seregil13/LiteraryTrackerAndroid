package com.seregil13.literarytracker.sqlite;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines the schema for the LiteraryTracker database;
 */
public class LiteraryTrackerContract {

    public static final String CONTENT_AUTHORITY = "com.seregil13.literarytracker";
    public static final String SCHEME = "content://";
    public static final String SLASH = "/";

    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + CONTENT_AUTHORITY);

    public static final String PATH_LIGHT_NOVEL = "lightnovel";
    public static final String PATH_BOOK = "book";
    public static final String PATH_MANGA = "manga";
    public static final String PATH_GENRES = "genres";

    /* SQLite keywords */
    public static final String INTEGER = " INTEGER ";
    public static final String TEXT = " TEXT ";
    public static final String NOT_NULL = " NOT NULL ";
    public static final String PRIMARY_KEY = " PRIMARY KEY ";
    public static final String AUTOINCREMENT = " AUTOINCREMENT ";
    public static final String COMMA = ",";

    public static final class LightNovelEntry implements BaseColumns {

        public static final Uri CONTENT_PATH = BASE_CONTENT_URI.buildUpon().appendPath(PATH_LIGHT_NOVEL).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + SLASH + CONTENT_AUTHORITY + SLASH + PATH_LIGHT_NOVEL;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + SLASH + CONTENT_AUTHORITY + SLASH + PATH_LIGHT_NOVEL;

        public static final String TABLE_NAME = "lightnovels";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_COMPLETED = "completed";
        public static final String COLUMN_TRANSLATOR_SITE = "translator_site";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + "(" +
                _ID + INTEGER + PRIMARY_KEY + AUTOINCREMENT + COMMA +//+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TITLE + TEXT + NOT_NULL + COMMA + //" TEXT NOT NULL," +
                COLUMN_AUTHOR + TEXT + NOT_NULL + COMMA + //" TEXT NOT NULL," +
                COLUMN_DESCRIPTION + TEXT + NOT_NULL + COMMA + //" TEXT NOT NULL," +
                COLUMN_COMPLETED + INTEGER + NOT_NULL + COMMA + //" INTEGER NOT NULL" +
                COLUMN_TRANSLATOR_SITE + TEXT + NOT_NULL + ");";//" TEXT NOT NULL" + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String[] COLUMNS = {
                _ID,
                COLUMN_TITLE,
                COLUMN_AUTHOR,
                COLUMN_DESCRIPTION,
                COLUMN_COMPLETED,
                COLUMN_TRANSLATOR_SITE
        };

        public static Uri buildLightNovelDetailUri(int id) {
            return CONTENT_PATH.buildUpon().appendPath(Integer.toString(id)).build();
        }
    }

    public static final class BookEntry implements BaseColumns {

        public static final Uri CONTENT_PATH = BASE_CONTENT_URI.buildUpon().appendPath(PATH_BOOK).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOK;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOK;

        public static final String TABLE_NAME = "books";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_LINK = "link";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + "(" +
                _ID + INTEGER + PRIMARY_KEY + AUTOINCREMENT + COMMA +//" INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TITLE + TEXT + NOT_NULL + COMMA + //" TEXT NOT NULL," +
                COLUMN_AUTHOR + TEXT + NOT_NULL + COMMA + //" TEXT NOT NULL," +
                COLUMN_DESCRIPTION + TEXT + NOT_NULL + COMMA + //" TEXT NOT NULL," +
                COLUMN_LINK + TEXT + NOT_NULL + ");";//" TEXT NOT NULL" + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String[] COLUMNS = {
                _ID,
                COLUMN_TITLE,
                COLUMN_AUTHOR,
                COLUMN_DESCRIPTION,
                COLUMN_LINK
        };
    }

    public static final class MangaEntry implements BaseColumns {

        public static final Uri CONTENT_PATH = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MANGA).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MANGA;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MANGA;

        public static final String TABLE_NAME = "manga";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_COMPLETED = "completed";
        public static final String COLUMN_TRANSLATOR_SITE = "translator_site";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + "(" +
                _ID + INTEGER + PRIMARY_KEY + AUTOINCREMENT + COMMA +//+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TITLE + TEXT + NOT_NULL + COMMA + //" TEXT NOT NULL," +
                COLUMN_AUTHOR + TEXT + NOT_NULL + COMMA + //" TEXT NOT NULL," +
                COLUMN_DESCRIPTION + TEXT + NOT_NULL + COMMA + //" TEXT NOT NULL," +
                COLUMN_COMPLETED + INTEGER + NOT_NULL + COMMA + //" INTEGER NOT NULL" +
                COLUMN_TRANSLATOR_SITE + TEXT + NOT_NULL + ");";//" TEXT NOT NULL" + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String[] COLUMNS = {
                _ID,
                COLUMN_TITLE,
                COLUMN_AUTHOR,
                COLUMN_DESCRIPTION,
                COLUMN_COMPLETED,
                COLUMN_TRANSLATOR_SITE
        };
    }

    public static final class GenresEntry implements BaseColumns {

        public static final Uri CONTENT_PATH = BASE_CONTENT_URI.buildUpon().appendPath(PATH_GENRES).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GENRES;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GENRES;

        public static final String TABLE_NAME = "genres";

        public static final String COLUMN_NAME = "genre_name";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + "(" +
                _ID + INTEGER + PRIMARY_KEY + AUTOINCREMENT + COMMA +
                COLUMN_NAME + TEXT + NOT_NULL + ");";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String[] COLUMNS = {
                _ID,
                COLUMN_NAME
        };

    }
}
