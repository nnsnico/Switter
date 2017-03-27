package com.example.user.lskiller.domain.usecase;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.example.user.lskiller.presentation.view.util.TwitterUtils;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by USER on 2017/02/14.
 */

public class MuteAsync extends AsyncTask<Long, Void, Boolean> {

    private Twitter mTwitter;
    private Context context;
    private Activity activity;
    private twitter4j.Status status;

    public MuteAsync(Context context, Activity activity, twitter4j.Status status) {
        this.context = context;
        this.status = status;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mTwitter = TwitterUtils.getTwitterInstance(context);
    }

    @Override
    protected Boolean doInBackground(Long... params) {
        try {
            // TODO: destroyMute
            mTwitter.createMute(status.getUser().getId());
            return true;
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        Snackbar snackbar;

        if (result) {
            snackbar = Snackbar.make(
                    activity.findViewById(android.R.id.content),
                    "Muted",
                    Snackbar.LENGTH_LONG
            );
            snackbar.show();
        } else {
            snackbar = Snackbar.make(
                    activity.findViewById(android.R.id.content),
                    "Missed Mute",
                    Snackbar.LENGTH_LONG
            );
            snackbar.show();
        }
    }
}
