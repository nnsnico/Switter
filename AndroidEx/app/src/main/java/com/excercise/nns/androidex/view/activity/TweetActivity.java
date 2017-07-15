package com.excercise.nns.androidex.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.excercise.nns.androidex.R;
import com.excercise.nns.androidex.databinding.ActivityTweetBinding;
import com.excercise.nns.androidex.viewmodel.TweetViewModel;

public class TweetActivity extends AppCompatActivity {

    public static void start(Context context, String screenName, long userId) {
        Intent intent = new Intent(context, TweetActivity.class);
        intent.putExtra("screenName", screenName);
        intent.putExtra("userId", userId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String screenName = getIntent().getStringExtra("screenName");
        ActivityTweetBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_tweet);
        TweetViewModel viewModel = new TweetViewModel(screenName);
        binding.setViewmodel(viewModel);
    }
}
