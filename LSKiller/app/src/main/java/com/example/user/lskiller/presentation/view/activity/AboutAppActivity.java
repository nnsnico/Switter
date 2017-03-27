/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.user.lskiller.presentation.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.transition.Slide;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.user.lskiller.R;

/**
 * Created by USER on 2016/10/26.
 */
public class AboutAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        getWindow().setEnterTransition(new Slide());

        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.loadUrl("file:///android_asset/licenses.html");

        TextView myScreen = (TextView) findViewById(R.id.my_screen);
        myScreen.setOnClickListener(v -> {
            Intent intent =
                new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/tym_free"));
            startActivity(intent);
        });
    }
}
