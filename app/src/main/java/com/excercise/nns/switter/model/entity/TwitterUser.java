package com.excercise.nns.androidex.model.entity;

import java.io.Serializable;

/**
 * Created by nns on 2017/10/01.
 */

public class TwitterUser implements Serializable {
    private long Id;
    private String profileImageUrl;
    private String name;
    private String screenName;
    private String profileDetail;

    public void setId(long id) {
        Id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void setProfileDetail(String profileDetail) {
        this.profileDetail = profileDetail;
    }

    public long getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileDetail() {
        return profileDetail;
    }
}
