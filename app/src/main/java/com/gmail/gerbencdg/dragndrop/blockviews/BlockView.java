package com.gmail.gerbencdg.dragndrop.blockviews;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.TypedValue;

/**
 * Created by Gerben on 04/02/2018.
 */

public abstract class BlockView extends CardView {

    public BlockView(Context context) {
        super(context);
        applyParams();
    }

    public BlockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyParams();
    }

    private void applyParams() {
        LayoutParams params = new LayoutParams(dpToPx(150), dpToPx(100));
        setLayoutParams(params);

        int margin = dpToPx(12);
        params.setMargins(margin,margin,margin,margin);
        super.setRadius(4);
        invalidate();
        requestLayout();
    }

    abstract public boolean isContainer();

    protected int dpToPx(float dp) {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

}
