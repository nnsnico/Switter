package com.example.user.lskiller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.lskiller.OnRecyclerListener;
import com.example.user.lskiller.R;

import java.util.List;

/**
 * Created by USER on 2016/10/05.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private LayoutInflater mInflater;
    private List<twitter4j.Status> statuses;
    private List<String> mediaList;
    private Context mContext;
    private OnRecyclerListener mListener;

    public RecyclerAdapter(Context context,List<String> mediaList, List<twitter4j.Status> data, OnRecyclerListener listener) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        this.mediaList = mediaList;
        statuses = data;
        mListener = listener;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new RecyclerViewHolder(mInflater.inflate((R.layout.item_timeline), parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {

        holder.screenName.setText("@" + statuses.get(position).getUser().getScreenName());
        holder.name.setText(statuses.get(position).getUser().getName());
        holder.textView.setText(statuses.get(position).getText());
        holder.icon.setImageUrl(statuses.get(position).getUser().getProfileImageURL());

        holder.media1.setImageUrl(mediaList.get(position));
        holder.media2.setImageUrl(mediaList.get(position));
        holder.media3.setImageUrl(mediaList.get(position));
        holder.itemView.setClickable(true);

        // クリック処理
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRecyclerClicked(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (statuses != null) {
            return statuses.size();
        } else {
            return 0;
        }
    }
}
