package com.excercise.nns.androidex.model.usecase;

import com.excercise.nns.androidex.model.entity.TwitterStatus;

import io.reactivex.Observable;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by nns on 2017/09/19.
 */

public class RetweetUseCase {
    private Twitter twitter;

    public RetweetUseCase(Twitter twitter) {
        this.twitter = twitter;
    }

    public Observable<Boolean> getRetweetUseCase(TwitterStatus status) {
        return Observable.create(e -> {
            try {
                if (status.isRetweeted) {
                    if (!status.isRetweetedByMe) {
                        twitter.destroyStatus(status.getId());
                        twitter.updateStatus(status.getTweetText());
                    } else {
                        twitter.destroyStatus(status.getCurrentRetweetId());
                    }
                    e.onNext(false);
                } else {
                    twitter.retweetStatus(status.getId());
                    e.onNext(true);
                }
                e.onComplete();
            } catch (TwitterException te) {
                e.onError(te);
            }
        });
    }
}
