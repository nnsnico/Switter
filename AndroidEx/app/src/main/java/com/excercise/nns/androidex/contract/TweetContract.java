package com.excercise.nns.androidex.contract;

/**
 * Created by nns on 2017/07/15.
 */

public interface TweetContract {
    void onTweetSuccess();
    void onTweetFailed();
    void onSendingTweet();
    void setTweetCount(int count);
}
