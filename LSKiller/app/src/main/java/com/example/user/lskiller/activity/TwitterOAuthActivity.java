package com.example.user.lskiller.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.lskiller.R;
import com.example.user.lskiller.Utils.TwitterUtils;

import java.util.Objects;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationContext;

/**
 * Created by USER on 2016/08/14.
 */
public class TwitterOAuthActivity extends AppCompatActivity {

    //    private String mCallbackURL;
    private Twitter mTwitter;
    private RequestToken mRequestToken;
    private EditText editPin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_oauth);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    ) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }

        editPin = (EditText) findViewById(R.id.editPIN);

//        mCallbackURL = getString(R.string.twitter_callback_url);
        mTwitter = TwitterUtils.getTwitterInstance(this);

        findViewById(R.id.action_start_oauth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAuthorize();
            }
        });

        findViewById(R.id.action_pin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authPin(editPin.getText().toString());
            }
        });
    }

    /**
     * OAuth認証（厳密には認可）を開始します。
     *
     * @param
     */
    private void startAuthorize() {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    mRequestToken = mTwitter.getOAuthRequestToken();
                    return mRequestToken.getAuthorizationURL();
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String url) {
                if (url != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } else {
                    // 失敗。。。
                    showToast("認証失敗・・・");
                }
            }
        };
        task.execute();
    }

//    @Override
//    public void onNewIntent(Intent intent) {
//        if (intent == null
//                || intent.getData() == null
//                || !intent.getData().toString().startsWith(mCallbackURL)
//                ) {
//            return;
//        }
//        String verifier = intent.getData().getQueryParameter("oauth_verifier");
//
//        AsyncTask<String, Void, AccessToken> task = new AsyncTask<String, Void, AccessToken>() {
//            @Override
//            protected AccessToken doInBackground(String... params) {
//                try {
//                    return mTwitter.getOAuthAccessToken(mRequestToken, params[0]);
//                } catch (TwitterException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(AccessToken accessToken) {
//                if (accessToken != null) {
//                    // 認証成功！
//                    showToast("認証成功！");
//                    successOAuth(accessToken);
//                } else {
//                    // 認証失敗。。。
//                    showToast("認証失敗。。。");
//                }
//            }
//        };
//        task.execute();
//    }

    private void authPin(final String pin) {
        if (Objects.equals(pin, "")) {
            showToast("PINコードを入力してください");
        }else {
            AsyncTask<String, Void, AccessToken> task = new AsyncTask<String, Void, AccessToken>() {
                @Override
                protected AccessToken doInBackground(String... pin) {
                    try {
                        if(mRequestToken != null) {
                            return mTwitter.getOAuthAccessToken(mRequestToken, pin[0]);
                        }
                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(AccessToken token) {
                    if (token != null) {
                        showToast("ログインしました");
                        successOAuth(token);
                    } else {
                        showToast("認証に失敗しました");
                    }
                }
            };
            task.execute(pin);
        }
    }

    private void successOAuth(AccessToken accessToken) {
        TwitterUtils.storeAccessToken(this, accessToken);
        Intent intent = new Intent(this, TimelineActivity.class);
        startActivity(intent);
        finish();
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
