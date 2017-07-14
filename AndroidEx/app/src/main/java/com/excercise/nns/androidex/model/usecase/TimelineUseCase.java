package com.excercise.nns.androidex.model.usecase;

import java.util.List;

import io.reactivex.Observable;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * Created by nns on 2017/07/11.
 */

public class TimelineUseCase {
    private Twitter twitter;

    public TimelineUseCase(Twitter twitter) {
        this.twitter = twitter;
    }

    public Observable<List<Status>> getHomeTimeline(int endPage) {
        Paging page = new Paging(1, endPage);
        return Observable.create(e -> {
            try {
                e.onNext(twitter.getHomeTimeline(page));
                e.onComplete();
            } catch (TwitterException te) {
                e.onError(te);
            }
        });
    }

    public Observable<List<Status>> getUserTimeline(User user, int endPage) {
        Paging page = new Paging(1, endPage);
        return Observable.create(e -> {
           try {
               e.onNext(twitter.getUserTimeline(user.getId(), page));
               e.onComplete();
           } catch (TwitterException te) {
               e.onError(te);
           }
        });
    }
}
