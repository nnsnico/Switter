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
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
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
            SwipeLayout swipeLayout, TwitterStatus status) {
        OnRecyclerListener rListener = (OnRecyclerListener) swipeLayout.getContext();
        Twitter twitter = TwitterUtils.getTwitterInstance(swipeLayout.getContext());

        swipeLayout.findViewById(R.id.goProfile).setOnClickListener(v -> {
            rListener.onSwipeItemClick("goPro", status);
            swipeLayout.close();
        });
        swipeLayout.findViewById(R.id.reply).setOnClickListener(v -> {
            rListener.onSwipeItemClick("reply", status);
            swipeLayout.close();
        });
        swipeLayout.findViewById(R.id.reTweet).setOnClickListener(v -> {
            // TODO: 2017/07/16 retweet user by usecase and observer.
            // contract -> onRetweetSuccess and onRetweetFailed
            swipeLayout.close();
        });

        swipeLayout.findViewById(R.id.favorite).setOnClickListener(v -> {
            TimelineContract tContract = (TimelineContract) v.getContext();
            FavoriteUseCase useCase = new FavoriteUseCase(twitter);
            useCase.postFavorite(status)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {}

                        @Override
                        public void onNext(@NonNull Boolean isFavorite) {
                            status.isFavorited = isFavorite;
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            tContract.postActionFailed("エラーが発生しました。もう一度行ってください。");
                        }

                        @Override
                        public void onComplete() {
                            String message = status.isFavorited ? "Favorite" : "Destroyed Favorite";
                            tContract.postFavoriteSuccess(message);
                        }
                    });
            swipeLayout.close();
        });

    }
}
