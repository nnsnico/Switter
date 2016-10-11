package com.example.user.lskiller.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.user.lskiller.AsyncTimeLine;
import com.example.user.lskiller.CustomRecycler.DividerItemDecoration;
import com.example.user.lskiller.OnRecyclerListener;
import com.example.user.lskiller.R;
import com.example.user.lskiller.Utils.TwitterUtils;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;

public class TimelineActivity extends AppCompatActivity implements OnRecyclerListener {

    Twitter mTwitter;
    //    private ListView listView;
    private Toolbar toolbar;
    private List<Status> statuses = new ArrayList<Status>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_material);
        // ツールバー
        toolbar = (Toolbar) findViewById(R.id.toolbar_material);
        toolbarConfig();

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        swipeLayoutConfig();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // Twitterのトークンが取得されているか
        if (!TwitterUtils.hasAccessToken(this)) {
            Intent intent = new Intent(this, TwitterOAuthActivity.class);
            startActivity(intent);
            finish();
        } else {
            mTwitter = TwitterUtils.getTwitterInstance(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(
                    this,
                    LinearLayoutManager.VERTICAL,
                    false
            ));
            recyclerView.addItemDecoration(new DividerItemDecoration(this));
            reloadTimeLine();
        }
    }

    // スワイプアクション
    private void swipeLayoutConfig() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadTimeLine();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void toolbarConfig() {
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite));

        // ToolBarのアイテム取得
        toolbar.inflateMenu(R.menu.toolbar_item);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_refresh:
                        reloadTimeLine();
                        return true;
                    case R.id.menu_tweet:
                        Intent intent = new Intent(TimelineActivity.this, TweetActivity.class);
                        startActivity(intent);
                        reloadTimeLine();
                        return true;
                }
                return true;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent resultData){

    }

    public void reloadTimeLine() {
        AsyncTask<Void, Void, List<twitter4j.Status>> task = new AsyncTimeLine(
                mTwitter,
                statuses,
                TimelineActivity.this,
                recyclerView);

        task.execute();
    }

    @Override
    public void onRecyclerClicked(View v, int position) {

    }
}
