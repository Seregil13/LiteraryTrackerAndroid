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
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.seregil13.literarytracker.R;
import com.seregil13.literarytracker.util.JsonKeys;
import com.seregil13.literarytracker.util.LiteraryTrackerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a single LightNovel detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link LightNovelListActivity}.
 */
public class LightNovelDetailActivity extends AppCompatActivity implements OnDataFetched {

    private static final String TAG = "LNDetailActivity";

    /* Detailed info of the current Light Novel */
    LightNovelModel data;

    private LightNovelDetailFragment mDetailFragment;
    /**
     * Handles the onclick event for the floating action buttons
     */
    private View.OnClickListener mFabListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Context context = view.getContext();

            Intent intent = new Intent(context, LightNovelFormActivity.class);
            intent.putExtra(JsonKeys.ID.toString(), data.getId());
            intent.putExtra(JsonKeys.TITLE.toString(), data.getTitle());
            intent.putExtra(JsonKeys.AUTHOR.toString(), data.getAuthor());
            intent.putExtra(JsonKeys.DESCRIPTION.toString(), data.getDescription());
            intent.putExtra(JsonKeys.COMPLETED.toString(), data.getCompleted());
            intent.putExtra(JsonKeys.TRANSLATOR_SITE.toString(), data.getTranslatorSite());
            intent.putStringArrayListExtra(JsonKeys.GENRES.toString(), (ArrayList<String>) data.getGenres());
            intent.putExtra(LightNovelFormActivity.REQUEST_CODE_KEY, LiteraryTrackerUtils.EDIT_REQUEST_CODE);

            startActivityForResult(intent, LiteraryTrackerUtils.EDIT_REQUEST_CODE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lightnovel_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        /* TODO: make fab open edit light novel */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null)
            fab.setOnClickListener(mFabListener);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(JsonKeys.ID.toString(), getIntent().getIntExtra(JsonKeys.ID.toString(), 1));
            arguments.putString(JsonKeys.TITLE.toString(), getIntent().getStringExtra(JsonKeys.TITLE.toString()));
            mDetailFragment = new LightNovelDetailFragment();
            mDetailFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.lightnovel_detail_container, mDetailFragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, LightNovelListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == LiteraryTrackerUtils.EDIT_REQUEST_CODE) {
            if (resultCode == LiteraryTrackerUtils.EDIT_SUCCESS_CODE) {
                //TODO: been updated refresh data
            } else {
                //TODO: canceled do nothing
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void setData(int id, String title, String author, String description, int completed, String translatorSite, List<String> genres) {
        this.data = new LightNovelModel(id, title, author, description, completed, translatorSite, genres);
    }
}
