package com.excercise.nns.androidex.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;

import com.excercise.nns.androidex.BR;
import com.excercise.nns.androidex.contract.OAuthContract;
import com.excercise.nns.androidex.model.usecase.OAuthUseCase;
import com.excercise.nns.androidex.viewmodel.factory.OAuthObserverFactory;

import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * Created by nns on 2017/06/09.
 */

public class OAuthViewModel extends BaseObservable {
    private OAuthContract contract;
    private OAuthUseCase useCase;
    private OAuthObserverFactory factory;
    @Bindable
    public String pin;

    public OAuthViewModel(
            OAuthContract contract, String consumer, String consumerSecret) {
        this.contract = contract;
        // observer取得用のfactory
        factory = new OAuthObserverFactory(contract);
        // load Twitter Instance
        TwitterFactory factory = new TwitterFactory();
        Twitter twitter = factory.getInstance();
        twitter.setOAuthConsumer(consumer, consumerSecret);
        useCase = new OAuthUseCase(twitter);
    }

    public void onClickPIN(View view) {
        Observer<String> observer = factory.getRequestTokenObserver();
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
            Observer<AccessToken> observer = factory.getAccessTokenObserver();
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
