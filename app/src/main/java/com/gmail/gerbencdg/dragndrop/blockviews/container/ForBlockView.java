package com.gmail.gerbencdg.dragndrop.blockviews.container;

import android.content.Context;

import com.gmail.gerbencdg.dragndrop.R;
import com.gmail.gerbencdg.dragndrop.blockviews.BlockView;

/**
 * Created by Gerben on 11/02/2018.
 */

public class ForBlockView extends ContainerBlockView {

    public ForBlockView(Context context) {
        super(context, R.layout.cardview_for, new InstructionContainer(context));
    }

    @Override
    public String getBlockName() {
        return "For loop";
    }

    @Override
    public BlockView clone() {
        return new ForBlockView(getContext());
    }
}
