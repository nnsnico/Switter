package com.excercise.nns.switter.contract;

import com.excercise.nns.switter.model.entity.TwitterStatus;

/**
 * Created by nns on 2017/07/15.
 */

public interface OnRecyclerListener {
    void onSwipeItemClick(String tag, TwitterStatus status);
}
