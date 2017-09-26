package com.excercise.nns.androidex.viewmodel;

import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.support.v4.widget.SwipeRefreshLayout;

import com.excercise.nns.androidex.contract.TimelineContract;
import com.excercise.nns.androidex.model.entity.TwitterStatus;
import com.excercise.nns.androidex.model.usecase.TimelineUseCase;
import com.excercise.nns.androidex.utils.TwitterUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import twitter4j.Status;
import twitter4j.Twitter;

/**
 * Created by nns on 2017/09/25.
 */
@BindingMethods({
        @BindingMethod(type = SwipeRefreshLayout.class, attribute = "android:onRefresh", method = "setOnRefreshListener")})
public class TimelineViewModel {
    private Twitter twitter;
    private TimelineContract contract;
    private List<TwitterStatus> statuses = Collections.emptyList();

    public TimelineViewModel(
            Twitter twitter,
            TimelineContract contract) {
        this.twitter = twitter;
        this.contract = contract;
    }

    public void loadTimeline() {
        contract.loadingTimeline();
        TimelineUseCase useCase = new TimelineUseCase(twitter);
        // TODO: 2017/09/18 最下部までスクロールでページ再読み込み
        useCase.getHomeTimeline(40)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Status>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {}

                    @Override
                    public void onNext(@NonNull List<Status> result) {
                        if(result != null) {
                            statuses = new ArrayList<>();
                            for(Status status : result) {
                                TwitterStatus st = TwitterUtils.getStatus(status);
                                statuses.add(st);
                            }
                        } else {
                            contract.getTimelineFailed("タイムラインの取得に失敗しました\n" +
                                    "時間を置いてから再度起動してください");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        contract.getTimelineFailed("タイムラインの取得に失敗しました\n" +
                                "時間を置いてから再度起動してください");
                    }

                    @Override
                    public void onComplete() {
                        contract.getTimelineSuccess(statuses);
                    }
                });
    }

    public void onRefresh() {
        loadTimeline();
    }
}
