package com.example.user.lskiller.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.lskiller.R;
import com.loopj.android.image.SmartImage;
import com.loopj.android.image.SmartImageView;

import twitter4j.MediaEntity;

/**
 * Created by USER on 2016/10/06.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder{

    View swipeableContainer;
    View optionView1;
    View optionView2;
    View optionView3;
    TextView textView;
    TextView name;
    TextView screenName;
    SmartImageView icon;
    SmartImageView media1;
    SmartImageView media2;
    SmartImageView media3;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.text);
        name = (TextView) itemView.findViewById(R.id.name);
        screenName = (TextView) itemView.findViewById(R.id.screen_name);
        icon = (SmartImageView) itemView.findViewById(R.id.icon);
        media1 = (SmartImageView) itemView.findViewById(R.id.media1);
        media2 = (SmartImageView) itemView.findViewById(R.id.media2);
        media3 = (SmartImageView) itemView.findViewById(R.id.media3);
    }
}
