package com.excercise.nns.androidex.contract;

import android.content.Intent;

/**
 * Created by nns on 2017/06/11.
 */

public interface OAuthContract {
    void RequestSuccessful(Intent intent);
    void RequestFailure();
    void OAuthSuccessful();
    void OAuthFailure(String message);
}
