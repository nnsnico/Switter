package com.example.user.lskiller.activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.lskiller.R;
import com.example.user.lskiller.Utils.TwitterUtils;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

/**
 * Created by USER on 2016/10/13.
 */
public class ReplyActivity extends FragmentActivity {

    private EditText mInputText;
    private Twitter mTwitter;
    private TextView countText;
    String screenName;
    long UserId;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        screenName = getIntent().getStringExtra("screenName");
        UserId = getIntent().getLongExtra("status", 1);

        mTwitter = TwitterUtils.getTwitterInstance(this);
        mInputText = (EditText)findViewById(R.id.input_text);
        mInputText.setText("@" + screenName + " ", TextView.BufferType.EDITABLE);
        mInputText.setSelection(mInputText.getText().length());

        countText = (TextView)findViewById(R.id.countText);
        countText.setText(String.format("%s/140",Integer.toString(140 - mInputText.getText().length())));
        countTweet();

        findViewById(R.id.action_tweet).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                reply(mInputText.getText().toString(), UserId);
            }
        });
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

    private void reply(final String message,  final long UserId){
        AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>(){
            @Override
            protected Boolean doInBackground(String... params){
                try{
                    StatusUpdate su = new StatusUpdate(message);
                    su.setInReplyToStatusId(UserId);
                    mTwitter.updateStatus(su);
                    return true;
                } catch (TwitterException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean result){
                if(result){
                showTweet("返信しました");
                    finish();
                }else{
                    showTweet("返信出来ませんでした");
                }
            }
        };

        task.execute(mInputText.getText().toString());
    }

    private void showTweet(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
