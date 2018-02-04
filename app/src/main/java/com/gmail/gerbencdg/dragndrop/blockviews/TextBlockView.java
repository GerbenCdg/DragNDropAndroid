package com.gmail.gerbencdg.dragndrop.blockviews;

import android.content.Context;
import android.support.annotation.Dimension;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by Gerben on 04/02/2018.
 */

public class TextBlockView extends SimpleBlockView {

    private String text;
    private TextView mTextView;

    public TextBlockView(Context context) {
        super(context);
        inflateTextView();
    }

    public TextBlockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateTextView();
    }

    public TextBlockView(Context context, String text) {
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

    public void setText(String text) {
        this.text = text;
        invalidate();
        requestLayout();
    }
}
