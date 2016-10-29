package com.example.user.lskiller.AsyncTask;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.example.user.lskiller.Utils.TwitterUtils;

import java.util.List;

import twitter4j.Status;
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
    protected void onPreExecute() {
        mTwitter = TwitterUtils.getTwitterInstance(context);
    }

    @Override
    protected Boolean doInBackground(Long... longs) {

        if (statuses.get(position).isRetweeted()) {
            try {
                if (!statuses.get(position).isRetweetedByMe()) {
                    Log.d("destroy retweet" , "destroy retweet of me");
                    mTwitter.destroyStatus(statuses.get(position).getId());
                    mTwitter.updateStatus(statuses.get(position).getText());
                    return true;
                } else {
                    Log.d("destroy retweet" , "destroy retweet of User");
                    mTwitter.destroyStatus(statuses.get(position).getCurrentUserRetweetId());
                    return true;
                }
            } catch (TwitterException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            try {
                mTwitter.retweetStatus(statuses.get(position).getId());
                return true;
            } catch (TwitterException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        Snackbar snackbar;

        if (result) {
            if (!statuses.get(position).isRetweeted()) {
                snackbar = Snackbar.make(
                        activity.findViewById(android.R.id.content),
                        "ReTweet",
                        Snackbar.LENGTH_LONG
                );
                snackbar.show();
            } else {
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
