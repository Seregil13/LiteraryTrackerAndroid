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
