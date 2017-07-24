package com.excercise.nns.androidex.viewmodel.factory;

import com.excercise.nns.androidex.contract.TweetContract;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import twitter4j.Status;

/**
 * Created by nns on 2017/07/16.
 */

public class TweetObserverFactory {

    private TweetContract contract;

    public TweetObserverFactory(TweetContract contract) {
        this.contract = contract;
    }

    public Observer<Status> getTweetObserver() {
        return new Observer<Status>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull Status update) {
            }

            @Override
            public void onError(@NonNull Throwable e) {
                contract.onTweetFailed();
            }

            @Override
            public void onComplete() {
                contract.onTweetSuccess();
            }
        };
    }


}
