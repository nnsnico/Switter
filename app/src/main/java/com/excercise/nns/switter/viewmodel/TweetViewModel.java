package com.excercise.nns.switter.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.view.View;
import android.widget.EditText;

import com.excercise.nns.switter.BR;
import com.excercise.nns.switter.contract.TweetContract;
import com.excercise.nns.switter.model.usecase.TweetUseCase;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
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
    private String screenName;
    private long userId;
    @Bindable
    public String message;

    public TweetViewModel(Twitter twitter, TweetContract contract, String screenName, long userId) {
        this.twitter = twitter;
        this.contract = contract;
        this.screenName = screenName;
        this.userId = userId;

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
            contract.onSendingTweet();
            useCase.tweet(message)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(tweetObserver());
        } else {
            // reply
            contract.onSendingTweet();
            useCase.reply(message, userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(tweetObserver());
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

    private Observer<Status> tweetObserver() {
        return new Observer<Status>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {}

            @Override
            public void onNext(@NonNull Status update) {}

            @Override
            public void onError(@NonNull Throwable e) {
                contract.onTweetFailed();
            }

            @Override
            public void onComplete() {
                contract.onTweetSuccess();
            }
        };
    }
}
