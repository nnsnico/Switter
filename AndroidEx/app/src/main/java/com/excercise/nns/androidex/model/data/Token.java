package com.excercise.nns.androidex.model.data;

import io.realm.RealmObject;

/**
 * Created by nns on 2017/06/19.
 */

public class Token extends RealmObject {
    private String accessToken;
    private String tokenSecret;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    public boolean hasAccessToken() {
        return tokenSecret != null && accessToken != null;
    }
}
