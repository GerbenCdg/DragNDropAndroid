package com.gmail.gerbencdg.dragndrop.blockviews;

import android.content.Context;
import android.support.annotation.Dimension;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.gerbencdg.dragndrop.blockviews.simple.SimpleBlockView;

/**
 * Created by Gerben on 04/02/2018.
 */

public class RecyclerBlockView extends SimpleBlockView {

    private BlockView cloneInstance;

    private TextView mTextView;
    private BlockView realBv;

    public RecyclerBlockView(Context context) {
        super(context);
        inflateTextView();
    }

    public RecyclerBlockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateTextView();
    }

    private void inflateTextView() {
        mTextView = new TextView(getContext());
        mTextView.setTextSize(Dimension.SP, 16);
        mTextView.setGravity(Gravity.CENTER);

        int padding = dpToPx(12);
        setPadding(padding, padding, padding, padding);

        addView(mTextView);

        invalidate();
        requestLayout();
    }

    public void setRealBv(BlockView rbv) {
        if (realBv != null) {
            removeView(rbv);
        }
        this.realBv = rbv;
        realBv.setVisibility(GONE);
        if (realBv.getParent() != null) {
            ((ViewGroup) realBv.getParent()).removeView(realBv);
        }
        addView(realBv);
    }

    private BlockView setCloneInstance() {
        if (cloneInstance != null) {
            removeCloneInstance(); // TODO check coherance
            //throw new IllegalStateException("removeCloneInstance should have been called !");
        }
        cloneInstance = realBv.clone();
        addView(cloneInstance);
        return cloneInstance;
    }

    // To be called when DragNDrop from RecyclerView is cancelled
    public void removeCloneInstance() {
        removeView(cloneInstance);
        cloneInstance = null;
    }

    @Override
    public BlockView clone() {
        // we ensure a new copy is made of the BlockView each time a dragNDrop is started
        return setCloneInstance();
    }

    @Override
    public Categories[] getCategories() {
        return new Categories[]{Categories.OTHERS};
    }

    @Override
    public String getBlockName() {
        return "Text Blockview";
    }
}
