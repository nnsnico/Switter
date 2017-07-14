package com.excercise.nns.androidex.utils;

import com.excercise.nns.androidex.model.entity.TwitterStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import twitter4j.Status;
import twitter4j.util.TimeSpanConverter;

/**
 * Created by nns on 2017/07/12.
 */

public class TwitterUtils {

    private static TimeSpanConverter time = new TimeSpanConverter(Locale.JAPAN);

    public static TwitterStatus getStatus(Status status) {
        TwitterStatus tStatus = new TwitterStatus();
        Status result;
        if(status.isRetweet()) {
            result = status.getRetweetedStatus();
            tStatus.setRetweet(status.getUser().getName());
        } else {
            result = status;
        }
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
