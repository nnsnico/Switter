package com.example.user.lskiller.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user.lskiller.AsyncTask.FavoriteAsync;
import com.example.user.lskiller.AsyncTask.ReTweetAsync;
import com.example.user.lskiller.AsyncTask.TimeLineAsync;
import com.example.user.lskiller.RecyclerItem.DividerItemDecoration;
import com.example.user.lskiller.Listener.EndlessScrollListener;
import com.example.user.lskiller.Listener.OnRecyclerListener;
import com.example.user.lskiller.R;
import com.example.user.lskiller.Utils.TwitterUtils;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import icepick.Icepick;
import icepick.State;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.Twitter;

public class TimelineActivity extends AppCompatActivity implements OnRecyclerListener {

    private Twitter mTwitter;
    private Toolbar toolbar;
    private List<Status> statuses = new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // restore State in before
//        Icepick.restoreInstanceState(this, savedInstanceState);

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
            getTimeLine();
            // endScrollListener
            recyclerView.addOnScrollListener(new EndlessScrollListener(
                    (LinearLayoutManager) recyclerView.getLayoutManager()) {
                @Override
                public void onLoadMore(int current_page) {
                    getTimeLine(current_page);
                    Toast.makeText(TimelineActivity.this, "読み込み中・・・", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    // トップスワイプアクション
    private void swipeLayoutConfig() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTimeLine();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void toolbarConfig() {
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite));

        toolbar.inflateMenu(R.menu.toolbar_item);
        // ToolBarのアイテム取得
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_refresh:
                        getTimeLine();
                        return true;
                    case R.id.menu_tweet:
                        Intent intent = new Intent(TimelineActivity.this, TweetActivity.class);
                        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(
                                TimelineActivity.this,
                                (Pair<View, String>[]) null
                        ).toBundle());
                        getTimeLine();
                        return true;
                    case R.id.AboutApp:
                        Intent intent2 = new Intent(TimelineActivity.this, AboutAppActivity.class);
                        startActivity(intent2, ActivityOptionsCompat.makeSceneTransitionAnimation(
                                TimelineActivity.this,
                                (Pair<View, String>[]) null
                        ).toBundle());
                }
                return true;
            }
        });
    }

    public void getTimeLine() {
        AsyncTask<Void, Void, List<twitter4j.Status>> task = new TimeLineAsync(
                mTwitter,
                statuses,
                TimelineActivity.this,
                recyclerView);
        task.execute();
    }

    public void getTimeLine(int currentPage) {
        AsyncTask<Void, Void, List<twitter4j.Status>> task = new TimeLineAsync(
                mTwitter,
                statuses,
                TimelineActivity.this,
                recyclerView,
                currentPage
        );
        task.execute();
    }

    @Override
    public void onRecyclerClicked(String tag, final List<Status> statuses, final int position) {
        switch (tag) {
            case "fav":
                AsyncTask<Long, Void, Boolean> favoriteTask = new FavoriteAsync(
                        TimelineActivity.this,
                        TimelineActivity.this,
                        statuses,
                        position
                );
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
                Intent intent = new Intent(TimelineActivity.this, ReplyActivity.class);
                intent.putExtra("screenName", statuses.get(position).getUser().getScreenName());
                intent.putExtra("status", statuses.get(position).getId());
                startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(
                        TimelineActivity.this,
                        (Pair<View, String>[]) null
                ).toBundle());
                break;
        }
        getTimeLine();
    }

    @Override
    public void onRecyclerClicked(
            String tag, MediaEntity[] mediaEntity, int position) {
        if (Objects.equals(tag, "img")) {
            Intent intent = new Intent(TimelineActivity.this, ImageViewerActivity.class);
            intent.putExtra("media", mediaEntity);
            intent.putExtra("position", position);
            startActivity(intent, ActivityOptionsCompat.
                    makeSceneTransitionAnimation(this, (Pair<View, String>[]) null).toBundle());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // TODO Icepickによるelementの保存
//        Icepick.saveInstanceState(this, outState);
//        RecyclerAdapter adapter = new RecyclerAdapter(
//                this,
//                statuses,
//                this
//        );
//        recyclerView.setAdapter(adapter);
    }
}
