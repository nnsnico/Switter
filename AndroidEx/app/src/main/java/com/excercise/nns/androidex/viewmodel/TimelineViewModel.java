package com.excercise.nns.androidex.viewmodel;

import android.databinding.BindingAdapter;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.daimajia.swipe.SwipeLayout;
import com.excercise.nns.androidex.R;
import com.excercise.nns.androidex.contract.OnRecyclerListener;
import com.excercise.nns.androidex.contract.TimelineContract;
import com.excercise.nns.androidex.model.entity.TwitterStatus;
import com.excercise.nns.androidex.model.data.Token;
import com.excercise.nns.androidex.model.data.Token_Table;
import com.excercise.nns.androidex.model.usecase.TimelineUseCase;
import com.excercise.nns.androidex.utils.TwitterUtils;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;

/**
 * Created by nns on 2017/06/19.
 */

@BindingMethods({
        @BindingMethod(type = Toolbar.class, attribute = "android:onMenuItemClick", method = "setOnMenuItemClickListener"),
        @BindingMethod(type = SwipeRefreshLayout.class, attribute = "android:onRefresh", method = "setOnRefreshListener")})
public class TimelineViewModel {
    private TimelineContract contract;
    private Twitter twitter;
    private ArrayList<TwitterStatus> statuses = new ArrayList<>();

    public TimelineViewModel(
            TimelineContract contract, String consumerKey, String consumerSecret) {
        this.contract = contract;
        TwitterFactory factory = new TwitterFactory();
        twitter = factory.getInstance();
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
        // query token
        Token token = SQLite.select().from(Token.class).where(Token_Table.id.is(1)).querySingle();
        if (token != null)
            twitter.setOAuthAccessToken(new AccessToken(token.getAccessToken(), token.getTokenSecret()));
        else
            contract.onStartOAuth();
        // get timeline
        loadTimeline();
    }

    private void loadTimeline() {
        TimelineUseCase useCase = new TimelineUseCase(twitter);
        Observer<List<Status>> observer = new Observer<List<Status>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull List<Status> result) {
                if (result != null) {
                    statuses = new ArrayList<>();
                    for (Status status : result) {
                        TwitterStatus st = TwitterUtils.getStatus(status);
                        statuses.add(st);
                    }
                } else {
                    contract.getTimelineFailed("タイムラインの取得に失敗しました\n" +
                            "時間を置いてから再度起動してください");
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
                contract.getTimelineFailed("タイムラインの取得に失敗しました\n" +
                        "時間を置いてから再度起動してください");
            }

            @Override
            public void onComplete() {
                contract.getTimelineSuccess(statuses);
            }
        };
        useCase.getHomeTimeline(40)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public boolean onMenuClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                loadTimeline();
                return true;
            case R.id.menu_tweet:
                contract.onStartTweet();
                return true;
            case R.id.AboutApp:
                contract.onStartAbout();
                return true;
            default:
                break;
        }
        return false;
    }

    public void onRefresh() {
        loadTimeline();
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(CircleImageView profileImage, String profileImageUrl) {
        Picasso.with(profileImage.getContext()).load(profileImageUrl).into(profileImage);
    }

    @BindingAdapter({"bind:uploadImage"})
    public static void loadUploadImage(GridLayout layout, MediaEntity[] entities) {
        if (layout.getChildCount() > 0) {
            layout.removeAllViews();
        }
        if (entities != null) {
            for (MediaEntity entity : entities) {
                ImageView image = new ImageView(layout.getContext());
                image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                image.setPadding(0, 0, 0, 32);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 300;
                params.height = 300;
                params.setMarginEnd(15);
                image.setLayoutParams(params);
                layout.addView(image, params);
                Picasso.with(layout.getContext()).load(entity.getMediaURL()).into(image);
            }
        }
    }

    @BindingAdapter({"bind:targetStatus", "bind:listener"})
    public static void onClickSwipeItem(
            SwipeLayout swipeLayout, TwitterStatus status, OnRecyclerListener listener) {
        swipeLayout.findViewById(R.id.goProfile).setOnClickListener(v -> {
            listener.onSwipeItemClick("goPro", status);
        });
        swipeLayout.findViewById(R.id.reply).setOnClickListener(v -> {
            listener.onSwipeItemClick("reply", status);
        });
        swipeLayout.findViewById(R.id.reTweet).setOnClickListener(v -> {
            listener.onSwipeItemClick("retweet", status);
        });
        swipeLayout.findViewById(R.id.favorite).setOnClickListener(v -> {
            listener.onSwipeItemClick("fav", status);
        });
        swipeLayout.close();
    }
}
