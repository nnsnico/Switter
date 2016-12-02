package com.example.user.lskiller.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.user.lskiller.AsyncTask.FavoriteAsync;
import com.example.user.lskiller.AsyncTask.ReTweetAsync;
import com.example.user.lskiller.AsyncTask.TimeLineAsync;
import com.example.user.lskiller.RecyclerItem.DividerItemDecoration;
import com.example.user.lskiller.Listener.EndlessScrollListener;
import com.example.user.lskiller.Listener.OnRecyclerListener;
import com.example.user.lskiller.R;
import com.example.user.lskiller.Utils.TwitterUtils;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;

public class TimelineActivity extends AppCompatActivity implements OnRecyclerListener {

    Twitter mTwitter;
    //    private ListView listView;
    private Toolbar toolbar;
    private List<Status> statuses = new ArrayList<>();
    private RecyclerView recyclerView;
    //    private SwipeLayout swipeLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline_material);
        // ツールバー
        toolbar = (Toolbar) findViewById(R.id.toolbar_material);
        toolbarConfig();

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
//        setSupportActionBar(toolbar);
        swipeLayoutConfig();
//        swipeLayout = (SwipeLayout)findViewById(R.id.swipeMenu);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // Twitterのトークンが取得されているか
        if (!TwitterUtils.hasAccessToken(this)) {
            Intent intent = new Intent(this, TwitterOAuthActivity.class);
            startActivity(intent);
            finish();
        } else {
            mTwitter = TwitterUtils.getTwitterInstance(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(
                    this,
                    LinearLayoutManager.VERTICAL,
                    false
            ));
            recyclerView.addItemDecoration(new DividerItemDecoration(this));

//            SwipeMenuConfig();
//            FileInputStream inputStream = null;
//            byte[] buffer = new byte[1024];
//            try {
//                File file = new File(getCacheDir(), "cache.txt");
//                if (file.exists()) {
//                    inputStream = new FileInputStream(file);
//                    inputStream.read(buffer);
//                    String data = new String(buffer, "UTF-8");
//                    // TODO  キャッシュ読み込み（いるのか？）
//                    recyclerView.setAdapter(new RecyclerAdapter(this, data., (OnRecyclerListener) this));
//                }
//            } catch (IOException e) {
//                Log.e("MyApp", "exception", e);
//            } finally {
//                try {
//                    if (inputStream != null) {
//                        inputStream.close();
                        reloadTimeLine();
//                    }
//                } catch (IOException e) {
//                    Log.e("MyApp", "exception", e);
//                }
//            }

            // endScrollListener
            recyclerView.addOnScrollListener(new EndlessScrollListener(
                    (LinearLayoutManager) recyclerView.getLayoutManager()) {
                @Override
                public void onLoadMore(int current_page) {
                    reloadTimeLine(current_page);
                    Toast.makeText(TimelineActivity.this, "読み込み中・・・", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    // トップスワイプアクション
    private void swipeLayoutConfig() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadTimeLine();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void toolbarConfig() {
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite));

        toolbar.inflateMenu(R.menu.toolbar_item);
        // ToolBarのアイテム取得
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_refresh:
                        reloadTimeLine();
                        return true;
                    case R.id.menu_tweet:
                        Intent intent = new Intent(TimelineActivity.this, TweetActivity.class);
                        startActivity(intent);
                        reloadTimeLine();
                        return true;
                    case R.id.AboutApp:
                        Intent intent2 = new Intent(TimelineActivity.this, AboutAppActivity.class);
                        startActivity(intent2);
                }
                return true;
            }
        });
    }

    public void reloadTimeLine() {
        AsyncTask<Void, Void, List<twitter4j.Status>> task = new TimeLineAsync(
                mTwitter,
                statuses,
                TimelineActivity.this,
                recyclerView);
        task.execute();
    }

    public void reloadTimeLine(int currentPage) {
        AsyncTask<Void, Void, List<twitter4j.Status>> task = new TimeLineAsync(
                mTwitter,
                statuses,
                TimelineActivity.this,
                recyclerView,
                currentPage
        );
        task.execute();
    }

    @Override
    public void onRecyclerClicked(String tag, final List<Status> statuses, final int position) {
        switch (tag) {
            case "fav":
                AsyncTask<Long, Void, Boolean> favoriteTask = new FavoriteAsync(
                        TimelineActivity.this,
                        TimelineActivity.this,
                        statuses,
                        position
                );
                favoriteTask.execute();
                break;
            case "ret":
                AsyncTask<Long, Void, Boolean> reTweetTask = new ReTweetAsync(
                        TimelineActivity.this,
                        TimelineActivity.this,
                        statuses,
                        position
                );
                reTweetTask.execute();
                break;
            case "rep":
                Intent intent = new Intent(TimelineActivity.this, ReplyActivity.class);
                intent.putExtra("screenName", statuses.get(position).getUser().getScreenName());
                intent.putExtra("status", statuses.get(position).getId());
                startActivity(intent);
                break;
        }
        reloadTimeLine();
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        FileOutputStream outputStream = null;
//        try {
//            // キャッシュ領域にファイルを作成し、書き込む。
//            File file = new File(getCacheDir(), "cache.txt");
//            file.createNewFile();
//            if (file.exists()) {
//                outputStream = new FileOutputStream(file);
//
//                outputStream.write(statuses.toString().getBytes());
//                // ちなみにDDMSで確認したところ、確認時の環境下では
//                // "/data/data/[パッケージ名]/cache/cache.txt"
//                // に書き込まれた。
//
//            }
//        } catch (IOException e) {
//            Log.e("MyApp", "exception", e);
//        } finally {
//            try {
//                if (outputStream != null) {
//                    outputStream.close();
//                }
//            } catch (IOException e) {
//                Log.e("MyApp", "exception", e);
//            }
//        }
//    }
}
