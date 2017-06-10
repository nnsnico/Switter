package com.example.user.lskiller.domain.usecase;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.io.FileNotFoundException;

import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by USER on 2016/10/19.
 */
public class MediaAsync extends AsyncTask<Void, Void, Long> {
    private Intent resultData;
    private Twitter mTwitter;
    private Context context;

    public MediaAsync(Intent resultData, Twitter mTwitter, Context context) {
        this.resultData = resultData;
        this.mTwitter = mTwitter;
        this.context = context;
    }

    @Override
    protected Long doInBackground(Void... voids) {
        ClipData clipData = resultData.getClipData();
        long[] mediaIds = new long[clipData.getItemCount()];
        for (int i = 0, length = clipData.getItemCount(); i < length; i++) {
            ClipData.Item item = clipData.getItemAt(i);
            try {
                mediaIds[i] = (mTwitter.uploadMedia(String.format("[filename_%d]", i + 1),
                        context.getContentResolver().openInputStream(item.getUri())).getMediaId());
                return mediaIds[i];
            } catch (TwitterException | FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return Long.parseLong(null);
    }

    @Override
    protected void onPostExecute(Long mediaId) {

    }
}
