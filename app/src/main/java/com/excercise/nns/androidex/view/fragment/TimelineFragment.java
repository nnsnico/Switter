package com.excercise.nns.androidex.view.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.excercise.nns.androidex.R;
import com.excercise.nns.androidex.contract.OnRecyclerListener;
import com.excercise.nns.androidex.contract.TimelineContract;
import com.excercise.nns.androidex.contract.TimelineFragmentCallback;
import com.excercise.nns.androidex.databinding.FragmentTimelineBinding;
import com.excercise.nns.androidex.model.entity.TwitterStatus;
import com.excercise.nns.androidex.utils.TwitterUtils;
import com.excercise.nns.androidex.view.activity.TweetActivity;
import com.excercise.nns.androidex.view.adapter.RecyclerAdapter;
import com.excercise.nns.androidex.view.component.RecyclerDivider;
import com.excercise.nns.androidex.viewmodel.TimelineViewModel;

import java.util.Collections;
import java.util.List;

import twitter4j.Twitter;

/**
 * Created by nns on 2017/09/25.
 */

public class TimelineFragment extends Fragment implements TimelineContract, OnRecyclerListener {
    private FragmentTimelineBinding binding;
    private TimelineViewModel viewModel;
    private RecyclerAdapter adapter;
    private TimelineFragmentCallback callback;

    public static TimelineFragment newInstance() {
        return new TimelineFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TimelineFragmentCallback) {
            callback = (TimelineFragmentCallback) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_timeline, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Twitter twitter = TwitterUtils.getTwitterInstance(getContext());

        viewModel = new TimelineViewModel(twitter, this);
        binding.setViewModel(viewModel);

        // recyclerView setup
        List<TwitterStatus> statuses = Collections.emptyList();
        adapter = new RecyclerAdapter(statuses, this, this);
        binding.recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerView.addItemDecoration(new RecyclerDivider(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel.loadTimeline();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
        binding = null;
        viewModel = null;
        adapter = null;
    }

    @Override
    public void loadingTimeline() {
        binding.swipeLayout.setRefreshing(false);
        callback.loadingTimeline();
    }

    @Override
    public void getTimelineFailed(String error) {
        callback.finishedTimeline();
        final Snackbar snackbar =
                Snackbar.make(getActivity().findViewById(android.R.id.content), error, Snackbar.LENGTH_INDEFINITE);
        View view = snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        snackbar.setAction("OK", view1 -> snackbar.dismiss());
        snackbar.show();
    }

    @Override
    public void getTimelineSuccess(List<TwitterStatus> statuses) {
        callback.finishedTimeline();
        adapter.setStatuses(statuses);
        adapter.notifyDataSetChanged();
        Parcelable state = binding.recyclerView.getLayoutManager().onSaveInstanceState();
        binding.recyclerView.getLayoutManager().onRestoreInstanceState(state);
    }

    @Override
    public void postActionSuccess(String message) {
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        snackbar.show();
        viewModel.loadTimeline();
    }

    @Override
    public void postActionFailed(String error) {
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onSwipeItemClick(String tag, TwitterStatus status) {
        switch (tag) {
            case "goPro":
                // TODO: 2017/09/19 set ProfileActivity

                break;
            case "reply":
                TweetActivity.start(getContext(), status.getScreenName(), status.getId());
                break;
            default:
                break;
        }
    }
}
