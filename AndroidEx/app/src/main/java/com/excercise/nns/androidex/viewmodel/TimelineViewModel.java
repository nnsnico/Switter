package com.excercise.nns.androidex.viewmodel;

import android.content.Context;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.excercise.nns.androidex.R;
import com.excercise.nns.androidex.contract.TimelineContract;
import com.excercise.nns.androidex.model.usecase.TimelineUseCase;
import com.excercise.nns.androidex.utils.TwitterUtils;
import com.excercise.nns.androidex.viewmodel.factory.TimelineObserverFactory;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import twitter4j.Status;
import twitter4j.Twitter;

/**
 * Created by nns on 2017/06/19.
 */

@BindingMethods({
        @BindingMethod(type = Toolbar.class, attribute = "android:onMenuItemClick", method = "setOnMenuItemClickListener"),
        @BindingMethod(type = SwipeRefreshLayout.class, attribute = "android:onRefresh", method = "setOnRefreshListener")})
public class TimelineViewModel {
    private TimelineContract contract;
    private Twitter twitter;
    private TimelineObserverFactory factory;

    public TimelineViewModel(
            Twitter twitter,
            TimelineContract contract) {
        this.twitter = twitter;
        this.contract = contract;
        factory = new TimelineObserverFactory(contract);
        if (twitter == null) {
            contract.onStartOAuth();
        }
        // get timeline
        loadTimeline();
    }

    private void loadTimeline() {
        TimelineUseCase useCase = new TimelineUseCase(twitter);
        Observer<List<Status>> observer = factory.getTimelineObserver();
        useCase.getHomeTimeline(40)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public boolean onMenuClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                loadTimeline();
                return true;
            case R.id.menu_tweet:
                contract.onStartTweet();
                return true;
            case R.id.AboutApp:
                contract.onStartAbout();
                return true;
            default:
                break;
        }
        return false;
    }

    public void onRefresh() {
        loadTimeline();
    }
}
