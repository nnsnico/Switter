package com.excercise.nns.androidex.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.excercise.nns.androidex.data.Token;
import com.excercise.nns.androidex.data.Token_Table;
import com.excercise.nns.androidex.model.entity.TwitterStatus;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.URLEntity;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.util.TimeSpanConverter;

/**
 * Created by nns on 2017/07/12.
 */

public class TwitterUtils {

    private static TimeSpanConverter time = new TimeSpanConverter(Locale.JAPAN);
    private static Token token;

    /**
     * Twitterクラスのインスタンスをもらうことが出来る．
     * DBにアクセスキーとシークレットキーが保存されていれば認証済みのアクセストークンをもったインスタンスを返す．
     * @return アクセストークンをもつTwitterインスタンス，持っていなければAPIキーのみをもったTwitterインスタンスを返す
     * */
    public static Twitter getTwitterInstance(Context context) {
        ConfigurationBuilder config = new ConfigurationBuilder();
        config.setTweetModeExtended(true);
        TwitterFactory factory = new TwitterFactory(config.build());
        String consumerKey = "";
        String consumerSecret = "";
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            consumerKey = info.metaData.getString("API_KEY");
            consumerSecret = info.metaData.getString("API_SECRET");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Twitter twitter = factory.getInstance();
        twitter.setOAuthConsumer(consumerKey, consumerSecret);
        token = SQLite.select().from(Token.class).where(Token_Table.id.is(1)).querySingle();
        if(token != null) {
            twitter.setOAuthAccessToken(new AccessToken(token.getAccessToken(), token.getTokenSecret()));
        }
        return twitter;
    }

    /**
     * TwitterStatusクラス用にキレイに整形した値を返す．
     * @param status APIより取得した生のデータ
     * */
    public static TwitterStatus getStatus(Status status) {
        TwitterStatus tStatus = new TwitterStatus();
        Status result;
        String refactoredText = status.getText();
        // UserTypeSelect
        if(status.isRetweet()) {
            result = status.getRetweetedStatus();
            tStatus.setRetweet(status.getUser().getName());
            refactoredText = replaceText(refactoredText, String.format("RT @%s: ", result.getUser().getScreenName()), "");
        } else {
            result = status;
        }
        // Set Status Parameter
        tStatus.setId(result.getId());
        tStatus.setProfileImageUrl(result.getUser().getOriginalProfileImageURL());
        tStatus.setName(result.getUser().getName());
        tStatus.setScreenName(result.getUser().getScreenName());
        tStatus.setCreatedTime(time.toTimeSpanString(result.getCreatedAt()));
        tStatus.isFavorited = result.isFavorited();
        // Replace URL contained in the Tweet Text
        URLEntity[] entities = status.getURLEntities();
        for (URLEntity entity : entities) {
            refactoredText = replaceText(refactoredText, entity.getURL(), entity.getExpandedURL());
        }
        if(result.getMediaEntities().length > 0) {
            for (MediaEntity entity : result.getMediaEntities()) {
                refactoredText = replaceText(refactoredText, entity.getURL(), "");
            }
            tStatus.setMediaImageUrl(result.getMediaEntities());
        } else {
            tStatus.setMediaImageUrl(null);
        }
        tStatus.setTweetText(refactoredText);
        return tStatus;
    }

    public static boolean hasAccessToken() {
        token = SQLite.select().from(Token.class).where(Token_Table.id.is(1)).querySingle();
        return token != null;
    }

    private static String replaceText(String text, String originalText, String replaceText) {
        Pattern pattern = Pattern.compile(originalText);
        Matcher matcher = pattern.matcher(text);
        return matcher.replaceAll(replaceText);
    }
}
