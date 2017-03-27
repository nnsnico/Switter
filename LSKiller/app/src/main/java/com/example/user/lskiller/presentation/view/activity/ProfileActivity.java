package com.example.user.lskiller.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.BoolRes;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.user.lskiller.R;
import com.example.user.lskiller.data.repository.TweetRepository;
import com.example.user.lskiller.domain.usecase.EndlessScrollListener;
import com.example.user.lskiller.domain.usecase.FavoriteAsync;
import com.example.user.lskiller.domain.usecase.MuteAsync;
import com.example.user.lskiller.domain.usecase.OnRecyclerListener;
import com.example.user.lskiller.domain.usecase.ReTweetAsync;
import com.example.user.lskiller.domain.usecase.TimeLineAsync;
import com.example.user.lskiller.presentation.view.component.DividerItemDecoration;
import com.example.user.lskiller.presentation.view.util.TwitterUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

import static com.example.user.lskiller.R.id.collapsingToolbar;
import static com.example.user.lskiller.R.id.recyclerView;

/**
 * Created by USER on 2017/02/10.
 */
public class ProfileActivity extends AppCompatActivity
        implements OnRecyclerListener, TweetRepository {

    private Toolbar toolbar;
    private RecyclerView recycler;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Twitter mTwitter;
    private Status status;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // slide transitional animation
        getWindow().setEnterTransition(new Slide());
        // bind
        mTwitter = TwitterUtils.getTwitterInstance(this);
        FloatingActionButton screenImg = (FloatingActionButton) findViewById(R.id.screen_image);
        TextView detailProfile = (TextView) findViewById(R.id.detail_profile);
        CollapsingToolbarLayout collapToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarConfig();
        // get Status
        status = (Status) getIntent().getSerializableExtra("status");
        user = status.getUser();
        // Title
        collapToolbar.setTitle(String.format("%s @%s",
                user.getName(),
                user.getScreenName()));
        detailProfile.setText(user.getDescription());
        Picasso.with(this)
                .load(user.getOriginalProfileImageURL())
                .into(screenImg);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTimeline();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        recycler = (RecyclerView) findViewById(recyclerView);
        recycler.setLayoutManager(new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        ));
        recycler.addItemDecoration(new DividerItemDecoration(this));
        getTimeline();
        // 最短で追加読み込み
        recycler.addOnScrollListener(new EndlessScrollListener(
                (LinearLayoutManager) recycler.getLayoutManager()) {
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

    private void toolbarConfig() {
        toolbar.inflateMenu(R.menu.profile_toolbar_item);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    // TODO ミュート機能、ブロック追加
                    case R.id.mute:
//                        AsyncTask<Long, Void, Boolean> task = new MuteAsync(
//                                ProfileActivity.this,
//                                ProfileActivity.this,
//                                status
//                        );
//                        task.execute();
                        break;
                    case R.id.block:
                        try {
                            mTwitter.createBlock(status.getId());
                        } catch (TwitterException e) {
                            e.printStackTrace();
                        }
                        break;
                    // リツイート非表示できるのか
                    case R.id.mute_ret:
//                        mTwitter
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void getTimeline() {
        AsyncTask<Void, Void, List<Status>> task = new TimeLineAsync(
                mTwitter,
                this,
                recycler,
                user,
                true
        );
        task.execute();
    }

    @Override
    public void getTimeline(int currentPage) {
        AsyncTask<Void, Void, List<Status>> task = new TimeLineAsync(
                mTwitter,
                this,
                recycler,
                currentPage,
                user,
                true
        );
        task.execute();
    }

    @Override
    public void onRecyclerClicked(String tag, ArrayList<Status> statuses, int position) {
        switch (tag) {
            case "goPro":
                if (statuses.get(position).isRetweet()) {
                    Intent intentPro = new Intent(this, ProfileActivity.class);
                    intentPro.putExtra("status", statuses.get(position).getRetweetedStatus());
                    startIntent(intentPro);
                }
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
    public void onRecyclerClicked(String tag, MediaEntity[] url, int position) {
        if (Objects.equals(tag, "img")) {
            Intent intent = new Intent(this, ImageViewerActivity.class);
            intent.putExtra("media", url);
            intent.putExtra("position", position);
            startIntent(intent);
        }
    }

    public void startIntent(Intent intent) {
        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(
                this, (Pair<View, String>[]) null).toBundle()
        );
    }

    public boolean isMuted(Status status) {
        try {
            return mTwitter.createMute(status.getId()) != null;
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return false;
    }
}
