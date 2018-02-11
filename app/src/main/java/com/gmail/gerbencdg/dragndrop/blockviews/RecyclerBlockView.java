package com.gmail.gerbencdg.dragndrop.blockviews;

import android.content.Context;
import android.support.annotation.Dimension;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by Gerben on 04/02/2018.
 */

public class RecyclerBlockView extends SimpleBlockView {

    private String text;
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

    public RecyclerBlockView(Context context, String text) {
        super(context);
        inflateTextView();
        mTextView.setText(text);
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

    public String getText() {
        return text;
    }

    public BlockView getRealBv() {
        return realBv;
    }

    @Override
    public BlockView clone() {
        // we ensure a new copy is made of the BlockView each time a dragNDrop is started
        return realBv.clone();
    }

    public void setRealBv(BlockView rbv) {
        if (realBv != null) {
            removeView(rbv);
        }
        this.realBv = rbv;
        realBv.setVisibility(GONE);
        addView(realBv);
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
        requestLayout();
    }

    @Override
    public String getBlockName() {
        return "Text Blockview";
    }
}
