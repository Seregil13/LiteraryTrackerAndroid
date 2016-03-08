package com.seregil13.literarytracker.lightnovel;


/**
 *
 * @author Alec
 * @since March 6, 2016
 */
public class LightNovelListContent {



    public static class LightNovel {
        public final int id;
        public final String title;
        public final String author;

        public LightNovel(int id, String title, String author) {
            this.id = id;
            this.title = title;
            this.author = author;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
