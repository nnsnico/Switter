package com.excercise.nns.switter.contract;

/**
 * Created by nns on 2017/07/15.
 */

public interface TweetContract {
    void onTweetSuccess();
    void onTweetFailed();
    void onSendingTweet();
    void setTweetCount(int count, int screenNameCount);
    void setReplyUser(String screenName);
}
