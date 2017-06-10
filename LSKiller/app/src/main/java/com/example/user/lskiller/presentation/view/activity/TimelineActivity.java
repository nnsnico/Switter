package com.example.user.lskiller.presentation.view.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.user.lskiller.data.repository.TweetRepository;
import com.example.user.lskiller.domain.usecase.FavoriteAsync;
import com.example.user.lskiller.domain.usecase.ReTweetAsync;
import com.example.user.lskiller.domain.usecase.TimeLineAsync;
import com.example.user.lskiller.presentation.view.component.DividerItemDecoration;
import com.example.user.lskiller.domain.usecase.EndlessScrollListener;
import com.example.user.lskiller.domain.usecase.OnRecyclerListener;
import com.example.user.lskiller.R;
import com.example.user.lskiller.presentation.view.util.TwitterUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.Twitter;

public class TimelineActivity extends AppCompatActivity
        implements OnRecyclerListener, TweetRepository {

    private Twitter mTwitter;
    private Toolbar toolbar;
    private ArrayList<Status> statuses = new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // restore State in before
//        Icepick.restoreInstanceState(this, savedInstanceState);

        setContentView(R.layout.activity_timeline_material);

        toolbar = (Toolbar) findViewById(R.id.toolbar_material);
        toolbarConfig();
        // SwipeLayoutConfig
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            getTimeline();
            mSwipeRefreshLayout.setRefreshing(false);
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // Twitterのトークンが取得されているか
        if (!TwitterUtils.hasAccessToken(this)) {
            Intent intent = new Intent(this, TwitterOAuthActivity.class);
            startIntent(intent);
            finish();
        } else {
            mTwitter = TwitterUtils.getTwitterInstance(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(
                    this,
                    LinearLayoutManager.VERTICAL,
                    false
            ));
            recyclerView.addItemDecoration(new DividerItemDecoration(this));
            getTimeline();
            // 最短で追加読み込み
            recyclerView.addOnScrollListener(new EndlessScrollListener(
                    (LinearLayoutManager) recyclerView.getLayoutManager()) {
                @Override
                public void onLoadMore(int current_page) {
                    getTimeline(current_page);
                    final CoordinatorLayout layout
                            = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
                    Snackbar snackbar;
                    snackbar = Snackbar.make(
                            layout,
                            "読み込み中・・・",
                            Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            });
        }
    }

    private void toolbarConfig() {
        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(R.menu.toolbar_item);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        // ToolBarのアイテム取得
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_refresh:
                    getTimeline();
                    return true;
                case R.id.menu_tweet:
                    Intent intent = new Intent(TimelineActivity.this, TweetActivity.class);
                    startIntent(intent);
                    return true;
                case R.id.AboutApp:
                    Intent intent2 = new Intent(TimelineActivity.this, AboutAppActivity.class);
                    startIntent(intent2);
            }
            return true;
        });
    }

    @Override
    public void getTimeline() {
        AsyncTask<Void, Void, List<Status>> task = new TimeLineAsync(
                mTwitter,
                this,
                recyclerView,
                null,
                false
        );
        task.execute();
    }

    @Override
    public void getTimeline(int currentPage) {
        AsyncTask<Void, Void, List<Status>> task = new TimeLineAsync(
                mTwitter,
                this,
                recyclerView,
                currentPage,
                null,
                false
        );
        task.execute();
    }

    @Override
    public void onRecyclerClicked(String tag, final ArrayList<Status> statuses, final int position) {
        switch (tag) {
            case "goPro":
                Intent intentPro = new Intent(this, ProfileActivity.class);
                if (!statuses.get(position).isRetweet()) {
                    intentPro.putExtra("status", statuses.get(position));
                } else {
                    intentPro.putExtra("status", statuses.get(position).getRetweetedStatus());
                }
                startIntent(intentPro);
                break;
            case "fav":
                AsyncTask<Long, Void, Boolean> favoriteTask = new FavoriteAsync(
                        this,
                        this,
                        statuses,
                        position
                );
                favoriteTask.execute();
                break;
            case "ret":
                AsyncTask<Long, Void, Boolean> reTweetTask = new ReTweetAsync(
                        this,
                        this,
                        statuses,
                        position
                );
                reTweetTask.execute();
                break;
            case "rep":
                Intent intent = new Intent(this, ReplyActivity.class);
                intent.putExtra("screenName", statuses.get(position).getUser().getScreenName());
                intent.putExtra("status", statuses.get(position).getId());
                startIntent(intent);
                break;
        }
    }

    @Override
    public void onRecyclerClicked(
            String tag, MediaEntity[] mediaEntity, int position) {
        if (Objects.equals(tag, "img")) {
            Intent intent = new Intent(this, ImageViewerActivity.class);
            intent.putExtra("media", mediaEntity);
            intent.putExtra("position", position);
            startIntent(intent);
        }
    }

    public void startIntent(Intent intent) {
        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(
                this, (Pair<View, String>[]) null).toBundle()
        );
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // TODO IcepickによるElementの保存
//        Icepick.saveInstanceState(this, outState);
//        RecyclerAdapter adapter = new RecyclerAdapter(
//                this,
//                statuses,
//                this
//        );
//        recyclerView.setAdapter(adapter);
    }
}
