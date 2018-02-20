package com.gmail.gerbencdg.dragndrop.blockviews.container;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.gmail.gerbencdg.dragndrop.Container;
import com.gmail.gerbencdg.dragndrop.blockviews.BlockView;

/**
 * Created by Gerben on 04/02/2018.
 */

public abstract class ContainerBlockView extends BlockView {

    protected LinearLayout mLL;
    private View mHeader;
    private Container mContainer;

    private ContainerBlockView(Context context, Container container) {
        super(context);

        mLL = new LinearLayout(context);
        mLL.setOrientation(LinearLayout.VERTICAL);
        mLL.setLayoutParams(new FrameLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        addView(mLL);

        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        setLayoutParams(params);

        mContainer = container;
        mContainer.setOnDragListener((View.OnDragListener) context);
        mLL.addView(container);
    }

    protected ContainerBlockView(Context context, View header, Container container) {
        super(context);
        setHeader(header);
    }

    // TODO allow to use ConditionContainer as header
    protected ContainerBlockView(Context context, @LayoutRes int layoutRes, Container container) {
        this(context, container);
        setHeader(LayoutInflater.from(context).inflate(layoutRes, null));
    }

    private void setHeader(View header) {
        mHeader = header;
        mLL.addView(header, 0);
    }

    @Override
    public Categories[] getCategories() {
        return new Categories[]{Categories.CONTAINERS};
    }

    @Override
    public boolean isContainer() {
        return true;
    }


}
