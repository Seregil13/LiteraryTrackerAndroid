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

package com.seregil13.literarytracker.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seregil13.literarytracker.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Alec
 * @since March 12, 2016
 */
public class WrappedLinearLayout extends LinearLayout {

    private static final String TAG = "WrappedLinearlayout";

    private List<LinearLayout> rowLayouts;
    private int childrenWidth;
    private Integer screenWidth = null;

    private Context mContext;

    public WrappedLinearLayout(Context context) {
        super(context);
        init(context);
    }

    public WrappedLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WrappedLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WrappedLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        this.rowLayouts = new ArrayList<>();
        this.childrenWidth = 0;
        this.setOrientation(VERTICAL);

        this.mContext = context;

        createRowLayout();
    }

    private void createRowLayout() {
        LinearLayout layout = new LinearLayout(this.getContext());
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        layout.setOrientation(HORIZONTAL);

        this.addView(layout);
        this.rowLayouts.add(layout);
    }

    @Override
    public void addView(View child) {

        if (child instanceof LinearLayout) {
            super.addView(child);
        } else {
            if (this.screenWidth == null || this.screenWidth == 0) {
                this.screenWidth = this.getWidth();
                Log.d(TAG, String.format("Screen width: %d", this.screenWidth));
            }
            child.measure(0, 0);
            childrenWidth += child.getMeasuredWidth();

            if (childrenWidth >= this.screenWidth) {
                createRowLayout();
                Log.d(TAG, String.format(Locale.US, "Starting new row -> childrenWidth: %d\tscreenWidth: %d\n", childrenWidth, this.screenWidth));

                this.rowLayouts.get(this.rowLayouts.size() - 1).addView(child);
                childrenWidth = child.getMeasuredWidth();
            } else {
                this.rowLayouts.get(this.rowLayouts.size() - 1).addView(child);
            }
        }
    }

    public void addTextView(String s) {
        /* Creates a textview to hold the genre */
        TextView view = new TextView(mContext);
        view.setText(s);
        view.setBackgroundResource(R.drawable.border);
        view.setTextColor(Color.WHITE);
        view.setPadding(15,10,15,10);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,10,10,10);
        view.setLayoutParams(params);

        this.addView(view);
    }

    public void addTextViews(ArrayList<String> als) {
        for (String s : als) {
            addTextView(s);
        }
    }
}
