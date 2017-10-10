package com.excercise.nns.switter.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.excercise.nns.switter.R;

public class AboutAppActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, AboutAppActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
    }
}
