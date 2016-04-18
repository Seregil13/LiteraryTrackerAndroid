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
import com.seregil13.widgetlibrary.WrappedLinearLayout;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A fragment representing a single LightNovel detail screen.
 * This fragment is either contained in a {@link LightNovelListActivity}
 * in two-pane mode (on tablets) or a {@link LightNovelDetailActivity}
 * on handsets.
 */
public class LightNovelDetailFragment extends Fragment {

    public static final String TAG = "LNDetailFragment";

    /* Views */
    private TextView mAuthorTextView;
    private TextView mCompletedTextView;
    private TextView mDescriptionTextView;
    private TextView mTranslatorSiteTextView;
    private WrappedLinearLayout mGenresLayout;

    private OnDataFetched mActivity;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LightNovelDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(JsonKeys.ID.toString())) {

            int id = getArguments().getInt(JsonKeys.ID.toString());
            String title = getArguments().getString(JsonKeys.TITLE.toString(), "Light Novel");

            /* Sends a request for a json object via volley */
            requestDetails(id);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(title);
            }
        }
    }

    public void requestDetails(int id) {
        JsonObjectRequest json = new JsonObjectRequest(Request.Method.GET, ServerInfo.LIGHT_NOVEL.getDetailUrl(id), null, onSuccess, onError);
        VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(json);
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
                int mId = response.getInt(JsonKeys.ID.toString());
                String mTitle = response.getString(JsonKeys.TITLE.toString());
                String mAuthor = response.getString(JsonKeys.AUTHOR.toString());
                String mDescription = response.getString(JsonKeys.DESCRIPTION.toString());
                String mCompleted = response.getString(JsonKeys.COMPLETED.toString());
                String mTranslatorSite = response.getString(JsonKeys.TRANSLATOR_SITE.toString());
                ArrayList<String> mGenres = LiteraryTrackerUtils.jsonArrayToList(response.getJSONArray(JsonKeys.GENRES.toString()));

                mActivity.setData(mId, mTitle, mAuthor, mDescription, mCompleted, mTranslatorSite, mGenres);

                mAuthorTextView.setText(mAuthor);
                mCompletedTextView.setText(Boolean.parseBoolean(mCompleted) ? "Completed" : "In Progress");
                mDescriptionTextView.setText(mDescription);
                mTranslatorSiteTextView.setText(mTranslatorSite);

                mGenresLayout.removeAllViews(); // Removes the existing views
                mGenresLayout.addTextViews(mGenres); // Adds the new genres to the WrappedLinearLayout

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
