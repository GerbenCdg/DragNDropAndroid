package com.gmail.gerbencdg.dragndrop.blockviews;

import android.content.Context;
import android.util.AttributeSet;

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
