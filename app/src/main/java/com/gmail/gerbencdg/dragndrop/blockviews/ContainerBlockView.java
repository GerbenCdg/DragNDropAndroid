package com.gmail.gerbencdg.dragndrop.blockviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Gerben on 04/02/2018.
 */

public class ContainerBlockView extends BlockView {

    public ContainerBlockView(Context context) {
        super(context);
        setOnDragListener((View.OnDragListener) context);
    }

    public ContainerBlockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnDragListener((View.OnDragListener) context);
    }

    @Override
    public boolean isContainer() {
        return true;
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
        params.height+= child.getHeight();
        setLayoutParams(params);

        invalidate();
        requestLayout();
    }

    private void decreaseViewSize(View removedChild) {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height-= removedChild.getHeight();
        setLayoutParams(params);

        invalidate();
        requestLayout();
    }
}
