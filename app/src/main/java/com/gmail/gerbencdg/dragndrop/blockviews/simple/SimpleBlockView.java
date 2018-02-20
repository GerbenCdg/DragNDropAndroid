package com.gmail.gerbencdg.dragndrop.blockviews.simple;

import android.content.Context;
import android.util.AttributeSet;

import com.gmail.gerbencdg.dragndrop.blockviews.BlockView;

/**
 * Created by Gerben on 04/02/2018.
 */

public abstract class SimpleBlockView extends BlockView {

    public SimpleBlockView(Context context) {
        super(context);
    }

    public SimpleBlockView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean isContainer() {
        return false;
    }
}
