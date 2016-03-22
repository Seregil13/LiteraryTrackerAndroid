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
import com.seregil13.literarytracker.R;
import com.seregil13.literarytracker.network.ServerInfo;
import com.seregil13.literarytracker.network.VolleySingleton;
import com.seregil13.literarytracker.util.JsonKeys;
import com.seregil13.literarytracker.util.LiteraryTrackerUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    private List<String> mGenres; //// TODO: 3/21/2016 add in genres

    /* Views */
    private EditText mTitleET;
    private EditText mAuthorET;
    private EditText mDescriptionET;
    private CheckBox mCompletedCB;
    private EditText mTranslatorSiteET;
    private Button mCancelBtn;
    private Button mSaveBtn;

    /* TODO: input genres */

    /**
     * Requiired empty public constructor
     */
    public LightNovelEditFragment() {}

    public static LightNovelEditFragment newInstance(int id, String title, String author, String description, String completed, String translatorSite, ArrayList<String> genres) {
        LightNovelEditFragment fragment = new LightNovelEditFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(JsonKeys.LightNovel.ID, id);
        arguments.putString(JsonKeys.LightNovel.TITLE, title);
        arguments.putString(JsonKeys.LightNovel.AUTHOR, author);
        arguments.putString(JsonKeys.LightNovel.DESCRIPTION, description);
        arguments.putString(JsonKeys.LightNovel.COMPLETED, completed);
        arguments.putString(JsonKeys.LightNovel.TRANSLATOR_SITE, translatorSite);
        arguments.putStringArrayList(JsonKeys.LightNovel.GENRES, genres);

        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        this.mId = arguments.getInt(JsonKeys.LightNovel.ID);
        this.mTitle = arguments.getString(JsonKeys.LightNovel.TITLE);
        this.mAuthor = arguments.getString(JsonKeys.LightNovel.AUTHOR);
        this.mDescription = arguments.getString(JsonKeys.LightNovel.DESCRIPTION);
        this.mCompleted = arguments.getString(JsonKeys.LightNovel.COMPLETED);
        this.mTranslatorSite = arguments.getString(JsonKeys.LightNovel.TRANSLATOR_SITE);

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
        this.mCancelBtn = (Button) view.findViewById(R.id.cancelButton);
        this.mSaveBtn = (Button) view.findViewById(R.id.saveButton);

        this.mCancelBtn.setOnClickListener(this.mCancelListener);
        this.mSaveBtn.setOnClickListener(this.mSaveListener);

        Log.d(TAG, mTitle);
        Log.d(TAG, mAuthor);
        Log.d(TAG, mDescription);

        mTitleET.setText(mTitle);
        mAuthorET.setText(mAuthor);
        mDescriptionET.setText(mDescription);
        mTranslatorSiteET.setText(mTranslatorSite);
        mCompletedCB.setChecked(Boolean.parseBoolean(mCompleted));

        return view;
    }


    View.OnClickListener mSaveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            JSONObject postData = new JSONObject();
            try {
                postData.put(JsonKeys.LightNovel.TITLE, mTitleET.getText());
                postData.put(JsonKeys.LightNovel.AUTHOR, mAuthorET.getText());
                postData.put(JsonKeys.LightNovel.COMPLETED, mCompletedCB.isChecked());
                postData.put(JsonKeys.LightNovel.DESCRIPTION, mDescriptionET.getText());
                postData.put(JsonKeys.LightNovel.TRANSLATOR_SITE, mTranslatorSiteET.getText());

                String genres = "fantasy,martial arts,adventure,action";
                postData.put(JsonKeys.LightNovel.GENRES, genres);

                // TODO: include genres

                JsonObjectRequest json = new JsonObjectRequest(Request.Method.POST, ServerInfo.LIGHT_NOVEL.getUpdateUrl(mId), postData, onSuccess, onError);
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

    Response.Listener<JSONObject> onSuccess = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            if (response != null)
                Log.d(TAG, response.toString());
            Intent returnData = new Intent();
            returnData.putExtra(JsonKeys.LightNovel.ID, mId);

            getActivity().setResult(LiteraryTrackerUtils.EDIT_SUCCESS_CODE, returnData);
            getActivity().finish(); // Finishes the activity and goes back to the detail screen
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
