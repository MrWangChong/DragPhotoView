package com.wc.dragphotoview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * 图片会配器
 * Created by RushKing on 2017/3/8.
 */

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.RecyclerViewHolder> {
    private List<ImageInfo> infos;
    private Context mContext;

    public DetailAdapter(Context context, List<ImageInfo> infos) {
        this.mContext = context;
        this.infos = infos;
    }

    // 获取数据的数量
    @Override
    public int getItemCount() {
        return infos.size();
    }

    // 创建新View，被LayoutManager所调用
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LinearLayout linearLayout = new LinearLayout(mContext);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(lp);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(20, 20, 20, 20);

        TextView textView = new TextView(mContext);
        RecyclerView recyclerView = new RecyclerView(mContext);
        linearLayout.addView(textView);
        linearLayout.addView(recyclerView);

        return new RecyclerViewHolder(linearLayout);
    }

    // 将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, int position) {
        viewHolder.text.setText(infos.get(position).title);
        Log.v("DetailAdapter", infos.get(position).title);
        viewHolder.recycler.setLayoutManager(new GridLayoutManager(mContext, 4));
        viewHolder.recycler.setAdapter(new ImageAdapter(infos.get(position).images));
    }

    // 自定义的ViewHolder，持有每个Item的的所有界面元素
    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        //商品详情
        TextView text;
        RecyclerView recycler;

        RecyclerViewHolder(View view) {
            super(view);
            text = (TextView) ((LinearLayout) view).getChildAt(0);
            recycler = (RecyclerView) ((LinearLayout) view).getChildAt(1);
        }
    }

    private class ImageAdapter extends RecyclerView.Adapter<DetailAdapter.ImageViewHolder> {
        private String[] images;
        private ImageView[] imageViews;

        ImageAdapter(String[] images) {
            this.images = images;
        }

        // 获取数据的数量
        @Override
        public int getItemCount() {
            return images.length;
        }

        // 创建新View，被LayoutManager所调用
        @Override
        public ImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            ImageView image = new ImageView(mContext);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(250, 250);
            lp.setMargins(0, 4, 0, 4);
            image.setLayoutParams(lp);
            return new ImageViewHolder(image);
        }

        // 将数据与界面进行绑定的操作
        @Override
        public void onBindViewHolder(ImageViewHolder viewHolder, int position) {
            if (imageViews == null) {
                imageViews = new ImageView[images.length];
            }
            imageViews[position] = viewHolder.image;

            showImage(images[position], viewHolder.image);

            final int currentPosition = position;
            viewHolder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //List转[] --- List.toArray(new String[List.size()])
                    ImageShowActivity.startImageActivity((Activity) mContext, imageViews, images, currentPosition);
                }
            });
        }

    }

    // 自定义的ViewHolder，持有每个Item的的所有界面元素
    class ImageViewHolder extends RecyclerView.ViewHolder {
        //商品详情
        ImageView image;

        ImageViewHolder(View view) {
            super(view);
            image = (ImageView) view;
        }
    }

    /**
     * 显示图片
     */
    public void showImage(String url, ImageView imageView) {
        if (!TextUtils.isEmpty(url) && url.length() > 0) {
            url = url.replaceAll(" ", "");
        }
        if (TextUtils.isEmpty(url)) {
            return;
        }
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(mContext).load(url)
                .dontAnimate()
                .placeholder(R.mipmap.ic_loding)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(R.mipmap.ic_loding_error)
                .into(imageView);
//        Picasso.with(this).load(url)
//                .error(R.mipmap.ic_loding_error)
//                .into(imageView);
    }
}