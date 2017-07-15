package com.excercise.nns.androidex.contract;

import com.excercise.nns.androidex.model.entity.TwitterStatus;

/**
 * Created by nns on 2017/07/15.
 */

public interface OnRecyclerListener {
    void onSwipeItemClick(String tag, TwitterStatus status);
}
