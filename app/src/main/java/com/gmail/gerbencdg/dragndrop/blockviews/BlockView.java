package com.gmail.gerbencdg.dragndrop.blockviews;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;

/**
 * Created by Gerben on 04/02/2018.
 */

public abstract class BlockView extends CardView {

    private static final int DEFAULT_HEIGHT = 120;
    private static final int DEFAULT_WIDTH = 200;

    public BlockView(Context context) {
        super(context);
        applyParams();
    }

    public BlockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyParams();
    }

    private void applyParams() {
        ViewGroup.MarginLayoutParams params = getDefaultMarginLayoutParams();
        int margin = dpToPx(12);
        params.setMargins(margin,margin,margin,margin);
        setLayoutParams(params);

        super.setRadius(4);
        invalidate();
        requestLayout();
    }

    abstract public boolean isContainer();

    protected int dpToPx(float dp) {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    // allows to set margin params, and works as CardView extends from FrameLayout


    public ViewGroup.MarginLayoutParams getDefaultMarginLayoutParams() {
        return new ViewGroup.MarginLayoutParams(getDefaultWidth(), getDefaultHeight());
    }

    public ViewGroup.LayoutParams getDefaultLayoutParams() {
        return new ViewGroup.LayoutParams(getDefaultWidth(), getDefaultHeight());
    }

    public int getDefaultHeight() {
        return dpToPx(DEFAULT_HEIGHT);
    }
    public int getDefaultWidth() {
        return dpToPx(DEFAULT_WIDTH);
    }
}
