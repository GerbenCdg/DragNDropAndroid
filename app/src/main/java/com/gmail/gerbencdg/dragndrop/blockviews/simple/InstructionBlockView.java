package com.gmail.gerbencdg.dragndrop.blockviews.simple;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.gmail.gerbencdg.dragndrop.blockviews.BlockView;

/**
 * Created by Gerben on 20/02/2018.
 */

public class InstructionBlockView extends SimpleBlockView {

    private Drawable mDrawable;

    public InstructionBlockView(Context context, @DrawableRes int drawableRes) {
        super(context);
        mDrawable = ContextCompat.getDrawable(context, drawableRes);
        setImage(mDrawable);
    }

    public InstructionBlockView(Context context, Drawable drawable) {
        super(context);
        this.mDrawable = drawable;
        setImage(drawable);
    }

    private void setImage(Drawable drawable) {

        ImageView imageView = new ImageView(getContext());
        imageView.setImageDrawable(drawable);
        addView(imageView);

        int padding = dpToPx(12);
        setPadding(padding, padding, padding, padding);

        invalidate();
        requestLayout();
    }

    @Override
    public Categories[] getCategories() {
        return new Categories[]{Categories.INSTRUCTIONS};
    }

    @Override
    public BlockView clone() {
        return new InstructionBlockView(getContext(), mDrawable);
    }

    @Override
    public String getBlockName() {
        return "InstructionBlockView";
    }
}
