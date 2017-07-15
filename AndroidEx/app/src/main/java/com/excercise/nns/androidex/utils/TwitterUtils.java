package com.excercise.nns.androidex.utils;

import com.excercise.nns.androidex.model.data.Token;
import com.excercise.nns.androidex.model.data.Token_Table;
import com.excercise.nns.androidex.model.entity.TwitterStatus;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.Locale;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.util.TimeSpanConverter;

/**
 * Created by nns on 2017/07/12.
 */

public class TwitterUtils {

    private static TimeSpanConverter time = new TimeSpanConverter(Locale.JAPAN);

    public static Twitter getTwitterInstance() {
        TwitterFactory factory = new TwitterFactory();
        Twitter twitter = factory.getInstance();
        String consumerKey = "7fd5ZNhqoElIzsd8eAIqyQK7B";
        String consumerSecret = "o2hZaNoZRoRPCz9zMI8C4Q5QKQWsMI6uZq3GmieAVGRwPeBoYJ";
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
        Token token = SQLite.select().from(Token.class).where(Token_Table.id.is(1)).querySingle();
        if(token != null) {
            twitter.setOAuthAccessToken(new AccessToken(token.getAccessToken(), token.getTokenSecret()));
        } else {
            twitter = null;
        }
        return twitter;
    }

    public static TwitterStatus getStatus(Status status) {
        TwitterStatus tStatus = new TwitterStatus();
        Status result;
        if(status.isRetweet()) {
            result = status.getRetweetedStatus();
            tStatus.setRetweet(status.getUser().getName());
        } else {
            result = status;
        }
        tStatus.setId(result.getUser().getId());
        tStatus.setProfileImageUrl(result.getUser().getOriginalProfileImageURL());
        tStatus.setName(result.getUser().getName());
        tStatus.setScreenName(result.getUser().getScreenName());
        tStatus.setTweetText(result.getText());
        tStatus.setCreatedTime(time.toTimeSpanString(result.getCreatedAt()));
        if(result.getMediaEntities().length > 0) {
            tStatus.setMediaImageUrl(result.getExtendedMediaEntities());
        } else {
            tStatus.setMediaImageUrl(null);
        }
        return tStatus;
    }
}
