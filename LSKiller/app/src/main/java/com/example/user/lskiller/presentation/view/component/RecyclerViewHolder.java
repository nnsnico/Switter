package com.example.user.lskiller.presentation.view.component;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.example.user.lskiller.R;
import com.loopj.android.image.SmartImageView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by USER on 2016/10/06.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView textView;
    public TextView name;
    public TextView screenName;
    public TextView createTime;
    public TextView reTweetedUser;
    public CircleImageView icon;
    public GridLayout gridLayout;
    public SwipeLayout swipeLayout;

    public RecyclerViewHolder(View itemView) {
        super(itemView);

        textView = (TextView) itemView.findViewById(R.id.text);
        name = (TextView) itemView.findViewById(R.id.name);
        screenName = (TextView) itemView.findViewById(R.id.screen_name);
        createTime = (TextView) itemView.findViewById(R.id.createTime);
        reTweetedUser = (TextView) itemView.findViewById(R.id.reTweetedUser);
        icon = (CircleImageView) itemView.findViewById(R.id.icon);
        icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        gridLayout = (GridLayout) itemView.findViewById(R.id.grid);
        swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipeMenu);
    }
}
