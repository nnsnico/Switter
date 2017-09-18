package com.excercise.nns.androidex.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.SharedPreferencesCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.excercise.nns.androidex.BR;
import com.excercise.nns.androidex.contract.TweetContract;
import com.excercise.nns.androidex.model.usecase.TweetUseCase;
import com.excercise.nns.androidex.utils.TwitterUtils;
import com.excercise.nns.androidex.viewmodel.factory.TweetObserverFactory;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import twitter4j.Status;
import twitter4j.Twitter;

/**
 * Created by nns on 2017/07/15.
 */

@BindingMethods(
        @BindingMethod(type = EditText.class, attribute = "android:onTextChanged", method = "addTextChangeListener"))
public class TweetViewModel extends BaseObservable {

    private boolean isUser = false;
    private TweetUseCase useCase;
    private Twitter twitter;
    private TweetContract contract;
    private TweetObserverFactory factory;
    private String screenName;
    private long userId;
    @Bindable
    public String message;

    public TweetViewModel(Twitter twitter, TweetContract contract, String screenName, long userId) {
        this.twitter = twitter;
        this.contract = contract;
        this.screenName = screenName;
        this.userId = userId;

        factory = new TweetObserverFactory(contract);

        isUser = screenName != null;

        if (isUser) {
            contract.setReplyUser(screenName);
        } else {
            contract.setTweetCount(0, 0);
        }
    }

    public void onClickTweet(View view) {
        useCase = new TweetUseCase(twitter);
        if (!isUser) {
            // tweet
            Observer<Status> observer = factory.getTweetObserver();
            contract.onSendingTweet();
            useCase.tweet(message)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        } else {
            // reply
            Observer<Status> observer = factory.getTweetObserver();
            contract.onSendingTweet();
            useCase.reply(message, userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }
    }

    public void onTweetChanged(CharSequence text, int i, int i1, int i2) {
        if (isUser) {
            contract.setTweetCount(text.toString().length(), screenName.length() + 2);
        } else {
            contract.setTweetCount(text.toString().length(), 0);
        }
    }

    public void setMessage(String message) {
        this.message = message;
        notifyPropertyChanged(BR.message);
    }
}
