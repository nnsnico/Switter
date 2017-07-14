package com.excercise.nns.androidex.view.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.excercise.nns.androidex.R;
import com.excercise.nns.androidex.contract.OAuthContract;
import com.excercise.nns.androidex.databinding.ActivityOauthBinding;
import com.excercise.nns.androidex.viewmodel.OAuthViewModel;

public class OAuthActivity extends AppCompatActivity implements OAuthContract {

    public static void start(Context context) {
        Intent intent = new Intent(context, OAuthActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityOauthBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_oauth);
        String consumerKey = getString(R.string.api_key);
        String consumerSecret = getString(R.string.api_secret);
        // set ViewModel
        OAuthViewModel viewModel = new OAuthViewModel(this, consumerKey, consumerSecret);
        viewModel.setPin(binding.pinEditor.getText().toString());
        binding.setViewmodel(viewModel);
    }

    @Override
    public void RequestSuccessful(Intent intent) {
        Toast.makeText(this, "request success.", Toast.LENGTH_SHORT).show();
        // Twitterの認証ブラウザーに接続
        startActivity(intent);
    }

    @Override
    public void RequestFailure() {
        Toast.makeText(this, "request failure.\nplease try again.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OAuthSuccessful() {
        // タイムライン画面へ移動
        Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
        TimelineActivity.start(this);
        finish();
    }

    @Override
    public void OAuthFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
