package com.example.user.lskiller.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.user.lskiller.FavoriteAsync;
import com.example.user.lskiller.ReTweetAsync;
import com.example.user.lskiller.TimeLineAsync;
import com.example.user.lskiller.CustomRecycler.DividerItemDecoration;
import com.example.user.lskiller.Listener.OnRecyclerListener;
import com.example.user.lskiller.R;
import com.example.user.lskiller.Utils.TwitterUtils;
import com.example.user.lskiller.adapter.RecyclerAdapter;

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
    private RecyclerAdapter adapter;
//    private SwipeLayout swipeLayout;
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
//        swipeLayout = (SwipeLayout)findViewById(R.id.swipeMenu);
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

//            SwipeMenuConfig();
            reloadTimeLine();
        }
    }

    // トップスワイプアクション
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

    public void reloadTimeLine() {
        AsyncTask<Void, Void, List<twitter4j.Status>> task = new TimeLineAsync(
                mTwitter,
                statuses,
                TimelineActivity.this,
                recyclerView);

        task.execute();
    }

    @Override
    public void onRecyclerClicked(String tag,final List<Status> statuses,final int position) {
        switch (tag){
            case "fav":
                AsyncTask<Long, Void, Boolean> favoriteTask = new FavoriteAsync(
                        TimelineActivity.this,
                        TimelineActivity.this,
                        statuses,
                        position);
                favoriteTask.execute();
                break;
            case "ret":
                AsyncTask<Long, Void, Boolean> reTweetTask = new ReTweetAsync(
                        TimelineActivity.this,
                        TimelineActivity.this,
                        statuses,
                        position
                );
                reTweetTask.execute();
                break;
            case "rep":
                ContentValues content = new ContentValues();
                Intent intent = new Intent(TimelineActivity.this, ReplyActivity.class);
                intent.putExtra("screenName", statuses.get(position).getUser().getScreenName());
                intent.putExtra("status", statuses.get(position).getUser().getId());
                startActivity(intent);
                break;
        }
        reloadTimeLine();
    }
}
