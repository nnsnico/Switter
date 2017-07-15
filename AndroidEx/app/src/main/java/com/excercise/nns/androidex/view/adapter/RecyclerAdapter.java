package com.excercise.nns.androidex.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.excercise.nns.androidex.contract.OnRecyclerListener;
import com.excercise.nns.androidex.databinding.TimelineItemBinding;
import com.excercise.nns.androidex.model.entity.TwitterStatus;
import com.excercise.nns.androidex.view.component.RecyclerViewHolder;

import java.util.List;

/**
 * Created by nns on 2017/07/12.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private List<TwitterStatus> statuses;
    private OnRecyclerListener listener;

    public RecyclerAdapter(List<TwitterStatus> statuses, OnRecyclerListener listener) {
        this.statuses = statuses;
        this.listener = listener;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TimelineItemBinding binding = TimelineItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new RecyclerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        TwitterStatus status = statuses.get(position);
        holder.binding.setListener(listener);
        holder.binding.setStatus(status);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return statuses.size();
    }

}