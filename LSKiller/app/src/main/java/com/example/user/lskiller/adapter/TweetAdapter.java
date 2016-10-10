package com.example.user.lskiller.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.user.lskiller.R;
import com.loopj.android.image.SmartImageView;

import twitter4j.Status;

/**
 * Created by USER on 2016/08/14.
 */
public class TweetAdapter extends ArrayAdapter<twitter4j.Status> {

    private LayoutInflater mInflater;

    public TweetAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
        mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_timeline, null);
        }
        Status item = getItem(position);
        // ユーザネーム
        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(item.getUser().getName());
        // userID
        TextView screenName = (TextView) convertView.findViewById(R.id.screen_name);
        screenName.setText("@" + item.getUser().getScreenName());
        // tweet detail
        TextView text = (TextView) convertView.findViewById(R.id.text);
        text.setText(item.getText());
        // icon
        SmartImageView icon = (SmartImageView)convertView.findViewById(R.id.icon);
        icon.setImageUrl(item.getUser().getProfileImageURL());
        return convertView;
    }


}
