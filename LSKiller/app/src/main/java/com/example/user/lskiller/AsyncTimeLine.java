package com.example.user.lskiller;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.example.user.lskiller.adapter.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import twitter4j.MediaEntity;
import twitter4j.Paging;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by USER on 2016/10/09.
 */
public class AsyncTimeLine extends AsyncTask<Void, Void, List<twitter4j.Status>> {

    Context context;
    Twitter mTwitter;
    List<twitter4j.Status> statuses;
    List<String> mediaList = new ArrayList<String>();
    Activity activity;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    MediaEntity[] mediaEntities;

    public AsyncTimeLine(
            Twitter mTwitter,
            List<twitter4j.Status> statuses,
            Activity activeView,
            RecyclerView recyclerView
    ){
        this.mTwitter = mTwitter;
        this.statuses = statuses;
        this.activity = activeView;
        this.recyclerView = recyclerView;
    }

    @Override
    public void onPreExecute(){
        progressBar = (ProgressBar) activity.findViewById(R.id.progress);
        progressBar.setVisibility(ProgressBar.VISIBLE);
    }

    @Override
    protected List<twitter4j.Status> doInBackground(Void... voids) {
        try {
//            ProgressBar progressBar = (ProgressBar)findViewById(R.id.progress);
            // 1ページ当たりの取得ツイート数
            Paging page = new Paging(1, 40);

            return mTwitter.getHomeTimeline(page);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<twitter4j.Status> result){
        progressBar.setVisibility(ProgressBar.GONE);
        if (result != null) {
            mediaList.clear();
            statuses.clear();
            int count = 1;
            for (twitter4j.Status status : result) {
                statuses.add(status);
                // TODO 画像の複数表示
                mediaEntities = status.getExtendedMediaEntities();
                if(mediaEntities.length > 0) {
                    for(MediaEntity mediaResult : mediaEntities){
                        mediaList.add(mediaResult.getMediaURL());
                    }
                    Log.d("activity_tag", "mediaList.added from page " + count);
                }else{
                    mediaList.add(null);
                }
                Log.d("activity_tag", "mData.added from page " + count);
                count++;
            }
            recyclerView.setAdapter(
                    new RecyclerAdapter(
                            activity,
                            mediaList,
                            statuses,
                            (OnRecyclerListener) activity
                    )
            );
        } else {
            final Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), "タイムラインの取得に失敗しました\n時間を置いてから再度起動してください", Snackbar.LENGTH_INDEFINITE);
            View view = snackbar.getView();
            FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
            params.gravity = Gravity.TOP;
            view.setLayoutParams(params);
            snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }
    }
}

