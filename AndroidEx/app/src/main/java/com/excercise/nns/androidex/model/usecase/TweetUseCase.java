package com.excercise.nns.androidex.model.usecase;

import io.reactivex.Observable;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by nns on 2017/07/15.
 */

public class TweetUseCase {

    private Twitter twitter;

    public TweetUseCase(Twitter twitter) {
        this.twitter = twitter;
    }

    public Observable<Status> tweet(String message) {
        return Observable.create(e -> {
            StatusUpdate statusUpdate = new StatusUpdate(message);
            try {
                // TODO: upload medias
                e.onNext(twitter.updateStatus(statusUpdate));
                e.onComplete();
            } catch (TwitterException te) {
                e.onError(te);
            }
        });
    }

    // TODO: 2017/07/15 reply()

}
