/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Alec Rietman
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.seregil13.literarytracker.util;

/**
 * Holds the keys used by the json response from the server
 *
 * @author Alec
 * @since March 13, 2016
 */
public enum JsonKeys {
    ID("id"),
    TITLE("title"),
    AUTHOR("author"),
    DESCRIPTION("description"),
    COMPLETED("completed"),
    TRANSLATOR_SITE("translatorSite"),
    GENRES("genres");

    String name;

    JsonKeys(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

//    public enum Columns {
//        ID("id"),
//        TITLE("title"),
//        AUTHOR("author"),
//        DESCRIPTION("description"),
//        COMPLETED("completed"),
//        TRANSLATOR_SITE("translatorSite"),
//        GENRES("genres");
//
//        String name;
//
//        Columns(String name) {
//            this.name = name;
//        }
//
////        LIGHT_NOVEL(Columns.ID, Columns.TITLE, Columns.AUTHOR, Columns.DESCRIPTION, Columns.COMPLETED, Columns.TRANSLATOR_SITE, Columns.GENRES),
////        MANGA(Columns.ID, Columns.TITLE, Columns.AUTHOR, Columns.DESCRIPTION, Columns.COMPLETED, Columns.TRANSLATOR_SITE, Columns.GENRES),
////        BOOK(Columns.ID, Columns.TITLE, Columns.AUTHOR, Columns.DESCRIPTION, Columns.COMPLETED, Columns.GENRES);
////
////        ArrayList<Columns> keys;
////
////        JsonKeys(Columns... columns) {
////            keys = new ArrayList<>();
////            Collections.addAll(keys, columns);
////        }
//    }


//    public class LightNovel {
//        public static final String ID = "id";
//        public static final String TITLE = "title";
//        public static final String AUTHOR = "author";
//        public static final String DESCRIPTION = "description";
//        public static final String COMPLETED = "completed";
//        public static final String TRANSLATOR_SITE = "translatorSite";
//        public static final String GENRES = "genres";
//    }
//
//    public class Manga {
//        public static final String ID = "id";
//        public static final String TITLE = "title";
//        public static final String AUTHOR = "author";
//        public static final String DESCRIPTION = "description";
//        public static final String COMPLETED = "completed";
//        public static final String TRANSLATOR_SITE = "translatorSite";
//        public static final String GENRES = "genres";
//    }
//
//    public class Book {
//        public static final String ID = "id";
//        public static final String TITLE = "title";
//        public static final String AUTHOR = "author";
//        public static final String DESCRIPTION = "description";
//        public static final String COMPLETED = "completed";
//        public static final String GENRES = "genres";
//    }
}
