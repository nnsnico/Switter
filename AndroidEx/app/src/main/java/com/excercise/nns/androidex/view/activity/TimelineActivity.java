package com.excercise.nns.androidex.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.excercise.nns.androidex.R;
import com.excercise.nns.androidex.contract.OnRecyclerListener;
import com.excercise.nns.androidex.contract.TimelineContract;
import com.excercise.nns.androidex.databinding.ActivityTimelineBinding;
import com.excercise.nns.androidex.model.entity.TwitterStatus;
import com.excercise.nns.androidex.view.adapter.RecyclerAdapter;
import com.excercise.nns.androidex.view.component.RecyclerDivider;
import com.excercise.nns.androidex.viewmodel.TimelineViewModel;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.util.List;

/**
 * Created by nns on 2017/06/12.
 */

public class TimelineActivity extends AppCompatActivity implements TimelineContract, OnRecyclerListener {

    private ActivityTimelineBinding binding;

    public static void start(Context context) {
        Intent intent = new Intent(context, TimelineActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FlowManager.init(this);
        TimelineViewModel viewModel = new TimelineViewModel(this);
        binding =
                DataBindingUtil.setContentView(this, R.layout.activity_timeline);
        // toolbar setup
        binding.toolbar.setTitle(R.string.app_name);
        binding.toolbar.inflateMenu(R.menu.toolbar_item);
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        // recycler setup
        binding.recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.recyclerView.addItemDecoration(new RecyclerDivider(this));
        binding.setViewmodel(viewModel);
    }

    @Override
    public void onStartOAuth() {
        OAuthActivity.start(this);
        finish();
    }

    @Override
    public void onStartTweet() {
        TweetActivity.start(this, null, 0);
    }

    @Override
    public void onStartAbout() {
        AboutAppActivity.start(this);
    }

    @Override
    public void getTimelineFailed(String error) {
        final Snackbar snackbar =
                Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_INDEFINITE);
        View view = snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        snackbar.setAction("OK", view1 -> snackbar.dismiss());
        snackbar.show();
    }

    @Override
    public void getTimelineSuccess(List<TwitterStatus> statuses) {
        RecyclerAdapter adapter = new RecyclerAdapter(statuses, this);
        Parcelable state = binding.recyclerView.getLayoutManager().onSaveInstanceState();
        binding.recyclerView.getLayoutManager().onRestoreInstanceState(state);
        binding.recyclerView.setAdapter(adapter);
        binding.swipeLayout.setRefreshing(false);
    }

    @Override
    public void onSwipeItemClick(String tag, TwitterStatus status) {
        switch (tag) {
            case "goPro":
                break;
            case "reply":
                TweetActivity.start(this, status.getName(), status.getId());
                break;
            case "retweet":
                break;
            case "fav":
                break;
            default:
                break;
        }
    }
}
