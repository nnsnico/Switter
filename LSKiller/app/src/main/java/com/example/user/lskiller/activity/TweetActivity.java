package com.example.user.lskiller.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.lskiller.R;
import com.example.user.lskiller.Utils.TwitterUtils;

import java.util.List;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by USER on 2016/08/16.
 */
public class TweetActivity extends FragmentActivity{

    private EditText mInputText;
    private Twitter mTwitter;
    private TextView countText;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

//        final String screenName = getIntent().getExtras().getString("screenName");

        mTwitter = TwitterUtils.getTwitterInstance(this);
        mInputText = (EditText)findViewById(R.id.input_text);
        countTweet();
        countText = (TextView)findViewById(R.id.countText);

//        assert screenName != null;
//        if(screenName.isEmpty()){
//            mInputText.setText(String.format("%s ", screenName));
//        }

        findViewById(R.id.action_tweet).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
//                if(screenName.isEmpty()){
//                    try {
//                        reply(screenName);
//                    } catch (TwitterException e) {
//                        e.printStackTrace();
//                    }
//                }else {
                    tweet();
//                }
            }
        });
    }

    private void reply(String screenName) throws TwitterException{

    }

    private void countTweet() {
        mInputText.addTextChangedListener(new TextWatcher() {
            int textLength = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int textColor = Color.WHITE;
                countText.setTextColor(textColor);

                // 入力文字数の表示
                String s = charSequence.toString();
                textLength = s.length();

                // 指定文字数オーバーで文字色を赤くする
                if (textLength > 140) {
                    textColor = Color.RED;
                    countText.setTextColor(textColor);
                }
                countText.setText(String.format("%s/140", Integer.toString(140 - textLength)));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void tweet(){
        AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>(){
            @Override
            protected Boolean doInBackground(String... params){
                try{
                    mTwitter.updateStatus(params[0]);
                    return true;
                } catch (TwitterException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean result){
                if(result){
                    showTweet("ツイートしました");
                    finish();
                }else{
                    showTweet("ツイート出来ませんでした");
                }
            }
        };

        task.execute(mInputText.getText().toString());
    }

    private void showTweet(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
