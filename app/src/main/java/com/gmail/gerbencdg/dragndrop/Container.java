package com.gmail.gerbencdg.dragndrop;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import static com.gmail.gerbencdg.dragndrop.blockviews.BlockView.DEFAULT_HEIGHT;
import static com.gmail.gerbencdg.dragndrop.blockviews.BlockView.DEFAULT_WIDTH;

/**
 * Created by Gerben on 11/02/2018.
 */

public abstract class Container extends LinearLayout {


    public Container(Context context) {
        super(context);
        setOrientation(VERTICAL);
        setLayoutParams(getDefaultLayoutParams());
        setBackgroundColor(getResources().getColor(R.color.cardview_light_background));

        int margin = dpToPx(8);
        MarginLayoutParams params = new MarginLayoutParams(getLayoutParams());
        params.setMargins(margin, margin, margin, margin*2);
        setLayoutParams(params);
    }

    @Override
    public void addView(View child) {
        setMargin(child);
        super.addView(child);
        increaseViewSize(child);
    }

    @Override
    public void addView(View child, int index) {
        setMargin(child);
        super.addView(child, index);
        increaseViewSize(child);
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
        decreaseViewSize(view);
    }

    private void setMargin(View child) {
        int margin = dpToPx(8);
        MarginLayoutParams params = new MarginLayoutParams(child.getLayoutParams());
        params.setMargins(margin, margin, margin, margin);
        child.setLayoutParams(params);
    }

    private void increaseViewSize(View child) {

        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        params.width = LinearLayout.LayoutParams.WRAP_CONTENT;

        setLayoutParams(params);

        invalidate();
        requestLayout();
    }

    private void decreaseViewSize(View removedChild) {

        if (getChildCount() < 1) {
            ViewGroup.LayoutParams params = getLayoutParams();

            params.height = getDefaultHeight();
            params.width = getDefaultWidth();
            setLayoutParams(params);

            invalidate();
            requestLayout();
        }

    }

    protected int dpToPx(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

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
