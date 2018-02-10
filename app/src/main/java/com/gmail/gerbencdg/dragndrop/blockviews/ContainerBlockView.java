package com.gmail.gerbencdg.dragndrop.blockviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Gerben on 04/02/2018.
 */

public class ContainerBlockView extends BlockView {

    private LinearLayout mLL;

    public ContainerBlockView(Context context) {
        super(context);
        setOnDragListener((View.OnDragListener) context);
        initializeLinearLayout();
    }

    public ContainerBlockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnDragListener((View.OnDragListener) context);
        initializeLinearLayout();
    }

    @Override
    public boolean isContainer() {
        return true;
    }

    private void initializeLinearLayout() {

        setLayoutParams(new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        mLL = new ResizingLinearLayout(getContext());
        addView(mLL);

        mLL.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_bright));
    }

    private void setMargin(View child) {
        int margin = dpToPx(8);
        MarginLayoutParams params = new MarginLayoutParams(child.getLayoutParams());
        params.setMargins(margin, margin, margin, margin);
        child.setLayoutParams(params);
    }


    private class ResizingLinearLayout extends LinearLayout {

        public ResizingLinearLayout(Context context) {
            super(context);
            setOrientation(VERTICAL);
            setLayoutParams(getDefaultLayoutParams());
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

        private void increaseViewSize(View child) {

            ViewGroup.LayoutParams params = getLayoutParams();

            params.height = LayoutParams.WRAP_CONTENT;
            //  params.height += child.getHeight();

            setLayoutParams(params);

            invalidate();
            requestLayout();
        }

        private void decreaseViewSize(View removedChild) {
            ViewGroup.LayoutParams params = getLayoutParams();

            if (getChildCount() != 0) {
                params.height = getDefaultHeight();
                //params.height -= removedChild.getHeight();
            } else {
                params.height = LayoutParams.WRAP_CONTENT;
            }
            setLayoutParams(params);

            invalidate();
            requestLayout();
        }
    }

}
