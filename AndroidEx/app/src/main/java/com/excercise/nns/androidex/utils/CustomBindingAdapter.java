package com.excercise.nns.androidex.utils;

import android.widget.GridLayout;
import android.widget.ImageView;

import android.databinding.BindingAdapter;
import com.daimajia.swipe.SwipeLayout;
import com.excercise.nns.androidex.R;
import com.excercise.nns.androidex.contract.OnRecyclerListener;
import com.excercise.nns.androidex.contract.TimelineContract;
import com.excercise.nns.androidex.model.entity.TwitterStatus;
import com.excercise.nns.androidex.model.usecase.FavoriteUseCase;
import com.excercise.nns.androidex.viewmodel.factory.FavoriteObserverFactory;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import twitter4j.MediaEntity;
import twitter4j.Twitter;

/**
 * Created by nns on 2017/09/18.
 */

public class CustomBindingAdapter {
    @BindingAdapter({"imageUrl"})
    public static void loadImage(CircleImageView profileImage, String profileImageUrl) {
        Picasso.with(profileImage.getContext()).load(profileImageUrl).into(profileImage);
    }

    @BindingAdapter({"uploadImage"})
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

    @BindingAdapter({"targetStatus", "listener", "contract"})
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
            Twitter twitter = TwitterUtils.getTwitterInstance(v.getContext());
            FavoriteObserverFactory factory = new FavoriteObserverFactory(contract);
            FavoriteUseCase useCase = new FavoriteUseCase(twitter);
            Observer<Boolean> observer = factory.getFavoriteObserver(status);
            useCase.postFavorite(status)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
            swipeLayout.close();
        });

    }
}
