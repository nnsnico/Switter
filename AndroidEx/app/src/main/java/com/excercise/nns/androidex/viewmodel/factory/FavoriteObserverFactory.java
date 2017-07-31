package com.excercise.nns.androidex.viewmodel.factory;

import com.excercise.nns.androidex.contract.TimelineContract;
import com.excercise.nns.androidex.model.entity.TwitterStatus;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by nns on 2017/07/28.
 */

public class FavoriteObserverFactory {
    private TimelineContract contract;

    public FavoriteObserverFactory(TimelineContract contract) {
        this.contract = contract;
    }

    public Observer<Boolean> getFavoriteObserver(TwitterStatus status) {
        return new Observer<Boolean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {}

            @Override
            public void onNext(@NonNull Boolean isFavorite) {
                status.isFavorited = isFavorite;
            }

            @Override
            public void onError(@NonNull Throwable e) {
                contract.postActionFailed("エラーが起こりました。もう一度行ってください。");
            }

            @Override
            public void onComplete() {
                contract.postFavoriteSuccess();
            }
        };
    }
}
