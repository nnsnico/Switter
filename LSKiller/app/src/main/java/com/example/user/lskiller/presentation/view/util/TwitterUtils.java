package com.example.user.lskiller.presentation.view.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * Created by USER on 2016/08/14.
 */
public class TwitterUtils {

    private static final String TOKEN = "token";
    private static final String TOKEN_SECRET = "token_secret";
    private static final String PREF_NAME = "twitter_access_token";

    /**
     * Twitterインスタンスを取得します。アクセストークンが保存されていれば自動的にセットします。
     *
     * @param context
     * @return twitter
     */
    public static Twitter getTwitterInstance(Context context) {
        String consumerKey = "";
        String consumerSecret = "";

        try {
            ApplicationInfo info =
                    context.getPackageManager().getApplicationInfo(
                            context.getPackageName(), PackageManager.GET_META_DATA);
            consumerKey = info.metaData.getString("apikey");
            consumerSecret = info.metaData.getString("apisecret");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        TwitterFactory factory = new TwitterFactory();
        Twitter twitter = factory.getInstance();
        twitter.setOAuthConsumer(consumerKey, consumerSecret);

        if (hasAccessToken(context)) {
            twitter.setOAuthAccessToken(loadAccessToken(context));
        }
        return twitter;
    }

    /**
     * アクセストークンをプリファレンスに保存します。
     *
     * @param context
     * @param accessToken
     */
    public static void storeAccessToken(Context context, AccessToken accessToken) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TOKEN, accessToken.getToken());
        editor.putString(TOKEN_SECRET, accessToken.getTokenSecret());
        editor.apply();
    }

    /**
     * アクセストークンをプリファレンスから読み込みます。
     *
     * @param context
     * @return AccessToken
     */
    public static AccessToken loadAccessToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME,
                Context.MODE_PRIVATE);
        String token = preferences.getString(TOKEN, null);
        String tokenSecret = preferences.getString(TOKEN_SECRET, null);
        if (token != null && tokenSecret != null) {
            return new AccessToken(token, tokenSecret);
        } else {
            return null;
        }
    }

    /**
     * アクセストークンが存在する場合はtrueを返します。
     *
     * @return boolean
     */
    public static boolean hasAccessToken(Context context) {
        return loadAccessToken(context) != null;
    }

}