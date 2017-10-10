package com.excercise.nns.androidex.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.excercise.nns.androidex.R;
import com.excercise.nns.androidex.databinding.ActivityProfileBinding;
import com.excercise.nns.androidex.model.entity.TwitterStatus;
import com.excercise.nns.androidex.view.fragment.TimelineFragment;

public class ProfileActivity extends BaseActivity {
    private TwitterStatus status;
    private ActivityProfileBinding binding;

    public static void start(Context context, TwitterStatus status) {
        Intent starter = new Intent(context, ProfileActivity.class);
        starter.putExtra("status", status);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        status = (TwitterStatus) getIntent().getSerializableExtra("status");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        binding.setStatus(status);
        // TODO: 2017/10/01 後でmenuを入れ替える
        setupToolbar(
                binding.toolbar,
                String.format("%s @%s" ,status.getUser().getName(), status.getUser().getScreenName()),
                R.menu.toolbar_item);
        // Userを渡してフラグメントに託す
        TimelineFragment fragment = TimelineFragment.newInstance(status.getUser());
        replaceFragment(R.id.container, fragment);
    }
}
