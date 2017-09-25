package com.excercise.nns.androidex.viewmodel;

import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.excercise.nns.androidex.R;
import com.excercise.nns.androidex.contract.HomeContract;
import com.excercise.nns.androidex.utils.TwitterUtils;

/**
 * Created by nns on 2017/06/19.
 */

@BindingMethods({
        @BindingMethod(type = Toolbar.class, attribute = "android:onMenuItemClick", method = "setOnMenuItemClickListener"),})
public class HomeViewModel {
    private HomeContract contract;

    public HomeViewModel(HomeContract contract) {
        this.contract = contract;
        if (!TwitterUtils.hasAccessToken()) {
            contract.onStartOAuth();
        }
    }

    public boolean onMenuClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_tweet:
                contract.onStartTweet();
                return true;
            case R.id.AboutApp:
                contract.onStartAbout();
                return true;
            default:
                break;
        }
        return false;
    }
}
