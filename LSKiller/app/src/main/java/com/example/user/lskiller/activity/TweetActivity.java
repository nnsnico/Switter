package com.example.user.lskiller.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.lskiller.R;
import com.example.user.lskiller.Utils.TwitterUtils;

import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by USER on 2016/08/16.
 */
public class TweetActivity extends FragmentActivity{

    private EditText mInputText;
    private Twitter mTwitter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        mTwitter = TwitterUtils.getTwitterInstance(this);
        mInputText = (EditText)findViewById(R.id.input_text);

        findViewById(R.id.action_tweet).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                tweet();
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
