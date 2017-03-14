package com.seregil13.literarytracker;

import android.app.Activity;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.seregil13.literarytracker.genre.GenreAdapter;
import com.seregil13.literarytracker.genre.GenreContent;
import com.seregil13.literarytracker.lightnovel.LightNovelListActivity;
import com.seregil13.literarytracker.network.ServerInfo;
import com.seregil13.literarytracker.network.VolleySingleton;
import com.seregil13.literarytracker.sqlite.LiteraryTrackerContract;
import com.seregil13.literarytracker.sqlite.LiteraryTrackerDbHelper;
import com.seregil13.literarytracker.sqlite.util.GenreDb;
import com.seregil13.literarytracker.util.JsonKeys;
import com.seregil13.literarytracker.util.LiteraryTrackerUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class GenreSelectionActivity extends AppCompatActivity {

    private static final String TAG = "GSelectActivity";

    private GenreAdapter mAdapter;
    private RecyclerView recyclerView;

    private ArrayList<String> mGenres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_selection);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Enter a new genre");

                    final EditText input = new EditText(view.getContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    // Set up the buttons
                    builder.setPositiveButton(getText(android.R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LiteraryTrackerDbHelper dbHelper = new LiteraryTrackerDbHelper(GenreSelectionActivity.this);
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            GenreDb.insert(db, input.getText().toString().trim());
                            requestGenres();
                        }
                    });
                    builder.setNegativeButton(getText(android.R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();

                }
            });
        }

        if (savedInstanceState == null) {

            this.mGenres = new ArrayList<>();
            this.mGenres.addAll(getIntent().getStringArrayListExtra(JsonKeys.GENRES.toString()));

            mAdapter = new GenreAdapter(new ArrayList<GenreContent>());

            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(mAdapter);

            requestGenres();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                ArrayList<String> genres = new ArrayList<>();
                List<GenreContent> items = mAdapter.getItems();

                for (GenreContent content : items) {
                    if (content.isSelected)
                        genres.add(content.name);
                }

                Intent returnData = new Intent();
                returnData.putStringArrayListExtra(JsonKeys.GENRES.toString(), genres);

                setResult(LiteraryTrackerUtils.GENRE_SUCCESS_CODE, returnData);
                finish();
                return true;
            case android.R.id.home:
                setResult(LiteraryTrackerUtils.GENRE_CANCEL_CODE);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void requestGenres() {

        LiteraryTrackerDbHelper dbHelper = new LiteraryTrackerDbHelper(this);

        List<GenreContent> genres = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                LiteraryTrackerContract.GenresEntry._ID,
                LiteraryTrackerContract.GenresEntry.COLUMN_NAME
        };

        Cursor cursor = db.query(LiteraryTrackerContract.GenresEntry.TABLE_NAME, projection, null, null, null, null, null);

        while (cursor.moveToNext()) {
            GenreContent content = new GenreContent(
                    cursor.getInt(cursor.getColumnIndexOrThrow(LiteraryTrackerContract.GenresEntry._ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(LiteraryTrackerContract.GenresEntry.COLUMN_NAME)));

            if (mGenres.contains(content.name))
                content.isSelected = true;

            genres.add(content);
        }

        mAdapter.updateGenreList(genres);

        cursor.close();
        db.close();
    }
}
