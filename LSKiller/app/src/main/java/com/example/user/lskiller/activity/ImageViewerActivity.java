package com.example.user.lskiller.activity;

import android.content.Context;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.user.lskiller.R;
import com.loopj.android.image.SmartImageView;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by USER on 2016/12/02.
 */
public class ImageViewerActivity extends AppCompatActivity {

    PhotoViewAttacher mAttacher;
    SmartImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_preview);

        imageView = (SmartImageView) findViewById(R.id.previewImage);
        String url = getIntent().getExtras().getString("imageView");
        imageView.setImageUrl(url);

//        mAttacher = new PhotoViewAttacher(imageView);
    }

    // TODO ViewPagerでイメージをスワイプで切り替える
    static class SimpleViewPager extends PagerAdapter{

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
