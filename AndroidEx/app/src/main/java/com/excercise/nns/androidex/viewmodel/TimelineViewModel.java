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
import com.excercise.nns.androidex.model.usecase.TimelineUseCase;
import com.excercise.nns.androidex.utils.TwitterUtils;
import com.excercise.nns.androidex.viewmodel.factory.TimelineObserverFactory;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.Twitter;

/**
 * Created by nns on 2017/06/19.
 */

@BindingMethods({
        @BindingMethod(type = Toolbar.class, attribute = "android:onMenuItemClick", method = "setOnMenuItemClickListener"),
        @BindingMethod(type = SwipeRefreshLayout.class, attribute = "android:onRefresh", method = "setOnRefreshListener")})
public class TimelineViewModel {
    private TimelineContract contract;
    private Twitter twitter;
    private TimelineObserverFactory factory;

    public TimelineViewModel(
            TimelineContract contract) {
        this.contract = contract;
        factory = new TimelineObserverFactory(contract);
        twitter = TwitterUtils.getTwitterInstance();
        if (twitter == null) {
            contract.onStartOAuth();
        }
        // get timeline
        loadTimeline();
    }

    private void loadTimeline() {
        TimelineUseCase useCase = new TimelineUseCase(twitter);
        Observer<List<Status>> observer = factory.getTimelineObserver();
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
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 300;
                params.height = 300;
                params.setMargins(0, 0, 15, 15);
                image.setLayoutParams(params);
                layout.addView(image, params);
                Picasso.with(layout.getContext()).load(entity.getMediaURL()).into(image);
            }
        }
    }

    @BindingAdapter({"bind:targetStatus", "bind:listener", "bind:contract"})
    public static void onClickSwipeItem(
            SwipeLayout swipeLayout, TwitterStatus status, OnRecyclerListener listener, TimelineContract contract) {
        swipeLayout.findViewById(R.id.goProfile).setOnClickListener(v -> {
            listener.onSwipeItemClick("goPro", status);
            swipeLayout.close();
        });
        swipeLayout.findViewById(R.id.reply).setOnClickListener(v -> {
            listener.onSwipeItemClick("reply", status);
            swipeLayout.close();
        });
        swipeLayout.findViewById(R.id.reTweet).setOnClickListener(v -> {
            // TODO: 2017/07/16 retweet user by usecase and observer.
            // contract -> onRetweetSuccess and onRetweetFailed
            swipeLayout.close();
        });

        swipeLayout.findViewById(R.id.favorite).setOnClickListener(v -> {
            // TODO: 2017/07/16 favorite user by usecase and observer.
            // contract -> onFavoriteSuccess and onFavoriteFailed
            swipeLayout.close();
        });

    }
}
