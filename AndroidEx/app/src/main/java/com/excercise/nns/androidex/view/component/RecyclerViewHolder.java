package com.excercise.nns.androidex.view.component;

import android.support.v7.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.excercise.nns.androidex.databinding.TimelineItemBinding;

/**
 * Created by nns on 2017/07/12.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public final TimelineItemBinding binding;

    public RecyclerViewHolder(TimelineItemBinding binding) {
        super(binding.getRoot());
        // setup SwipeLayout
        SwipeLayout layout = binding.swipeLayout;
        layout.setShowMode(SwipeLayout.ShowMode.LayDown);
        layout.addDrag(SwipeLayout.DragEdge.Left, layout.findViewWithTag("swipe_menu_left"));
        layout.addDrag(SwipeLayout.DragEdge.Right, layout.findViewWithTag("swipe_menu_right"));
        this.binding = binding;
    }
}
