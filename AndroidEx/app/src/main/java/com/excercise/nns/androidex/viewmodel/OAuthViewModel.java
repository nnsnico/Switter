package com.excercise.nns.androidex.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.excercise.nns.androidex.BR;
import com.excercise.nns.androidex.contract.OAuthContract;
import com.excercise.nns.androidex.model.data.Token;
import com.excercise.nns.androidex.model.usecase.OAuthUseCase;

import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * Created by nns on 2017/06/09.
 */

public class OAuthViewModel extends BaseObservable {
    private OAuthContract contract;
    private Intent intent;
    private OAuthUseCase useCase;
    @Bindable
    public String pin;

    public OAuthViewModel(
            OAuthContract contract, String consumer, String consumerSecret) {
        this.contract = contract;
        // load Twitter Instance
        TwitterFactory factory = new TwitterFactory();
        Twitter twitter = factory.getInstance();
        twitter.setOAuthConsumer(consumer, consumerSecret);
        useCase = new OAuthUseCase(twitter);
    }

    public void onClickPIN(View view) {
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {}

            @Override
            public void onNext(@NonNull String url) {
                if (url != null) {
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
        // OAuth認証画面へ
        useCase.getRequestToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void onClickOAuth(View view) {
        if (Objects.equals(pin, "")) {
            contract.OAuthFailure("PIN is not ENTERED.");
        } else {
            Observer<AccessToken> observer = new Observer<AccessToken>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {}

                @Override
                public void onNext(@NonNull AccessToken accessToken) {
                    if (accessToken != null) {
                        Log.d(OAuthViewModel.class.toString(), "AccessToken got.");
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
            useCase.readPinCode(pin)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
    }

    public void setPin(String pin) {
        this.pin = pin;
        notifyPropertyChanged(BR.pin);
    }
}
