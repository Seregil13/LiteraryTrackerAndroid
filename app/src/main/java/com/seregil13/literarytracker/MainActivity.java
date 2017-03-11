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

package com.seregil13.literarytracker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.seregil13.literarytracker.lightnovel.LightNovelListActivity;
import com.seregil13.literarytracker.sqlite.LiteraryTrackerContract;
import com.seregil13.literarytracker.sqlite.LiteraryTrackerDbHelper;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    LiteraryTrackerDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new LiteraryTrackerDbHelper(this);

        createTestData();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(MainActivity.this, LightNovelListActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createTestData() {

        dbHelper.onUpgrade(dbHelper.getReadableDatabase(), 0, 0);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        createGenres();
        createLightNovels();

        db.close();
    }

    public void createLightNovels() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = dbHelper.insertLightNovel(db, "I Shall Seal The Heavens", "deathblade", "Desc", false, "wuxiaworld", "lkjfds");
        dbHelper.insertLightNovelGenre(db, id, 3);
        dbHelper.insertLightNovelGenre(db, id, 6);
        dbHelper.insertLightNovelGenre(db, id, 4);
        dbHelper.insertLightNovelGenre(db, id, 8);
        dbHelper.insertLightNovelGenre(db, id, 18);
        dbHelper.insertLightNovelGenre(db, id, 10);
        dbHelper.insertLightNovelGenre(db, id, 21);
        dbHelper.insertLightNovelGenre(db, id, 11);
        id = dbHelper.insertLightNovel(db, "Desolate Era", "Blizzard", "Desc", false, "wuxiaworld", "lkjfds");
        dbHelper.insertLightNovelGenre(db, id, 5);
        id = dbHelper.insertLightNovel(db, "Martial World", "Er gen", "Desc", false, "gravitytales", "lkjfds");
        dbHelper.insertLightNovelGenre(db, id, 9);
        id = dbHelper.insertLightNovel(db, "True Martial World", "Er gen", "Desc", false, "gravitytales", "lkjfds");
        dbHelper.insertLightNovelGenre(db, id, 3);
        id = dbHelper.insertLightNovel(db, "Ancient Godly Monarch", "Baby Blueman", "Desc", false, "gravitytales", "lkjfds");
        dbHelper.insertLightNovelGenre(db, id, 13);

        db.close();
    }

    public void createGenres() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.insertGenre(db, "action");
        dbHelper.insertGenre(db, "adventure");
        dbHelper.insertGenre(db, "comedy");
        dbHelper.insertGenre(db, "doujinshi");
        dbHelper.insertGenre(db, "drama");
        dbHelper.insertGenre(db, "ecchi");
        dbHelper.insertGenre(db, "fantasy");
        dbHelper.insertGenre(db, "gender bender");
        dbHelper.insertGenre(db, "harem");
        dbHelper.insertGenre(db, "historical");
        dbHelper.insertGenre(db, "horror");
        dbHelper.insertGenre(db, "josei");
        dbHelper.insertGenre(db, "martial arts");
        dbHelper.insertGenre(db, "mature");
        dbHelper.insertGenre(db, "mecha");
        dbHelper.insertGenre(db, "mystery");
        dbHelper.insertGenre(db, "one shot");
        dbHelper.insertGenre(db, "psycological");
        dbHelper.insertGenre(db, "romance");
        dbHelper.insertGenre(db, "school life");
        dbHelper.insertGenre(db, "sci-fi");
        dbHelper.insertGenre(db, "seinen");
        dbHelper.insertGenre(db, "shoujo");
        dbHelper.insertGenre(db, "shoujo ai");
        dbHelper.insertGenre(db, "shounen");
        dbHelper.insertGenre(db, "shounen ai");
        dbHelper.insertGenre(db, "slice of life");
        dbHelper.insertGenre(db, "sports");
        dbHelper.insertGenre(db, "supernatural");
        dbHelper.insertGenre(db, "tragedy");
        dbHelper.insertGenre(db, "yaoi");
        dbHelper.insertGenre(db, "yuri");

        db.close();
    }
}
