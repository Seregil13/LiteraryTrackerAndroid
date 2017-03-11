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

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.seregil13.literarytracker.R;
import com.seregil13.literarytracker.network.ServerInfo;
import com.seregil13.literarytracker.network.VolleySingleton;
import com.seregil13.literarytracker.sqlite.LiteraryTrackerContract;
import com.seregil13.literarytracker.sqlite.LiteraryTrackerDbHelper;
import com.seregil13.literarytracker.util.JsonKeys;
import com.seregil13.literarytracker.util.LiteraryTrackerUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of LightNovels. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link LightNovelDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class LightNovelListActivity extends AppCompatActivity {

    private static final String TAG = "LN_LISTACTIVITY";

    private LiteraryTrackerDbHelper mDbHelper;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lightnovel_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setTitle(getTitle());
        }

//        sendRequest();

        mDbHelper = new LiteraryTrackerDbHelper(this);
        test();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(fabListener);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lightnovel_list);
        if (recyclerView != null) {
            setupRecyclerView(recyclerView);
        }


        if (findViewById(R.id.lightnovel_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    SimpleItemRecyclerViewAdapter adapter = new SimpleItemRecyclerViewAdapter(new ArrayList<ListContent>());

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LiteraryTrackerUtils.CREATE_REQUEST_CODE) {
            test();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void test() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                LiteraryTrackerContract.LightNovelEntry._ID,
                LiteraryTrackerContract.LightNovelEntry.COLUMN_TITLE,
                LiteraryTrackerContract.LightNovelEntry.COLUMN_AUTHOR
        };

        Cursor cursor = db.query(
                LiteraryTrackerContract.LightNovelEntry.TABLE_NAME,
                projection, null, null, null, null, null);

        List<ListContent> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(LiteraryTrackerContract.LightNovelEntry._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(LiteraryTrackerContract.LightNovelEntry.COLUMN_TITLE));
            String author = cursor.getString(cursor.getColumnIndexOrThrow(LiteraryTrackerContract.LightNovelEntry.COLUMN_AUTHOR));

            list.add(new ListContent(id, title, author));
        }

        cursor.close();
        db.close();

        adapter.updateNovelList(list);
    }

    /**
     *
     */
    private View.OnClickListener fabListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Context context = LightNovelListActivity.this;

            Intent intent = new Intent(context, LightNovelFormActivity.class);

            startActivityForResult(intent, LiteraryTrackerUtils.CREATE_REQUEST_CODE);
        }
    };

}
