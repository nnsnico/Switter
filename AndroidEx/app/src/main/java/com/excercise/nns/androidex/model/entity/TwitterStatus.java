package com.excercise.nns.androidex.model.entity;

import twitter4j.MediaEntity;

/**
 * Created by nns on 2017/07/12.
 */

public class TwitterStatus {
    private long id;
    private String profileImageUrl;
    private String name;
    private String screenName;
    private String createdTime;
    private String tweetText;
    private MediaEntity[] mediaImageUrl;
    private String retweet = "";
    public boolean isFavorited = false;

    public void setId(long id) {
        this.id = id;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void setCreatedTime(String createTime) {
        this.createdTime = createTime;
    }

    public void setTweetText(String tweetText) {
        this.tweetText = tweetText;
    }

    public void setMediaImageUrl(MediaEntity[] mediaImageUrl) {
        this.mediaImageUrl = mediaImageUrl;
    }

    public void setRetweet(String retweet) {
        this.retweet = retweet + "がリツイートしました";
    }

    public long getId() {
        return id;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public String getTweetText() {
        return tweetText;
    }

    public MediaEntity[] getMediaImageUrls() {
        return mediaImageUrl;
    }

    public String getRetweet() {
        return retweet;
    }
}
