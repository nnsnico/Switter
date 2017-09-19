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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.excercise.nns.androidex.R;
import com.excercise.nns.androidex.contract.OnRecyclerListener;
import com.excercise.nns.androidex.contract.TimelineContract;
import com.excercise.nns.androidex.databinding.ActivityTimelineBinding;
import com.excercise.nns.androidex.model.entity.TwitterStatus;
import com.excercise.nns.androidex.utils.TwitterUtils;
import com.excercise.nns.androidex.view.adapter.RecyclerAdapter;
import com.excercise.nns.androidex.view.component.RecyclerDivider;
import com.excercise.nns.androidex.viewmodel.TimelineViewModel;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.util.Collections;
import java.util.List;

import twitter4j.Twitter;

/**
 * Created by nns on 2017/06/12.
 */

public class TimelineActivity extends AppCompatActivity implements TimelineContract, OnRecyclerListener {

    private ActivityTimelineBinding binding;
    private RecyclerAdapter adapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, TimelineActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FlowManager.init(this);

        Twitter twitter = TwitterUtils.getTwitterInstance(this);
        binding =
                DataBindingUtil.setContentView(this, R.layout.activity_timeline);
        TimelineViewModel viewModel = new TimelineViewModel(twitter, this);
        // toolbar setup
        binding.toolbar.setTitle(R.string.app_name);
        binding.toolbar.inflateMenu(R.menu.toolbar_item);
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        // recyclerView setup
        List<TwitterStatus> statuses = Collections.emptyList();
        adapter = new RecyclerAdapter(statuses);
        binding.recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.recyclerView.addItemDecoration(new RecyclerDivider(this));
        binding.recyclerView.setAdapter(adapter);
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
    public void loadingTimeline() {
        binding.swipeLayout.setRefreshing(false);
        binding.setIsloaded(true);
    }

    @Override
    public void getTimelineFailed(String error) {
        binding.setIsloaded(false);
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
        adapter.setStatuses(statuses);
        adapter.notifyDataSetChanged();
        binding.setIsloaded(false);
        Parcelable state = binding.recyclerView.getLayoutManager().onSaveInstanceState();
        binding.recyclerView.getLayoutManager().onRestoreInstanceState(state);
    }

    @Override
    public void postFavoriteSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void postActionFailed(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setProgress() {
        binding.progress.setVisibility(ProgressBar.VISIBLE);
    }

    @Override
    public void onSwipeItemClick(String tag, TwitterStatus status) {
        switch (tag) {
            case "goPro":
                break;
            case "reply":
                TweetActivity.start(this, status.getScreenName(), status.getId());
                break;
            default:
                break;
        }
    }
}
