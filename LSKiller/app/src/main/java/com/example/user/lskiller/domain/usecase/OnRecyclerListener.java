package com.example.user.lskiller.domain.usecase;

import java.util.List;

import twitter4j.MediaEntity;
import twitter4j.Status;

/**
 * Created by USER on 2016/10/05.
 * RecyclerView用の独自クリックリスナー
 */
public interface OnRecyclerListener {
    void onRecyclerClicked(String tag, List<Status> statuses, int position);
    void onRecyclerClicked(String tag, MediaEntity[] url, int position);
}
