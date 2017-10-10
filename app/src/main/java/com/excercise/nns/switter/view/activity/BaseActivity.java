package com.excercise.nns.androidex.view.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.excercise.nns.androidex.R;

/**
 * Created by nns on 2017/09/25.
 */

public class BaseActivity extends AppCompatActivity {

    public void replaceFragment(int containerId, Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(containerId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void setupToolbar(Toolbar toolbar, String title, int menuId) {
      toolbar.setTitle(title);
      toolbar.inflateMenu(menuId);
      toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
    }
}
