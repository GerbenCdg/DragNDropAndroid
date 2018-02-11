package com.gmail.gerbencdg.dragndrop.blockviews;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Gerben on 04/02/2018.
 */

public abstract class ContainerBlockView extends BlockView {

    protected LinearLayout mLL;

    protected ContainerBlockView(Context context) {
        super(context);
        setOnDragListener((View.OnDragListener) context);
        initializeLinearLayout();
    }

  /*  private ContainerBlockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnDragListener((View.OnDragListener) context);
        initializeLinearLayout();
    }
*/

    public ContainerBlockView(Context context, @LayoutRes int layoutRes) {
        this(context);
        LayoutInflater.from(context).inflate(layoutRes, mLL);
    }

    protected void setHeader(ConditionContainer cc) {
        mLL.addView(cc, 0);
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
            if (index == 0) index = 1; // avoids setting the child above the header
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
            if (getChildCount() > 1) {
                params.height = LayoutParams.WRAP_CONTENT;
            }

            setLayoutParams(params);

            invalidate();
            requestLayout();
        }

        private void decreaseViewSize(View removedChild) {
            ViewGroup.LayoutParams params = getLayoutParams();

            if (getChildCount() <= 1) {
                params.height = getDefaultHeight();
            } else {
                params.height = LayoutParams.WRAP_CONTENT;
            }
            setLayoutParams(params);

            invalidate();
            requestLayout();
        }
    }

}
