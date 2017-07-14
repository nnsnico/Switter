package com.excercise.nns.androidex.view.component;

import android.support.v7.widget.RecyclerView;

import com.excercise.nns.androidex.databinding.TimelineItemBinding;

/**
 * Created by nns on 2017/07/12.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public final TimelineItemBinding binding;

    public RecyclerViewHolder(TimelineItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
