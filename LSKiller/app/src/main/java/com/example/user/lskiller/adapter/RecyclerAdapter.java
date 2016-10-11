package com.example.user.lskiller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.user.lskiller.OnRecyclerListener;
import com.example.user.lskiller.R;
import com.loopj.android.image.SmartImageView;

import java.util.List;

import twitter4j.MediaEntity;
import twitter4j.Status;

/**
 * Created by USER on 2016/10/05.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private LayoutInflater mInflater;
    private List<twitter4j.Status> statuses;
    private List<String> mediaList;
    private Context mContext;
    private OnRecyclerListener mListener;

    public RecyclerAdapter(Context context,List<String> mediaList, List<twitter4j.Status> data, OnRecyclerListener listener) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        this.mediaList = mediaList;
        statuses = data;
        mListener = listener;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new RecyclerViewHolder(mInflater.inflate((R.layout.item_timeline), parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {

        holder.screenName.setText("@" + statuses.get(position).getUser().getScreenName());
        holder.name.setText(statuses.get(position).getUser().getName());
        holder.textView.setText(statuses.get(position).getText());
        holder.icon.setImageUrl(statuses.get(position).getUser().getProfileImageURL());

        setMedias(holder, position);
        holder.itemView.setClickable(true);

        // クリック処理
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRecyclerClicked(v, position);
            }
        });
    }

    private void setMedias(RecyclerViewHolder holder, int position) {
        holder.gridLayout.setColumnCount(2);
        holder.gridLayout.setRowCount(2);
        if (holder.gridLayout.getChildCount() > 0) {
            holder.gridLayout.removeAllViews();
        }

        Status status = statuses.get(position);

        /** 画像の取得 */
        MediaEntity[] mediaEntities = status.getExtendedMediaEntities();
        /** 画像が含まれていない場合はもちろん処理をスルーします. */
        if (mediaEntities.length <= 0) {
            return;
        }

        /** 画像を表示する処理 */
        for (int i = 0; i < mediaEntities.length; i++) {
            /** URLから画像セット(contextはもちろんandroid.content.Contextクラスのインスタンスです) */
            SmartImageView imageView = new SmartImageView(mContext);
            imageView.setImageUrl(mediaEntities[i].getMediaURL());

            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            /** 画像はリサイズしないので両方ともwrap_contentで. */
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 300;
            params.height = 300;
            /** 画像と画像の間を少し空ける. */
            params.setMarginEnd(15);
            imageView.setLayoutParams(params);

            /** 画像表示用のレイアウトに突っ込む. */
            holder.gridLayout.addView(imageView, params);
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
