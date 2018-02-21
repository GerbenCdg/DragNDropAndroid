package com.gmail.gerbencdg.dragndrop;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.gerbencdg.dragndrop.blockviews.BlockView;
import com.gmail.gerbencdg.dragndrop.blockviews.RecyclerBlockView;
import com.gmail.gerbencdg.dragndrop.blockviews.container.ForBlockView;
import com.gmail.gerbencdg.dragndrop.blockviews.container.IfBlockView;
import com.gmail.gerbencdg.dragndrop.blockviews.simple.InstructionBlockView;
import com.gmail.gerbencdg.dragndrop.blockviews.simple.TextBlockView;

import java.util.ArrayList;
import java.util.List;

import static android.support.annotation.Dimension.SP;

/**
 * Created by Gerben on 04/02/2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        // displayed in the recyclerview
        RecyclerBlockView recyclerBv;
        TextView tvBlockName;
        ImageView iv;

        ViewHolder(RecyclerBlockView itemView) {
            super(itemView);
            recyclerBv = itemView;
            tvBlockName = recyclerBv.findViewById(R.id.tv_blockname);
            iv = recyclerBv.findViewById(R.id.rbv_imageview);
        }

        void setDrawable(Drawable drawable) {

            iv.setImageDrawable(drawable);

            if (drawable == null) {
                iv.setVisibility(View.GONE);
                tvBlockName.setTextSize(SP, 16);
            } else {
                iv.setVisibility(View.VISIBLE);
                tvBlockName.setTextSize(SP, 12);
            }
        }
    }

    private MainActivity ma;
    private List<BlockView> mBlockViews;
    private List<BlockView> mFilteredBvs;
    private BlockView.Categories mLastCategory;

    public RecyclerAdapter(MainActivity ac, BlockView.Categories selectedCat) {

        ma = ac;
        mBlockViews = new ArrayList<>();
        mBlockViews.add(new ForBlockView(ac));
        mBlockViews.add(new IfBlockView(ac));
        mBlockViews.add(new InstructionBlockView(ac, R.drawable.turn_left){
            @Override
            public String getBlockName() {
                return "Turn Left";
            }
        });
        mBlockViews.add(new InstructionBlockView(ac, R.drawable.turn_right){
            @Override
            public String getBlockName() {
                return "Turn Right";
            }
        });
        mBlockViews.add(new TextBlockView(ac, "Action 1"));
        mBlockViews.add(new TextBlockView(ac, "Action 2"));

        mFilteredBvs = new ArrayList<>();
        mFilteredBvs.addAll(mBlockViews);
        mLastCategory = selectedCat;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerBlockView blockView = (RecyclerBlockView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerblockview, parent, false);

        return new ViewHolder(blockView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        BlockView bv = mFilteredBvs.get(position);
        holder.recyclerBv.setRealBv(bv);
        holder.tvBlockName.setText(bv.getBlockName());

        if (bv instanceof InstructionBlockView) {
            InstructionBlockView ibv = (InstructionBlockView) bv;
            holder.setDrawable(ibv.getDrawable());
        } else {
            holder.setDrawable(null);
        }

        holder.recyclerBv.setOnTouchListener(ma);
        holder.recyclerBv.setOnDragListener(ma);
    }

    @Override
    public int getItemCount() {
        return mFilteredBvs.size();
    }

    private void Log(String s) {
        Log.w("RecyclerAdapter", s);
    }

    public void setFilter(BlockView.Categories category) {
        mFilteredBvs.clear();

        if (mLastCategory.equals(category)) {
            return;
        }

        mLastCategory = category;

        if (category.equals(BlockView.Categories.ALL)) {
            mFilteredBvs.addAll(mBlockViews);
            notifyDataSetChanged();
            return;
        }

        for (BlockView bv : mBlockViews) {
            for (BlockView.Categories cat : bv.getCategories()) {
                if (cat.equals(category)) {
                    mFilteredBvs.add(bv);
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

}
