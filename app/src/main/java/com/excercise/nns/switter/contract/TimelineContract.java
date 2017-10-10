package com.excercise.nns.switter.contract;

import com.excercise.nns.switter.model.entity.TwitterStatus;

import java.util.List;

/**
 * Created by nns on 2017/06/19.
 */

public interface TimelineContract {
    void loadingTimeline();

    void getTimelineFailed(String error);

    void getTimelineSuccess(List<TwitterStatus> statuses);

    void postActionSuccess(String message);

    void postActionFailed(String error);
}