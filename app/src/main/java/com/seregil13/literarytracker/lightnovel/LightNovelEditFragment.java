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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.seregil13.literarytracker.R;
import com.seregil13.literarytracker.util.JsonKeys;

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
    private List<String> mGenres;

    /* Views */
    private EditText mTitleET;
    private EditText mAuthorET;
    private EditText mDescriptionET;
    private CheckBox mCompletedET;
    private EditText mTranslatorSiteET;
    /* TODO: input genres */

    /**
     * Requiired empty public constructor
     */
    public LightNovelEditFragment() {}

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

        mTitleET = (EditText) view.findViewById(R.id.title);
        mAuthorET = (EditText) view.findViewById(R.id.author);
        mDescriptionET = (EditText) view.findViewById(R.id.description);
        mTranslatorSiteET = (EditText) view.findViewById(R.id.translatorSite);
        mCompletedET = (CheckBox) view.findViewById(R.id.completionStatus);

        Log.d(TAG, mTitle);
        Log.d(TAG, mAuthor);
        Log.d(TAG, mDescription);

        mTitleET.setText(mTitle);
        mAuthorET.setText(mAuthor);
        mDescriptionET.setText(mDescription);
        mTranslatorSiteET.setText(mTranslatorSite);
        mCompletedET.setChecked(Boolean.parseBoolean(mCompleted));

        return view;
    }

}
