package com.excercise.nns.androidex.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.excercise.nns.androidex.R;
import com.excercise.nns.androidex.contract.TweetContract;
import com.excercise.nns.androidex.databinding.ActivityTweetBinding;
import com.excercise.nns.androidex.viewmodel.TweetViewModel;

// TODO: 2017/07/28 if released uploading medias, set reading files permission by "PermissionDispatcher"
public class TweetActivity extends AppCompatActivity implements TweetContract {

    private ProgressDialog progressDialog;
    private ActivityTweetBinding binding;

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
        long userId = getIntent().getLongExtra("userId", 0);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tweet);
        TweetViewModel viewModel = new TweetViewModel(this, screenName, userId);
        viewModel.setMessage(binding.editTweet.getText().toString());
        binding.setViewmodel(viewModel);
    }

    @Override
    public void onSendingTweet() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("送信中・・・");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void setTweetCount(int count, int screenNameCount) {
        if (count > 140 - screenNameCount) {
            binding.tweetCount.setTextColor(Color.RED);
        } else {
            binding.tweetCount.setTextColor(Color.WHITE);
        }
        binding.tweetCount.setText(
                String.format("%s/140", Integer.toString(140 - count + screenNameCount)));
    }

    @Override
    public void setReplyUser(String screenName) {
        binding.editTweet.setText(String.format("@%s ", screenName));
        binding.editTweet.setSelection(screenName.length() + 2);
    }

    @Override
    public void onTweetSuccess() {
        progressDialog.dismiss();
        Toast.makeText(this, "ツイートしました！！", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onTweetFailed() {
        progressDialog.dismiss();
        Toast.makeText(this, "送信に失敗しました。\nもう一度行ってください。", Toast.LENGTH_LONG).show();
    }
}
