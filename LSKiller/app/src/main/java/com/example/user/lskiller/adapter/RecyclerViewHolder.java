package com.example.user.lskiller.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.example.user.lskiller.R;
import com.loopj.android.image.SmartImageView;

/**
 * Created by USER on 2016/10/06.
 */
class RecyclerViewHolder extends RecyclerView.ViewHolder {

    TextView textView;
    TextView name;
    TextView screenName;
    TextView createTime;
    TextView reTweetedUser;
    SmartImageView icon;
    GridLayout gridLayout;
    SwipeLayout swipeLayout;

    RecyclerViewHolder(View itemView) {
        super(itemView);

        textView = (TextView) itemView.findViewById(R.id.text);
        name = (TextView) itemView.findViewById(R.id.name);
        screenName = (TextView) itemView.findViewById(R.id.screen_name);
        createTime = (TextView) itemView.findViewById(R.id.createTime);
        reTweetedUser = (TextView) itemView.findViewById(R.id.reTweetedUser);
        icon = (SmartImageView) itemView.findViewById(R.id.icon);
        icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        gridLayout = (GridLayout) itemView.findViewById(R.id.grid);
        swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipeMenu);
    }
}
