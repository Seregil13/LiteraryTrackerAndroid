package com.seregil13.literarytracker;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.seregil13.literarytracker.network.ServerInfo;
import com.seregil13.literarytracker.network.VolleySingleton;
import com.seregil13.literarytracker.sqlite.LiteraryTrackerContract;
import com.seregil13.literarytracker.sqlite.LiteraryTrackerDbHelper;
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
            mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice);//R.layout.genres_list_item);

            mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            mListView.setAdapter(mAdapter);

            saveButton.setOnClickListener(mSaveListener);
            cancelButton.setOnClickListener(mCancelListener);

            requestGenres();
        }
    }

    protected void requestGenres() {

        LiteraryTrackerDbHelper dbHelper = new LiteraryTrackerDbHelper(this);

        List<String> genres = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                LiteraryTrackerContract.GenresEntry.COLUMN_NAME
        };

        Cursor cursor = db.query(LiteraryTrackerContract.GenresEntry.TABLE_NAME, projection, null, null, null, null, null);

        while (cursor.moveToNext()) {
            genres.add(cursor.getString(cursor.getColumnIndexOrThrow(LiteraryTrackerContract.GenresEntry.COLUMN_NAME)));
        }

        mAdapter.clear();
        mAdapter.addAll(genres);

        for (String s: mGenres) {
            int idx = mAdapter.getPosition(s);
            if (idx >= 0) {
                getListView().setItemChecked(idx, true);
            }
        }

        cursor.close();
        db.close();
    }

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
