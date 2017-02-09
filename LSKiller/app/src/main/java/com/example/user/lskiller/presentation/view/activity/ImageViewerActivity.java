package com.example.user.lskiller.presentation.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.user.lskiller.R;
import com.example.user.lskiller.presentation.view.component.PagerLayout;
import com.loopj.android.image.SmartImageView;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

import java.util.ArrayList;

import twitter4j.MediaEntity;

/**
 * Created by USER on 2016/12/02.
 */
public class ImageViewerActivity extends AppCompatActivity {

    SmartImageView imageView;
    MediaEntity[] mediaEntities;
    ArrayList<ImageView> list = new ArrayList<>();
    int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_preview);
        getWindow().setEnterTransition(new Slide());

        slidrConfig();

        ViewPager viewPager = (PagerLayout) findViewById(R.id.previewImage);
        mediaEntities = (MediaEntity[]) getIntent().getSerializableExtra("media");
        position = getIntent().getIntExtra("position", 0);
        for (MediaEntity mediaEntity : mediaEntities) {
            imageView = new SmartImageView(ImageViewerActivity.this);
            imageView.setImageUrl(mediaEntity.getMediaURL());
            list.add(imageView);
        }
        viewPager.setAdapter(new SimpleViewPager(list));
        viewPager.setCurrentItem(position);
        setContentView(viewPager);
    }

    // delete by horizontal slide
    private void slidrConfig() {
        SlidrConfig config = new SlidrConfig.Builder()
                .position(SlidrPosition.HORIZONTAL)
                .edge(true)
                .build();
        Slidr.attach(this, config);
    }

    /**
     * ImageViewPager
     */
    class SimpleViewPager extends PagerAdapter {
        private ArrayList<ImageView> list;

        SimpleViewPager(ArrayList<ImageView> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            container.addView(
                    list.get(position),
                    ViewPager.LayoutParams.MATCH_PARENT,
                    ViewPager.LayoutParams.MATCH_PARENT);

            return list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
