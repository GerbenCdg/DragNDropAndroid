package com.gmail.gerbencdg.dragndrop.blockviews;

import android.content.Context;
import android.support.annotation.Dimension;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by Gerben on 12/02/2018.
 */

public class TextBlockView extends SimpleBlockView {

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
        return mTextView.getText().toString();
    }

    @Override
    public BlockView clone() {
        // we ensure a new copy is made of the BlockView each time a dragNDrop is started
        return new TextBlockView(getContext(), getText());
    }

    public void setText(String text) {
        mTextView.setText(text);
        invalidate();
        requestLayout();
    }

    @Override
    public String getBlockName() {
        return getText();
    }
}
