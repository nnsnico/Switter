package com.example.user.lskiller.data.repository;

/**
 * Created by USER on 2017/02/10.
 */

public interface TweetRepository {
    void getTimeline();
    void getTimeline(int currentPage);
}
