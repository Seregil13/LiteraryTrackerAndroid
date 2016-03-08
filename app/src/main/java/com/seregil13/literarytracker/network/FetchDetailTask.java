package com.seregil13.literarytracker.network;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.seregil13.literarytracker.stream.StreamHandler;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Alec
 * @since March 6, 2016
 */
public class FetchDetailTask extends AsyncTask<String, Void, String> {

    private TaskListener mTaskListener;
    private ServerInfo.LiteraryType mType;

    public FetchDetailTask(Context context, ServerInfo.LiteraryType type) {
        this.mTaskListener = (TaskListener) context;
        this.mType = type;
    }


    @Override
    protected String doInBackground(String... params) {
        String result = "";

        try {
            URL url = ServerInfo.getDetailUrl(mType, params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            try {
                InputStream in = new BufferedInputStream(connection.getInputStream());
                result = StreamHandler.readStream(in);
            } finally {
                connection.disconnect();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        this.mTaskListener.onTaskCompleted(s);
    }
}
