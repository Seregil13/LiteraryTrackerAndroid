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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.seregil13.literarytracker.R;
import com.seregil13.literarytracker.network.ServerInfo;
import com.seregil13.literarytracker.network.VolleySingleton;
import com.seregil13.literarytracker.sqlite.LiteraryTrackerContract;
import com.seregil13.literarytracker.sqlite.LiteraryTrackerDbHelper;
import com.seregil13.literarytracker.util.JsonKeys;
import com.seregil13.literarytracker.util.LiteraryTrackerUtils;
import com.seregil13.widgetlibrary.WrappedLinearLayout;

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

    private static final String TAG = "LNDetailFragment";

    private int id;

    /* Views */
    private TextView mAuthorTextView;
    private TextView mCompletedTextView;
    private TextView mDescriptionTextView;
    private TextView mTranslatorSiteTextView;
    private LinearLayout mGenresLayout;

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

            id = getArguments().getInt(JsonKeys.ID.toString());
            String title = getArguments().getString(JsonKeys.TITLE.toString(), "Light Novel");

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(title);
            }
        }
    }

    public void getDetails(long id) {
        LiteraryTrackerDbHelper dbHelper = new LiteraryTrackerDbHelper(getContext());

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = dbHelper.readLightNovelDetails(db, id);
        List<String> genres = new ArrayList<>();

        while(cursor.moveToNext()) {
            int mId = cursor.getInt(cursor.getColumnIndexOrThrow(LiteraryTrackerContract.LightNovelEntry._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(LiteraryTrackerContract.LightNovelEntry.COLUMN_TITLE));
            String mAuthor = cursor.getString(cursor.getColumnIndexOrThrow(LiteraryTrackerContract.LightNovelEntry.COLUMN_AUTHOR));
            String mDescription = cursor.getString(cursor.getColumnIndexOrThrow(LiteraryTrackerContract.LightNovelEntry.COLUMN_DESCRIPTION));
            int mCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(LiteraryTrackerContract.LightNovelEntry.COLUMN_COMPLETED));
            String mTranslatorSite = cursor.getString(cursor.getColumnIndexOrThrow(LiteraryTrackerContract.LightNovelEntry.COLUMN_TRANSLATOR_SITE));
            genres.add(cursor.getString(cursor.getColumnIndexOrThrow(LiteraryTrackerContract.GenresEntry.COLUMN_NAME)));

            mActivity.setData(mId, title, mAuthor, mDescription, mCompleted, mTranslatorSite, null);

            mAuthorTextView.setText(mAuthor);
//            mCompletedTextView.setText(mCompleted != 0 ? "Completed" : "In Progress");
            mDescriptionTextView.setText(mDescription);
            mTranslatorSiteTextView.setText(mTranslatorSite);

            ((CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout)).setTitle(title);
        }

        for (String g : genres) {
            TextView v = new TextView(mGenresLayout.getContext());
            v.setBackground(getResources().getDrawable(R.drawable.genres_background));
            v.setPadding(10, 5, 10, 5);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(5,15,5,15);

            v.setLayoutParams(params);
            v.setText(g);
            mGenresLayout.addView(v);
        }

        cursor.close();
        db.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lightnovel_detail, container, false);

        mAuthorTextView = (TextView) view.findViewById(R.id.author);
        mCompletedTextView = (TextView) view.findViewById(R.id.completionStatus);
        mDescriptionTextView = (TextView) view.findViewById(R.id.description);
        mTranslatorSiteTextView = (TextView) view.findViewById(R.id.translator_site);
        mGenresLayout = (LinearLayout) view.findViewById(R.id.genre_list);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDetails(id);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (OnDataFetched) context;
    }
}
