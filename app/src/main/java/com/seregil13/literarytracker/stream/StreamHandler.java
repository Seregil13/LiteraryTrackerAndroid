package com.seregil13.literarytracker.stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 *
 * @author Alec
 * @since March 6, 2016
 */
public class StreamHandler {


    /**
     * @param inputStream The stream to read from.
     * @return The string representation of the input stream.
     * @throws IOException If some I/O problem occurs
     */
    public static String readStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder res = new StringBuilder();

        String line = "";
        while((line = reader.readLine()) != null) {
            res.append(line);
        }

        return res.toString();
    }
}
