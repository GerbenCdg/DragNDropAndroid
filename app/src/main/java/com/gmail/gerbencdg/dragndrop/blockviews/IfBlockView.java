package com.gmail.gerbencdg.dragndrop.blockviews;

import android.content.Context;

import com.gmail.gerbencdg.dragndrop.R;

/**
 * Created by Gerben on 11/02/2018.
 */

public class IfBlockView extends ContainerBlockView{

    public IfBlockView(Context context) {
        // TODO allow to use ConditionContainer as header instead of R.layout.cardview_if
        super(context, R.layout.cardview_if, new InstructionContainer(context));
    }

    @Override
    public String getBlockName() {
        return "If condition";
    }

    @Override
    public BlockView clone() {
        return new IfBlockView(getContext());
    }
}
