package com.seregil13.literarytracker.network;

import android.app.Activity;
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
public class FetchListTask extends AsyncTask<Void, Void, String> {

    private ServerInfo.LiteraryType mType;
    private TaskListener mTaskListener;

    public FetchListTask(Activity activity, ServerInfo.LiteraryType type) {
        this.mTaskListener = (TaskListener) activity;
        this.mType = type;
    }

    @Override
    protected String doInBackground(Void... params) {
        String result = "";

        try {
            URL url = ServerInfo.getListUrl(mType);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            try {
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                result = StreamHandler.readStream(inputStream);
            } finally {
                connection.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        mTaskListener.onTaskCompleted(result);
    }
}
