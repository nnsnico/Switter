package com.excercise.nns.androidex.utils;

import android.util.Log;

import com.excercise.nns.androidex.data.Token;
import com.excercise.nns.androidex.data.Token_Table;
import com.excercise.nns.androidex.model.entity.TwitterStatus;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.URLEntity;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.util.TimeSpanConverter;

/**
 * Created by nns on 2017/07/12.
 */

public class TwitterUtils {

    private static TimeSpanConverter time = new TimeSpanConverter(Locale.JAPAN);

    public static Twitter getTwitterInstance() {
        ConfigurationBuilder config = new ConfigurationBuilder();
        config.setTweetModeExtended(true);
        TwitterFactory factory = new TwitterFactory(config.build());
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

    public static TwitterStatus getStatus(Status status, URLEntity[] entities) {
        TwitterStatus tStatus = new TwitterStatus();
        Status result;
        if(status.isRetweet()) {
            result = status.getRetweetedStatus();
            tStatus.setRetweet(status.getUser().getName());
        } else {
            result = status;
        }
        String refactoredText = status.getText();
        tStatus.setId(result.getUser().getId());
        tStatus.setProfileImageUrl(result.getUser().getOriginalProfileImageURL());
        tStatus.setName(result.getUser().getName());
        tStatus.setScreenName(result.getUser().getScreenName());
        tStatus.setCreatedTime(time.toTimeSpanString(result.getCreatedAt()));
        for (URLEntity entity : entities) {
            refactoredText = refactorTweetText(status.getText(), entity.getURL(), entity.getExpandedURL());
        }
        if(result.getMediaEntities().length > 0) {
            for (MediaEntity entity : result.getMediaEntities()) {
                refactoredText = refactorTweetText(refactoredText, entity.getURL(), "");
            }
            tStatus.setMediaImageUrl(result.getMediaEntities());
        } else {
            tStatus.setMediaImageUrl(null);
        }
        Log.d("text", refactoredText);
        tStatus.setTweetText(refactoredText);
        return tStatus;
    }

    private static String refactorTweetText(String text, String originalText, String replaceText) {
        Pattern pattern = Pattern.compile(originalText);
        Matcher matcher = pattern.matcher(text);
        return matcher.replaceAll(replaceText);
    }
}
