package com.excercise.nns.androidex.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.support.v4.content.ContextCompat;
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
    @Bindable
    public String message;

    public TweetViewModel(TweetContract contract, String screenName, long userId) {
        this.contract = contract;
        factory = new TweetObserverFactory(contract);
        twitter = TwitterUtils.getTwitterInstance();

        isUser = screenName != null;
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
            // TODO: 2017/07/16  on reply by usecase
            // reply

        }
    }

    public void onTweetChanged(CharSequence text, int i, int i1, int i2) {
        // TODO: 2017/07/24  changed tweet color when text's length is over
        contract.setTweetCount(text.toString().length());
    }

    public void setMessage(String message) {
        this.message = message;
        notifyPropertyChanged(BR.message);
    }
}
