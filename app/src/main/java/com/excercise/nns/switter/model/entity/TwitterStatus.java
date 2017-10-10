package com.excercise.nns.switter.model.entity;

import java.io.Serializable;

import twitter4j.MediaEntity;

/**
 * Created by nns on 2017/07/12.
 */

public class TwitterStatus implements Serializable {
    private long id;
    private long currentRetweetId;
    private TwitterUser user;
    private String createdTime;
    private String tweetText;
    private MediaEntity[] mediaImageUrl;
    private String retweet = "";
    public boolean isFavorited = false;
    public boolean isRetweeted = false;
    public boolean isRetweetedByMe = false;

    public void setId(long id) {
        this.id = id;
    }

    public void setCurrentRetweetId(long currentRetweetId) {
        this.currentRetweetId = currentRetweetId;
    }

    public void setUser(TwitterUser user) {
        this.user = user;
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

    public long getCurrentRetweetId() {
        return currentRetweetId;
    }

    public TwitterUser getUser() {
        return user;
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
