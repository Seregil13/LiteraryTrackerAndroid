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

package com.seregil13.literarytracker.lightnovel;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.seregil13.literarytracker.R;
import com.seregil13.literarytracker.network.ServerInfo;
import com.seregil13.literarytracker.network.VolleySingleton;
import com.seregil13.literarytracker.util.JsonKeys;
import com.seregil13.literarytracker.util.LiteraryTrackerUtils;
import com.seregil13.literarytracker.views.WrappedLinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a single LightNovel detail screen.
 * This fragment is either contained in a {@link LightNovelListActivity}
 * in two-pane mode (on tablets) or a {@link LightNovelDetailActivity}
 * on handsets.
 */
public class LightNovelDetailFragment extends Fragment {

    public static final String TAG = "LNDetailFragment";

    /* Fragment Arguments */
    public static final String ARG_LIGHTNOVEL_ID = "lightnovel_id";
    public static final String ARG_LIGHTNOVEL_TITLE = "lightnovel_title";
    public static final String ARG_LIGHTNOVEL_AUTHOR = "lightnovel_author";

    /* Defines the type of literature to be used in the network calls */
    private static final ServerInfo.LiteraryType TYPE = ServerInfo.LiteraryType.LIGHT_NOVEL;

    /* Views */
    private TextView mAuthorTextView;
    private TextView mCompletedTextView;
    private TextView mDescriptionTextView;
    private TextView mTranslatorSiteTextView;
    private WrappedLinearLayout mGenresLayout;

    /* Values */
    private int mId;
    private String mTitle;
    private String mAuthor;
    private String mDescription;
    private String mCompleted;
    private String mTranslatorSite;
    private List<String> mGenres;

    private OnDataFetched mActivity;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LightNovelDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_LIGHTNOVEL_ID)) {

            int id = getArguments().getInt(ARG_LIGHTNOVEL_ID);
            String title = getArguments().getString(ARG_LIGHTNOVEL_TITLE, "Light Novel");

            Log.d(TAG, title);

            /* Sends a request for a json object via volley */
            JsonObjectRequest json = new JsonObjectRequest(Request.Method.GET, ServerInfo.getDetailUrl(TYPE, String.valueOf(id)), null, onSuccess, onError);
            VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(json);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(title);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lightnovel_detail, container, false);

        mAuthorTextView = (TextView) view.findViewById(R.id.author);
        mCompletedTextView = (TextView) view.findViewById(R.id.completionStatus);
        mDescriptionTextView = (TextView) view.findViewById(R.id.description);
        mTranslatorSiteTextView = (TextView) view.findViewById(R.id.translatorSite);
        mGenresLayout = (WrappedLinearLayout) view.findViewById(R.id.genres);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (OnDataFetched) context;
    }

    /**
     * The callback function for a successful network query
     */
    Response.Listener<JSONObject> onSuccess = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {
                mId = response.getInt(JsonKeys.LightNovel.ID);
                mTitle = response.getString(JsonKeys.LightNovel.TITLE);
                mAuthor = response.getString(JsonKeys.LightNovel.AUTHOR);
                mDescription = response.getString(JsonKeys.LightNovel.DESCRIPTION);
                mCompleted = response.getString(JsonKeys.LightNovel.COMPLETED);
                mTranslatorSite = response.getString(JsonKeys.LightNovel.TRANSLATOR_SITE);
                mGenres = LiteraryTrackerUtils.jsonArrayToList(response.getJSONArray(JsonKeys.LightNovel.GENRES));

                mActivity.setData(mId, mTitle, mAuthor, mDescription, mCompleted, mTranslatorSite, mGenres);

                mAuthorTextView.setText(mAuthor);
                mCompletedTextView.setText(Boolean.parseBoolean(mCompleted) ? "Completed" : "In Progress");
                mDescriptionTextView.setText(mDescription);
                mTranslatorSiteTextView.setText(mTranslatorSite);

                for (String genre : mGenres) {

                    /* Creates a textview to hold the genre */
                    TextView genreTV = new TextView(getContext());
                    genreTV.setText(genre);
                    genreTV.setBackgroundResource(R.drawable.border);
                    genreTV.setTextColor(Color.WHITE);
                    genreTV.setPadding(15,10,15,10);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(10,10,10,10);
                    genreTV.setLayoutParams(params);

                    mGenresLayout.addView(genreTV);
                }

                ((CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout)).setTitle(mTitle);
            } catch (Exception e) {
                e.printStackTrace(); // TODO: Handle exceptions better
            }
        }
    };

    /**
     * The callback function for a failed network query
     */
    Response.ErrorListener onError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d(TAG, error.getMessage());
        }
    };
}
