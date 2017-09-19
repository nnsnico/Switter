package com.excercise.nns.androidex.contract;

import com.excercise.nns.androidex.model.entity.TwitterStatus;

import java.util.List;

import twitter4j.Status;

/**
 * Created by nns on 2017/06/19.
 */

public interface TimelineContract {
    void onStartOAuth();

    void onStartTweet();

    void onStartAbout();

    void getTimelineFailed(String error);

    void getTimelineSuccess(List<TwitterStatus> statuses);

    void postFavoriteSuccess(String message);

    void postActionFailed(String error);

    void setProgress();
}