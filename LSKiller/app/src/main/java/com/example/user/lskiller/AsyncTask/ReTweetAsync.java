package com.example.user.lskiller.AsyncTask;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;

import com.example.user.lskiller.Utils.TwitterUtils;

import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by USER on 2016/10/13.
 */
public class ReTweetAsync extends AsyncTask<Long, Void, Boolean> {

    Context context;
    Activity activity;
    List<twitter4j.Status> statuses;
    Twitter mTwitter;
    int position;

    public ReTweetAsync(
            Activity activity,
            Context context,
            List<twitter4j.Status> statuses,
            int position
    ) {
        this.activity = activity;
        this.context = context;
        this.statuses = statuses;
        this.position = position;
    }

    @Override
    protected void onPreExecute(){
        mTwitter = TwitterUtils.getTwitterInstance(context);
    }

    @Override
    protected Boolean doInBackground(Long... longs) {
        if(statuses.get(position).isRetweeted()){
            try{
                mTwitter.destroyStatus(statuses.get(position).getId());
                return true;
            }catch (TwitterException e){
                e.printStackTrace();
                return false;
            }
        }else{
            try{
                mTwitter.retweetStatus(statuses.get(position).getId());
                return true;
            }catch (TwitterException e){
                e.printStackTrace();
                return false;
            }
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        Snackbar snackbar;

        if (result) {
            if (!statuses.get(position).isFavorited()) {
                snackbar = Snackbar.make(
                        activity.findViewById(android.R.id.content),
                        "ReTweet",
                        Snackbar.LENGTH_LONG
                );
                snackbar.show();
            }else {
                snackbar = Snackbar.make(
                        activity.findViewById(android.R.id.content),
                        "destroy ReTweet",
                        Snackbar.LENGTH_LONG
                );
                snackbar.show();
            }
        } else {
            snackbar = Snackbar.make(
                    activity.findViewById(android.R.id.content),
                    "Can't ReTweet",
                    Snackbar.LENGTH_LONG
            );
            snackbar.show();
        }
    }

}
