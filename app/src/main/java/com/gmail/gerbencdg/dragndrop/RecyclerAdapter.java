package com.gmail.gerbencdg.dragndrop;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Gerben on 04/02/2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    protected class ViewHolder extends RecyclerView.ViewHolder {

        public CardView blockView;
        public ViewHolder(View itemView) {
            super(itemView);
            blockView = (CardView) itemView;
        }
    }

    private MainActivity ma;

    public RecyclerAdapter(MainActivity activity) {
        ma = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView blockView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.blockview, parent, false);

        return new ViewHolder(blockView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.blockView.setOnTouchListener(ma);
        holder.blockView.setOnDragListener(ma);
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
