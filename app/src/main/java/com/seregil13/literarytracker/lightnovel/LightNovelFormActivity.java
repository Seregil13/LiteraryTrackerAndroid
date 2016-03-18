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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.seregil13.literarytracker.R;
import com.seregil13.literarytracker.util.JsonKeys;

public class LightNovelFormActivity extends AppCompatActivity {

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String AUTHOR = "author";
    public static final String DESCRIPTION = "description";
    public static final String COMPLETED = "completed";
    public static final String TRANSLATOR_SITE = "translatorSite";
    public static final String GENRES = "genres";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_novel_form);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putInt(ID, getIntent().getIntExtra(JsonKeys.LightNovel.ID, 1));
            arguments.putString(JsonKeys.LightNovel.TITLE, getIntent().getStringExtra(JsonKeys.LightNovel.TITLE));
            arguments.putString(JsonKeys.LightNovel.AUTHOR, getIntent().getStringExtra(JsonKeys.LightNovel.AUTHOR));
            arguments.putString(JsonKeys.LightNovel.DESCRIPTION, getIntent().getStringExtra(JsonKeys.LightNovel.DESCRIPTION));
            arguments.putString(JsonKeys.LightNovel.COMPLETED, getIntent().getStringExtra(JsonKeys.LightNovel.COMPLETED));
            arguments.putString(JsonKeys.LightNovel.TRANSLATOR_SITE, getIntent().getStringExtra(JsonKeys.LightNovel.TRANSLATOR_SITE));
            arguments.putStringArrayList(JsonKeys.LightNovel.GENRES, getIntent().getStringArrayListExtra(JsonKeys.LightNovel.GENRES));

            LightNovelEditFragment fragment = new LightNovelEditFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
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

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
