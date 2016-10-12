package com.example.user.lskiller.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.example.user.lskiller.R;
import com.loopj.android.image.SmartImage;
import com.loopj.android.image.SmartImageView;

import twitter4j.MediaEntity;

/**
 * Created by USER on 2016/10/06.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder{

    TextView textView;
    TextView name;
    TextView screenName;
    SmartImageView icon;
    GridLayout gridLayout;
    SwipeLayout swipeLayout;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.text);
        name = (TextView) itemView.findViewById(R.id.name);
        screenName = (TextView) itemView.findViewById(R.id.screen_name);
        icon = (SmartImageView) itemView.findViewById(R.id.icon);
        gridLayout = (GridLayout) itemView.findViewById(R.id.grid);
        swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipeMenu);
    }
}
