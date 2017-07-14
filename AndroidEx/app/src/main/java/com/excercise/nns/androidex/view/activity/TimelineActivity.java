package com.excercise.nns.androidex.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.excercise.nns.androidex.R;
import com.excercise.nns.androidex.contract.TimelineContract;
import com.excercise.nns.androidex.databinding.ActivityTimelineBinding;
import com.excercise.nns.androidex.viewmodel.TimelineViewModel;

import io.realm.Realm;

/**
 * Created by nns on 2017/06/12.
 */

public class TimelineActivity extends AppCompatActivity implements TimelineContract {

    public static void start(Context context) {
        Intent intent = new Intent(context, TimelineActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Realm.init(this);
        TimelineViewModel viewModel = new TimelineViewModel(this);
        ActivityTimelineBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_timeline);
        binding.setViewmodel(viewModel);
        binding.toolbar.setTitle(R.string.app_name);
    }

    @Override
    public void onStartOAuth() {
        OAuthActivity.start(this);
        finish();
    }
}
