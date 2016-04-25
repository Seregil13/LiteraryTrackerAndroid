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

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * A utility class to handle common operations.
 *
 * @author Alec
 * @since March 12, 2016
 */
public class LiteraryTrackerUtils {

    /* random integer for request code */
    // TODO: Find a better spot for these
    public static final int EDIT_REQUEST_CODE = 1238;
    public static final int EDIT_SUCCESS_CODE = 1239;
    public static final int EDIT_CANCEL_CODE = 1240;

    public static final int CREATE_REQUEST_CODE = 1241;
    public static final int CREATE_SUCCESS_CODE = 1242;
    public static final int CREATE_CANCEL_CODE = 1243;

    public static final int GENRE_REQUEST_CODE = 1244;
    public static final int GENRE_SUCCESS_CODE = 1245;
    public static final int GENRE_CANCEL_CODE = 1246;

    /**
     * Converts a {@link JSONArray} to an {@link ArrayList} of strings
     * @param jsonArray A JSON array that contains strings
     * @return A list of the strings contained in the JSON array
     */
    public static ArrayList<String> jsonArrayToList(JSONArray jsonArray) throws Exception {

        if (jsonArray.length() == 0) return new ArrayList<>();

        if (!(jsonArray.get(0) instanceof String)) throw new Exception("Must pass in a json array with only string values");

        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            result.add(jsonArray.getString(i));
        }

        return result;
    }
}
