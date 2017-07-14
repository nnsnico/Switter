package com.excercise.nns.androidex.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.excercise.nns.androidex.R;

public class TweetActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, TweetActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
    }
}
