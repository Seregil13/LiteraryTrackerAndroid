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
import com.seregil13.literarytracker.sqlite.util.GenreDb;
import com.seregil13.literarytracker.sqlite.util.LightNovelDb;

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

//        createTestData();

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
        long id = LightNovelDb.insert(db, "I Shall Seal The Heavens", "deathblade", "Desc", false, "wuxiaworld", "lkjfds");
        LightNovelDb.insertLiteraryGenre(db, id, 3);
        LightNovelDb.insertLiteraryGenre(db, id, 6);
        LightNovelDb.insertLiteraryGenre(db, id, 4);
        LightNovelDb.insertLiteraryGenre(db, id, 8);
        LightNovelDb.insertLiteraryGenre(db, id, 18);
        LightNovelDb.insertLiteraryGenre(db, id, 10);
        LightNovelDb.insertLiteraryGenre(db, id, 21);
        LightNovelDb.insertLiteraryGenre(db, id, 11);
        id = LightNovelDb.insert(db, "Desolate Era", "Blizzard", "Desc", false, "wuxiaworld", "lkjfds");
        LightNovelDb.insertLiteraryGenre(db, id, 5);
        id = LightNovelDb.insert(db, "Martial World", "Er gen", "Desc", false, "gravitytales", "lkjfds");
        LightNovelDb.insertLiteraryGenre(db, id, 9);
        id = LightNovelDb.insert(db, "True Martial World", "Er gen", "Desc", false, "gravitytales", "lkjfds");
        LightNovelDb.insertLiteraryGenre(db, id, 3);
        id = LightNovelDb.insert(db, "Ancient Godly Monarch", "Baby Blueman", "Desc", false, "gravitytales", "lkjfds");
        LightNovelDb.insertLiteraryGenre(db, id, 13);

        db.close();
    }

    public void createGenres() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        GenreDb.insert(db, "action");
        GenreDb.insert(db, "adventure");
        GenreDb.insert(db, "comedy");
        GenreDb.insert(db, "doujinshi");
        GenreDb.insert(db, "drama");
        GenreDb.insert(db, "ecchi");
        GenreDb.insert(db, "fantasy");
        GenreDb.insert(db, "gender bender");
        GenreDb.insert(db, "harem");
        GenreDb.insert(db, "historical");
        GenreDb.insert(db, "horror");
        GenreDb.insert(db, "josei");
        GenreDb.insert(db, "martial arts");
        GenreDb.insert(db, "mature");
        GenreDb.insert(db, "mecha");
        GenreDb.insert(db, "mystery");
        GenreDb.insert(db, "one shot");
        GenreDb.insert(db, "psycological");
        GenreDb.insert(db, "romance");
        GenreDb.insert(db, "school life");
        GenreDb.insert(db, "sci-fi");
        GenreDb.insert(db, "seinen");
        GenreDb.insert(db, "shoujo");
        GenreDb.insert(db, "shoujo ai");
        GenreDb.insert(db, "shounen");
        GenreDb.insert(db, "shounen ai");
        GenreDb.insert(db, "slice of life");
        GenreDb.insert(db, "sports");
        GenreDb.insert(db, "supernatural");
        GenreDb.insert(db, "tragedy");
        GenreDb.insert(db, "yaoi");
        GenreDb.insert(db, "yuri");

        db.close();
    }
}
