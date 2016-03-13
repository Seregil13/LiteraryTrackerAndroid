package com.seregil13.literarytracker.util;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * A utility class to handle common operations.
 *
 * @author Alec
 * @since March 12, 2016
 */
public class LiteraryTrackerUtils {


    /**
     * Converts a {@link JSONArray} to an {@link ArrayList} of strings
     * @param jsonArray A JSON array that contains strings
     * @return A list of the strings contained in the JSON array
     */
    public static ArrayList<String> jsonArrayToList(JSONArray jsonArray) throws Exception {

        if (!(jsonArray.get(0) instanceof String)) throw new Exception("Must pass in a json array with only string values");

        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            result.add(jsonArray.getString(i));
        }

        return result;
    }

}
