package com.seregil13.literarytracker.genre;

/**
 * @author seregil13
 */
public class GenreContent {

    public final int id;
    public final String name;
    public boolean isSelected = false;

    public GenreContent(int id, String name) {
        this.id = id;
        this.name = name;
    }

}
