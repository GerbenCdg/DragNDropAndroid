package com.gmail.gerbencdg.dragndrop.blockviews;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * Created by Gerben on 04/02/2018.
 */

public abstract class BlockView extends CardView implements Cloneable {

    public static final int DEFAULT_HEIGHT = 120;
    public static final int DEFAULT_WIDTH = 200;

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
        int margin = dpToPx(16);
        params.setMargins(margin, margin, margin, margin);
        setLayoutParams(params);

        super.setRadius(8);
        invalidate();
        requestLayout();
    }

    abstract public boolean isContainer();

    abstract public BlockView clone();

    abstract public Categories[] getCategories();

    abstract public String getBlockName();

    protected int dpToPx(float dp) {
        return dpToPx(dp, getResources());
    }

    public static int dpToPx(float dp, Resources res) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
    }

    // allows to set margin params, and works as CardView extends from FrameLayout


    public ViewGroup.MarginLayoutParams getDefaultMarginLayoutParams() {
        return new ViewGroup.MarginLayoutParams(getDefaultWidth(), getDefaultHeight());
    }

    public int getDefaultHeight() {
        return dpToPx(DEFAULT_HEIGHT);
    }

    public int getDefaultWidth() {
        return dpToPx(DEFAULT_WIDTH);
    }

    public int getDepth() {
        int depth = 0;

        for (ViewParent iterView = this; iterView != null && iterView instanceof BlockView; depth++) {
            // Container, LinearLayout, BlockView
            for (int i = 0; i < 3; i++) {
                if (iterView != null)
                    iterView = iterView.getParent();
                else return depth;
            }
        }
        return depth;
    }

    public enum Categories{
        INSTRUCTIONS,
        ALL,
        LOGIC,
        CONTAINERS,
        OTHERS
    }

}
