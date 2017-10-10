package com.excercise.nns.switter.model.usecase;

import io.reactivex.Observable;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Created by nns on 2017/06/11.
 */

public class OAuthUseCase {
    private RequestToken requestToken;
    private Twitter twitter;

    public OAuthUseCase(Twitter twitter) {
        this.twitter = twitter;
    }

    public Observable<String> getRequestToken() {
        return Observable.create(e -> {
            try {
                requestToken = twitter.getOAuthRequestToken();
                e.onNext(requestToken.getAuthorizationURL());
                e.onComplete();
            } catch (TwitterException te) {
                e.onError(te);
            }
        });
    }

    public Observable<AccessToken> readPinCode(String pin) {
        return Observable.create(e -> {
            try {
                if(requestToken != null) {
                    e.onNext(twitter.getOAuthAccessToken(requestToken, pin));
                    e.onComplete();
                }
            } catch (TwitterException te) {
                e.onError(te);
            }
        });
    }
}
