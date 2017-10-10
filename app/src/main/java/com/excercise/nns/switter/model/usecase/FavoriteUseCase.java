package com.excercise.nns.switter.model.usecase;

import com.excercise.nns.switter.model.entity.TwitterStatus;

import io.reactivex.Observable;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by nns on 2017/07/28.
 */

public class FavoriteUseCase {
    private Twitter twitter;

    public FavoriteUseCase(Twitter twitter) {
        this.twitter = twitter;
    }

    public Observable<Boolean> postFavorite(TwitterStatus status) {
        return Observable.create(e -> {
            try {
                if (status.isFavorited) {
                    // ふぁぼを消してisFavoriteをfalseに
                    twitter.destroyFavorite(status.getId());
                    e.onNext(false);
                } else {
                    // ふぁぼしてisFavoriteをtrueに
                    twitter.createFavorite(status.getId());
                    e.onNext(true);
                }
                e.onComplete();
            } catch (TwitterException te) {
                e.onError(te);
            }
        });
    }
}
