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

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.seregil13.literarytracker.GenreSelectionActivity;
import com.seregil13.literarytracker.R;
import com.seregil13.literarytracker.network.ServerInfo;
import com.seregil13.literarytracker.network.VolleySingleton;
import com.seregil13.literarytracker.sqlite.LiteraryTrackerContract;
import com.seregil13.literarytracker.sqlite.LiteraryTrackerDbHelper;
import com.seregil13.literarytracker.sqlite.util.GenreDb;
import com.seregil13.literarytracker.sqlite.util.LightNovelDb;
import com.seregil13.literarytracker.util.JsonKeys;
import com.seregil13.literarytracker.util.LiteraryTrackerUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class LightNovelEditFragment extends Fragment implements LightNovelFormActivity.SaveData {

    private static final String TAG = "LNEditFragment";

    private LightNovelModel data;

    private Mode mCreateOrEdit;

    private LiteraryTrackerDbHelper mDbHelper;

    /* Views */
    private EditText mTitleET;
    private EditText mAuthorET;
    private EditText mDescriptionET;
    private CheckBox mCompletedCB;
    private EditText mTranslatorSiteET;

    private static final String CREATE_OR_EDIT_KEY = "create_or_edit";

    public enum Mode {
        CREATE,
        EDIT
    }

    /**
     * Required empty public constructor
     */
    public LightNovelEditFragment() {}

    /**
     * Creates a new instance of the fragment in edit mode with all the pertinent data passed in as
     * parameters.
     *
     * @return An instance of LightNovelEditFragment.
     */
    public static LightNovelEditFragment newEditInstance(int id, String title, String author, String description, String completed, String translatorSite, ArrayList<String> genres) {
        LightNovelEditFragment fragment = new LightNovelEditFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(JsonKeys.ID.toString(), id);
        arguments.putString(JsonKeys.TITLE.toString(), title);
        arguments.putString(JsonKeys.AUTHOR.toString(), author);
        arguments.putString(JsonKeys.DESCRIPTION.toString(), description);
        arguments.putString(JsonKeys.COMPLETED.toString(), completed);
        arguments.putString(JsonKeys.TRANSLATOR_SITE.toString(), translatorSite);
        arguments.putStringArrayList(JsonKeys.GENRES.toString(), genres);
        arguments.putSerializable(CREATE_OR_EDIT_KEY, Mode.EDIT);

        fragment.setArguments(arguments);
        return fragment;
    }

    /**
     * Creates a new instance of the fragment in create mode with all the pertinent data passed in as
     * parameters.
     *
     * @return An instance of LightNovelEditFragment.
     */
    public static LightNovelEditFragment newCreateInstance() {
        LightNovelEditFragment fragment = new LightNovelEditFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(CREATE_OR_EDIT_KEY, Mode.CREATE);

        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        this.mCreateOrEdit = (Mode) arguments.getSerializable(CREATE_OR_EDIT_KEY);
        if (this.mCreateOrEdit == null) {
            this.mCreateOrEdit = Mode.CREATE;
        }

        this.mDbHelper = new LiteraryTrackerDbHelper(getContext());

        switch (this.mCreateOrEdit) {
            case CREATE:
                this.data = new LightNovelModel(-1, "", "", "", 0, "", new ArrayList<String>());
                break;
            case EDIT:
                this.data = new LightNovelModel(
                        arguments.getInt(JsonKeys.ID.toString()),
                        arguments.getString(JsonKeys.TITLE.toString()),
                        arguments.getString(JsonKeys.AUTHOR.toString()),
                        arguments.getString(JsonKeys.DESCRIPTION.toString()),
                        arguments.getInt(JsonKeys.COMPLETED.toString()),
                        arguments.getString(JsonKeys.TRANSLATOR_SITE.toString()),
                        arguments.getStringArrayList(JsonKeys.GENRES.toString())
                );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_light_novel_edit, container, false);

        this.mTitleET = (EditText) view.findViewById(R.id.title);
        this.mAuthorET = (EditText) view.findViewById(R.id.author_label);
        this.mDescriptionET = (EditText) view.findViewById(R.id.description);
        this.mTranslatorSiteET = (EditText) view.findViewById(R.id.translatorSite);
        this.mCompletedCB = (CheckBox) view.findViewById(R.id.completionStatus);
        Button editGenres = (Button) view.findViewById(R.id.genreSelection);

        editGenres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), GenreSelectionActivity.class);
                intent.putStringArrayListExtra(JsonKeys.GENRES.toString(), (ArrayList<String>) data.getGenres());

                startActivityForResult(intent, LiteraryTrackerUtils.GENRE_REQUEST_CODE);
            }
        });

        mTitleET.setText(this.data.getTitle());
        mAuthorET.setText(this.data.getAuthor());
        mDescriptionET.setText(this.data.getDescription());
        mTranslatorSiteET.setText(this.data.getTranslatorSite());
        mCompletedCB.setChecked(Boolean.parseBoolean(this.data.getCompleted()));

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == LiteraryTrackerUtils.GENRE_REQUEST_CODE) {
            Log.d(TAG, "Received result from Activity with code " + requestCode);

            if (resultCode == LiteraryTrackerUtils.GENRE_SUCCESS_CODE) {
                /* Updates the stored genres to be the selection returned from the Genre Selection Activity */
                this.data.setGenres(intent.getStringArrayListExtra(JsonKeys.GENRES.toString()));
            }
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save, menu);
    }
    
    @Override
    public Intent returnData() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        switch (mCreateOrEdit) {
            case EDIT:
                LightNovelDb.updateDetails(db, data.getId(), mTitleET.getText().toString(),
                        mAuthorET.getText().toString(), mDescriptionET.getText().toString(),
                        mCompletedCB.isChecked(), mTranslatorSiteET.getText().toString(), ""); // TODO

                for (String s : data.getGenres())
                    LightNovelDb.insertLiteraryGenre(db, data.getId(), GenreDb.getIdFromName(db, s));

                break;
            case CREATE:
                LightNovelDb.insert(db, mTitleET.getText().toString(),
                        mAuthorET.getText().toString(), mDescriptionET.getText().toString(),
                        mCompletedCB.isChecked(), mTranslatorSiteET.getText().toString(), "");

                for (String s : data.getGenres())
                    LightNovelDb.insertLiteraryGenre(db, data.getId(), GenreDb.getIdFromName(db, s));
                break;
            default:
                LightNovelDb.insert(db, mTitleET.getText().toString(),
                        mAuthorET.getText().toString(), mDescriptionET.getText().toString(),
                        mCompletedCB.isChecked(), mTranslatorSiteET.getText().toString(), "");
                break;
        }

        Intent returnData = new Intent();
        returnData.putExtra(JsonKeys.ID.toString(), data.getId());

        return returnData;
    }
}
