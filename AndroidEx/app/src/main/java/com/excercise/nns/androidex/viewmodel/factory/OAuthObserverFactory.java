package com.excercise.nns.androidex.viewmodel.factory;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.excercise.nns.androidex.contract.OAuthContract;
import com.excercise.nns.androidex.data.Token;
import com.excercise.nns.androidex.viewmodel.OAuthViewModel;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import twitter4j.auth.AccessToken;

/**
 * Created by nns on 2017/07/16.
 */

public class OAuthObserverFactory {

    private OAuthContract contract;
    private Intent intent;

    public OAuthObserverFactory(OAuthContract contract) {
        this.contract = contract;
    }

    public Observer<String> getRequestTokenObserver() {
        return new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {}

            @Override
            public void onNext(@NonNull String url) {
                if(url != null) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
                contract.RequestFailure();
            }

            @Override
            public void onComplete() {
                contract.RequestSuccessful(intent);
            }
        };
    }

    public Observer<AccessToken> getAccessTokenObserver() {
        return new Observer<AccessToken>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull AccessToken accessToken) {
                if (accessToken != null) {
                    // store accessToken by DBFlow
                    final Token token= new Token();
                    token.setAccessToken(accessToken.getToken());
                    token.setTokenSecret(accessToken.getTokenSecret());
                    token.save();
                } else {
                    contract.OAuthFailure("Couldn't get AccessToken.\nPlease try again.");
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
                contract.OAuthFailure("Failure...");
            }

            @Override
            public void onComplete() {
                contract.OAuthSuccessful();
            }
        };
    }
}
