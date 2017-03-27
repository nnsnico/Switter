package com.example.user.lskiller.presentation.view.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.example.user.lskiller.domain.usecase.OnRecyclerListener;
import com.example.user.lskiller.R;
import com.example.user.lskiller.presentation.view.component.RecyclerViewHolder;
import com.loopj.android.image.SmartImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.User;
import twitter4j.util.TimeSpanConverter;

/**
 * Created by USER on 2016/10/05.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private LayoutInflater mInflater;
    private ArrayList<Status> statuses;
    private Context mContext;
    private OnRecyclerListener mListener;
    private TimeSpanConverter timeSpan = new TimeSpanConverter(Locale.JAPAN);

    public RecyclerAdapter(
            Context context,
            List<Status> data,
            OnRecyclerListener listener
    ) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        statuses = (ArrayList<Status>) data;
        mListener = listener;
//        Twitter mTwitter = TwitterUtils.getTwitterInstance(context);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new RecyclerViewHolder(mInflater.inflate((R.layout.item_timeline), parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        /** リツイートされたステータスか確認 */
        Status targetSta = statuses.get(position);

        if (!statuses.get(position).isRetweet()) {
            /** リツイートされていない（通常のタイムライン） */
            holder.screenName.setText(String.format("@%s",
                    targetSta.getUser().getScreenName()));
            holder.name.setText(targetSta.getUser().getName());
            holder.textView.setText(targetSta.getText());
            Picasso.with(mContext)
                    .load(targetSta.getUser().getOriginalProfileImageURL())
                    .into(holder.icon);
            holder.createTime.setText(timeSpan.toTimeSpanString(targetSta.getCreatedAt()));
            holder.reTweetedUser.setText("");
            setMedias(holder, position);
        } else {
            /** リツイートされたユーザをタイムラインに差し替える */
            Status retStatus = statuses.get(position).getRetweetedStatus();

            holder.screenName.setText(
                    String.format("@%s", retStatus.getUser().getScreenName()));
            holder.name.setText(retStatus.getUser().getName());
            holder.textView.setText(retStatus.getText());
            Picasso.with(mContext)
                    .load(retStatus.getUser().getOriginalProfileImageURL())
                    .into(holder.icon);
            holder.createTime.setText(timeSpan.toTimeSpanString(retStatus.getCreatedAt()));
            /** リツイート元の名前を挿入 */
            holder.reTweetedUser.setText(
                    String.format("%sさんがRTしました",
                            targetSta.getUser().getName()));
            setMedias(holder, position);
        }

        holder.itemView.setClickable(true);

        setSwipeMenu(holder, position);

    }

    // TimeLineActivity側で処理
    private void setSwipeMenu(final RecyclerViewHolder holder, final int position) {
        // ShowMode
        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        // Add Drag Menu
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right,
                holder.swipeLayout.findViewWithTag("swipe_menu_right"));
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left,
                holder.swipeLayout.findViewWithTag("swipe_menu_left"));
        // pushed Icon
        holder.swipeLayout.findViewById(R.id.goProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRecyclerClicked("goPro", statuses, position);
                holder.swipeLayout.close();
            }
        });
        holder.swipeLayout.findViewById(R.id.favorite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onRecyclerClicked("fav", statuses, position);
                holder.swipeLayout.close();
            }
        });

        holder.swipeLayout.findViewById(R.id.reTweet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onRecyclerClicked("ret", statuses, position);
                holder.swipeLayout.close();
            }
        });

        holder.swipeLayout.findViewById(R.id.reply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onRecyclerClicked("rep", statuses, position);
                holder.swipeLayout.close();
            }
        });
    }

    private void setMedias(final RecyclerViewHolder holder, final int position) {
        holder.gridLayout.setColumnCount(2);
        holder.gridLayout.setRowCount(2);
        if (holder.gridLayout.getChildCount() > 0) {
            holder.gridLayout.removeAllViews();
        }

        Status status = statuses.get(position);

        /** 画像の取得 */
        final MediaEntity[] mediaEntities = status.getExtendedMediaEntities();
        /** 画像が含まれていない場合はもちろん処理をスルーします. */
        if (mediaEntities.length <= 0) {
            return;
        }

        int count = 0;
        /** 画像を表示する処理 */
        for (final MediaEntity mediaEntity : mediaEntities) {
            /** URLから画像セット(contextはもちろんandroid.content.Contextクラスのインスタンスです) */
            final SmartImageView imageView = new SmartImageView(mContext);
            imageView.setImageUrl(mediaEntity.getMediaURL());

            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(0, 0, 0, 32);

            /** 画像はリサイズしないので両方ともwrap_contentで. */
            final GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 300;
            params.height = 300;
            /** 画像と画像の間を少し空ける. */
            params.setMarginEnd(15);
            imageView.setLayoutParams(params);

            final int finalCount = count;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onRecyclerClicked("img", mediaEntities, finalCount);
                    Log.d("MediaUrl", mediaEntity.getMediaURL());
                    Log.d("ExpandedUrl", mediaEntity.getExpandedURL());
                }
            });
            /** 画像表示用のレイアウトに突っ込む. */
            holder.gridLayout.addView(imageView, params);
            count++;
        }
    }

    @Override
    public int getItemCount() {
        if (statuses != null) {
            return statuses.size();
        } else {
            return 0;
        }
    }
}