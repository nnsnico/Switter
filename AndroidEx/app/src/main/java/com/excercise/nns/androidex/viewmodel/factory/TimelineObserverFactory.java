package com.excercise.nns.androidex.viewmodel.factory;

import com.excercise.nns.androidex.contract.TimelineContract;
import com.excercise.nns.androidex.model.entity.TwitterStatus;
import com.excercise.nns.androidex.utils.TwitterUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import twitter4j.Status;
import twitter4j.URLEntity;

/**
 * Created by nns on 2017/07/16.
 */

public class TimelineObserverFactory {

    private TimelineContract contract;
    private ArrayList<TwitterStatus> statuses = new ArrayList<>();

    public TimelineObserverFactory(TimelineContract contract) {
        this.contract = contract;
    }

    public Observer<List<Status>> getTimelineObserver() {
        return new Observer<List<Status>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {}

            @Override
            public void onNext(@NonNull List<Status> result) {
                contract.setProgress();
                if(result != null) {
                    statuses = new ArrayList<>();
                    for(Status status : result) {
                        URLEntity[] entities = status.getURLEntities();
                        TwitterStatus st = TwitterUtils.getStatus(status, entities);
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
        };
    }
}
