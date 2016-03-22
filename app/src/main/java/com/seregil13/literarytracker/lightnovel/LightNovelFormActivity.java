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

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.seregil13.literarytracker.R;
import com.seregil13.literarytracker.util.JsonKeys;
import com.seregil13.literarytracker.util.LiteraryTrackerUtils;

import java.util.ArrayList;

public class LightNovelFormActivity extends AppCompatActivity {

    private static final String TAG = "LNFormActivity";
    private static final String REQUEST_CODE_KEY = "requestCode";

    /* The request code will determine which form to display. */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_novel_form);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {

            int requestCode = getIntent().getIntExtra(REQUEST_CODE_KEY, LiteraryTrackerUtils.CREATE_REQUEST_CODE);
            Fragment fragment;

            switch (requestCode) {
                case LiteraryTrackerUtils.CREATE_REQUEST_CODE:
                    fragment = new LightNovelCreateFragment();
                    break;
                case LiteraryTrackerUtils.EDIT_REQUEST_CODE:
                    int id = getIntent().getIntExtra(JsonKeys.LightNovel.ID, 1);
                    String title = getIntent().getStringExtra(JsonKeys.LightNovel.TITLE);
                    String author = getIntent().getStringExtra(JsonKeys.LightNovel.AUTHOR);
                    String description = getIntent().getStringExtra(JsonKeys.LightNovel.DESCRIPTION);
                    String completed = getIntent().getStringExtra(JsonKeys.LightNovel.COMPLETED);
                    String tsite = getIntent().getStringExtra(JsonKeys.LightNovel.TRANSLATOR_SITE);
                    ArrayList<String> genres =  getIntent().getStringArrayListExtra(JsonKeys.LightNovel.GENRES);
                    fragment = LightNovelEditFragment.newInstance(id, title, author, description, completed, tsite, genres);
                    break;
                default:
                    fragment = new LightNovelCreateFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        intent.putExtra(REQUEST_CODE_KEY, requestCode);
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
