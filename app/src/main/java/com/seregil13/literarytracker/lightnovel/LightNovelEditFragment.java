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
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.seregil13.literarytracker.util.JsonKeys;
import com.seregil13.literarytracker.util.LiteraryTrackerUtils;
import com.seregil13.widgetlibrary.WrappedLinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class LightNovelEditFragment extends Fragment {

    private static final String TAG = "LNEditFragment";

    private int mId;
    private String mTitle;
    private String mAuthor;
    private String mDescription;
    private String mCompleted;
    private String mTranslatorSite;
    private ArrayList<String> mGenres;

    private Mode mCreateOrEdit;

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

        switch (this.mCreateOrEdit) {
            case CREATE:
                this.mTitle = "";
                this.mAuthor = "";
                this.mDescription = "";
                this.mCompleted = "false";
                this.mTranslatorSite = "";
                this.mGenres = new ArrayList<>();
                break;
            case EDIT:
                this.mId = arguments.getInt(JsonKeys.ID.toString());
                this.mTitle = arguments.getString(JsonKeys.TITLE.toString());
                this.mAuthor = arguments.getString(JsonKeys.AUTHOR.toString());
                this.mDescription = arguments.getString(JsonKeys.DESCRIPTION.toString());
                this.mCompleted = arguments.getString(JsonKeys.COMPLETED.toString());
                this.mTranslatorSite = arguments.getString(JsonKeys.TRANSLATOR_SITE.toString());
                this.mGenres = arguments.getStringArrayList(JsonKeys.GENRES.toString());
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_light_novel_edit, container, false);

        this.mTitleET = (EditText) view.findViewById(R.id.title);
        this.mAuthorET = (EditText) view.findViewById(R.id.author);
        this.mDescriptionET = (EditText) view.findViewById(R.id.description);
        this.mTranslatorSiteET = (EditText) view.findViewById(R.id.translatorSite);
        this.mCompletedCB = (CheckBox) view.findViewById(R.id.completionStatus);
        Button editGenres = (Button) view.findViewById(R.id.genreSelection);
        Button cancel = (Button) view.findViewById(R.id.cancelButton);
        Button save = (Button) view.findViewById(R.id.saveButton);


        cancel.setOnClickListener(this.mCancelListener);
        save.setOnClickListener(this.mSaveListener);
        editGenres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), GenreSelectionActivity.class);
                intent.putStringArrayListExtra(JsonKeys.GENRES.toString(), mGenres);

                startActivityForResult(intent, LiteraryTrackerUtils.GENRE_REQUEST_CODE);
            }
        });

        mTitleET.setText(mTitle);
        mAuthorET.setText(mAuthor);
        mDescriptionET.setText(mDescription);
        mTranslatorSiteET.setText(mTranslatorSite);
        mCompletedCB.setChecked(Boolean.parseBoolean(mCompleted));

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LiteraryTrackerUtils.GENRE_REQUEST_CODE) {
            Log.d(TAG, "Received result from Activity with code " + requestCode);

            if (resultCode == LiteraryTrackerUtils.GENRE_SUCCESS_CODE) {
                /* Updates the stored genres to be the selection returned from the Genre Selection Activity */
                this.mGenres = data.getStringArrayListExtra(JsonKeys.GENRES.toString());
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    View.OnClickListener mSaveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            JSONObject postData = new JSONObject();
            try {
                postData.put(JsonKeys.TITLE.toString(), mTitleET.getText());
                postData.put(JsonKeys.AUTHOR.toString(), mAuthorET.getText());
                postData.put(JsonKeys.COMPLETED.toString(), mCompletedCB.isChecked());
                postData.put(JsonKeys.DESCRIPTION.toString(), mDescriptionET.getText());
                postData.put(JsonKeys.TRANSLATOR_SITE.toString(), mTranslatorSiteET.getText());
                postData.put(JsonKeys.GENRES.toString(), TextUtils.join(",", mGenres));

                // TODO: validation

                JsonObjectRequest json;

                switch (mCreateOrEdit) {
                    case EDIT:
                        json = new JsonObjectRequest(Request.Method.POST, ServerInfo.LIGHT_NOVEL.getUpdateUrl(mId), postData, onEditSuccess, onError);
                        break;
                    case CREATE:
                        json = new JsonObjectRequest(Request.Method.POST, ServerInfo.LIGHT_NOVEL.getCreateUrl(), postData, onCreateSuccess, onError);
                        break;
                    default:
                        json = new JsonObjectRequest(Request.Method.POST, ServerInfo.LIGHT_NOVEL.getCreateUrl(), postData, onCreateSuccess, onError); // Defaults to create rather than
                }

                VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(json);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    View.OnClickListener mCancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().setResult(LiteraryTrackerUtils.EDIT_CANCEL_CODE);
            getActivity().finish();
        }
    };

    /**
     * The callback for a successful network query.
     */
    Response.Listener<JSONObject> onEditSuccess = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            if (response != null)
                Log.d(TAG, response.toString());
            Intent returnData = new Intent();
            returnData.putExtra(JsonKeys.ID.toString(), mId);

            getActivity().setResult(LiteraryTrackerUtils.EDIT_SUCCESS_CODE, returnData);
            getActivity().finish(); // Finishes the activity and goes back to the detail screen
        }
    };

    /**
     * The callback for a successful network query to create a Light Novel.
     */
    Response.Listener<JSONObject> onCreateSuccess = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            if (response != null)
                Log.d(TAG, response.toString());
            Intent returnData = new Intent();
            returnData.putExtra(JsonKeys.ID.toString(), mId);

            getActivity().setResult(LiteraryTrackerUtils.CREATE_SUCCESS_CODE, returnData);
            getActivity().finish(); // Finishes the activity and goes back to the list screen
        }
    };

    /**
     * The callback function for a failed network query
     */
    Response.ErrorListener onError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d(TAG, error.getMessage());

            if (getView() != null) {
                Snackbar.make(getView(), error.getMessage(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        }
    };
}
