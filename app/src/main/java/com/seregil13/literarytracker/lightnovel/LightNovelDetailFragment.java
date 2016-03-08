package com.seregil13.literarytracker.lightnovel;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seregil13.literarytracker.R;
import com.seregil13.literarytracker.lightnovel.dummy.DummyContent;
import com.seregil13.literarytracker.network.FetchDetailTask;
import com.seregil13.literarytracker.network.ServerInfo;
import com.seregil13.literarytracker.network.TaskListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A fragment representing a single LightNovel detail screen.
 * This fragment is either contained in a {@link LightNovelListActivity}
 * in two-pane mode (on tablets) or a {@link LightNovelDetailActivity}
 * on handsets.
 */
public class LightNovelDetailFragment extends Fragment implements TaskListener {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_LIGHTNOVEL_ID = "lightnovel_id";
    public static final String ARG_LIGHTNOVEL_TITLE = "lightnovel_title";
    public static final String ARG_LIGHTNOVEL_AUTHOR = "lightnovel_author";

    private static final String JSON_TITLE = "title";
    private static final String JSON_AUTHOR = "author";
    private static final String JSON_DESCRIPTION = "description";
    private static final String JSON_COMPLETED = "completed";
    private static final String JSON_TRANSLATOR_SITE = "translatorSite";
    private static final String JSON_GENRES = "genres";

    private TextView mTitleTextView;
    private TextView mAuthorTextView;
    private TextView mCompletedTextView;
    private TextView mDescriptionTextView;
    private TextView mTranslatorSiteTextView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LightNovelDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_LIGHTNOVEL_ID)) {

            new FetchDetailTask(this.getContext(), ServerInfo.LiteraryType.LIGHT_NOVEL).execute(String.valueOf(getArguments().getInt(ARG_LIGHTNOVEL_ID)));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(getArguments().getString(ARG_LIGHTNOVEL_TITLE, "Light Novel"));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lightnovel_detail, container, false);

        mTitleTextView = (TextView) view.findViewById(R.id.title);
        mAuthorTextView = (TextView) view.findViewById(R.id.author);
        mCompletedTextView = (TextView) view.findViewById(R.id.completionStatus);
        mDescriptionTextView = (TextView) view.findViewById(R.id.description);
        mTranslatorSiteTextView = (TextView) view.findViewById(R.id.translatorSite);

        return view;
    }

    @Override
    public void onTaskCompleted(String result) {

        try {
            JSONObject novel = new JSONObject(result);

            mTitleTextView.setText(novel.getString(JSON_TITLE));
            mAuthorTextView.setText(novel.getString(JSON_AUTHOR));
            mCompletedTextView.setText(novel.getString(JSON_COMPLETED));
            mDescriptionTextView.setText(novel.getString(JSON_DESCRIPTION));
            mTranslatorSiteTextView.setText(novel.getString(JSON_TRANSLATOR_SITE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
