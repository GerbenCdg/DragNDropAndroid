package com.gmail.gerbencdg.dragndrop;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.gerbencdg.dragndrop.blockviews.BlockView;
import com.gmail.gerbencdg.dragndrop.blockviews.ForBlockView;
import com.gmail.gerbencdg.dragndrop.blockviews.IfBlockView;
import com.gmail.gerbencdg.dragndrop.blockviews.RecyclerBlockView;
import com.gmail.gerbencdg.dragndrop.blockviews.TextBlockView;

/**
 * Created by Gerben on 04/02/2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        // displayed in the recyclerview
        RecyclerBlockView recyclerBv;
        TextView tvBlockName;

        ViewHolder(RecyclerBlockView itemView) {
            super(itemView);
            recyclerBv = itemView;
            tvBlockName = recyclerBv.findViewById(R.id.tv_blockname);
        }
    }

    private MainActivity ma;
    private BlockView[] mBlockViews;

    public RecyclerAdapter(MainActivity ac) {
        ma = ac;
        mBlockViews = new BlockView[]{new ForBlockView(ac), new IfBlockView(ac), new TextBlockView(ac, "TextBlockView 1"), new TextBlockView(ac, "TextBlockView 2") };
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerBlockView blockView = (RecyclerBlockView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerblockview, parent, false);

        return new ViewHolder(blockView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        BlockView bv = mBlockViews[position];
        holder.recyclerBv.setRealBv(bv);
        holder.tvBlockName.setText(bv.getBlockName());
        Log("OnBind !" + bv.getBlockName());

        holder.recyclerBv.setOnTouchListener(ma);
        holder.recyclerBv.setOnDragListener(ma);
    }

    @Override
    public int getItemCount() {
        return mBlockViews.length;
    }

    private void Log(String s) {
        Log.w("RecyclerAdapter", s);
    }

}
