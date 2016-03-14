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
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alec
 * @since March 12, 2016
 */
public class WrappedLinearLayout extends LinearLayout {

    private static final String TAG = "WrappedLinearlayout";

    private List<LinearLayout> rowLayouts;
    private int childrenWidth;
    private Integer screenWidth = null;


    public WrappedLinearLayout(Context context) {
        super(context);
        init();
    }

    public WrappedLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WrappedLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WrappedLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        this.rowLayouts = new ArrayList<>();
        this.childrenWidth = 0;
        this.setOrientation(VERTICAL);

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
            if (this.screenWidth == null) {
                this.screenWidth = this.getWidth();
            }
            child.measure(0, 0);
            childrenWidth += child.getMeasuredWidth();

            if (childrenWidth >= this.screenWidth) {
                createRowLayout();

                this.rowLayouts.get(this.rowLayouts.size() - 1).addView(child);
                childrenWidth = child.getMeasuredWidth();
            } else {
                this.rowLayouts.get(this.rowLayouts.size() - 1).addView(child);
            }
        }
    }
}
