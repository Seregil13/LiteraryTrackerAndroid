package com.seregil13.literarytracker;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.seregil13.literarytracker.lightnovel.LightNovelCreateFragment;
import com.seregil13.literarytracker.lightnovel.LightNovelEditFragment;
import com.seregil13.literarytracker.network.ServerInfo;
import com.seregil13.literarytracker.network.VolleySingleton;
import com.seregil13.literarytracker.util.JsonKeys;
import com.seregil13.literarytracker.util.LiteraryTrackerUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class GenreSelectionActivity extends ListActivity {

    private static final String TAG = "GSelectActivity";

    private ArrayAdapter<String> mAdapter;
    private ListView mListView;

    private ArrayList<String> mGenres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_selection);

        if (savedInstanceState == null) {

            this.mGenres = new ArrayList<>();
            this.mGenres.addAll(getIntent().getStringArrayListExtra(JsonKeys.GENRES.toString()));

            mListView = getListView();
            Button saveButton = (Button) findViewById(R.id.saveButton);
            Button cancelButton = (Button) findViewById(R.id.cancelButton);

            mListView.setTextFilterEnabled(true);
            mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice);//R.layout.genres_list_item);
            mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            mListView.setAdapter(mAdapter);

            saveButton.setOnClickListener(mSaveListener);
            cancelButton.setOnClickListener(mCancelListener);

            requestGenres();
        }
    }

    protected void requestGenres() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, ServerInfo.GENRE.getListUrl(), null, mOnSuccess, mOnError);
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }



    Response.Listener<JSONArray> mOnSuccess = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            try {
                List<String> genres = LiteraryTrackerUtils.jsonArrayToList(response);
                mAdapter.clear();
                mAdapter.addAll(genres);

                for (String s: mGenres) {
                    int idx = mAdapter.getPosition(s);
                    if (idx >= 0) {
                        getListView().setItemChecked(idx, true);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * The callback function for a failed network query
     */
    Response.ErrorListener mOnError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d(TAG, "error happened");
        }
    };

    private View.OnClickListener mSaveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            ArrayList<String> genres = new ArrayList<>();

            SparseBooleanArray arr = mListView.getCheckedItemPositions();
            for (int i = 0; i < arr.size(); ++i) {
                int idx = arr.keyAt(i);
                if (arr.get(idx)) {
                    genres.add((String) mListView.getItemAtPosition(idx));
                }
            }

            Log.d(TAG, genres.toString());

            Intent returnData = new Intent();
            returnData.putStringArrayListExtra(JsonKeys.GENRES.toString(), genres);

            setResult(LiteraryTrackerUtils.GENRE_SUCCESS_CODE, returnData);
            finish();
        }
    };

    private View.OnClickListener mCancelListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            setResult(LiteraryTrackerUtils.GENRE_CANCEL_CODE);
            finish();
        }
    };
}
