package com.wc.dragphotoview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wc.dragphoto.widget.DragPhotoView;

import java.util.ArrayList;

/**
 * 图片展示界面
 * Created by RushKing on 2017/4/7.
 */

public class ImageShowActivity extends AppCompatActivity {
    private FixMultiViewPager mViewPager;
    private String[] imageUrls;
    private ArrayList<ImageBean> imageBeans;
    private DragPhotoView[] mPhotoViews;
    private int mStatusHeight;
    private int currentPosition;

    private boolean isShowStatusBar;

    public static void startImageActivity(Activity activity, ImageView imageView, String imageUrl) {
        startImageActivity(activity, new ImageView[]{imageView}, new String[]{imageUrl}, 0);
    }

    public static void startImageActivity(Activity activity, ImageView[] imageViews, String[] imageUrls, int currentPosition) {
        Intent intent = new Intent(activity, ImageShowActivity.class);
//        ImageBean[] imageBeans = new ImageBean[imageViews.length];
        ArrayList<ImageBean> imageBeans = new ArrayList<>();
        for (ImageView imageView : imageViews) {
            ImageBean imageBean = new ImageBean();
            int location[] = new int[2];
            imageView.getLocationOnScreen(location);
            imageBean.left = location[0];
            imageBean.top = location[1];
            imageBean.width = imageView.getWidth();
            imageBean.height = imageView.getHeight();
//            imageBeans[i] = imageBean;
            imageBeans.add(imageBean);
        }
        intent.putParcelableArrayListExtra("imageBeans", imageBeans);
        intent.putExtra("currentPosition", currentPosition);
        intent.putExtra("imageUrls", imageUrls);

        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将window扩展至全屏，并且不会覆盖状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //避免在状态栏的显示状态发生变化时重新布局
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        setIsShowStatusBar(false);
        mStatusHeight = getStatusHeight();
        mViewPager = new FixMultiViewPager(this);
        setContentView(mViewPager);

        Intent intent = getIntent();
        currentPosition = intent.getIntExtra("currentPosition", 0);
        imageUrls = intent.getStringArrayExtra("imageUrls");
        imageBeans = intent.getParcelableArrayListExtra("imageBeans");
        if (imageUrls == null || imageUrls.length == 0) {
            return;
        }
        mPhotoViews = new DragPhotoView[imageUrls.length];
        setPhotoViewAndViewPager();
        //设置入场动画参数
        mViewPager.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            mViewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            mViewPager.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                        int left = imageBeans.get(currentPosition).left;
                        int top = imageBeans.get(currentPosition).top;
                        int height = imageBeans.get(currentPosition).height;
                        int width = imageBeans.get(currentPosition).width;

                        final DragPhotoView photoView = mPhotoViews[currentPosition];
                        int[] locationPhoto = new int[2];
                        photoView.getLocationOnScreen(locationPhoto);
                        float targetHeight = (float) photoView.getHeight();
                        float targetWidth = (float) photoView.getWidth();
                        float scaleX = (float) width / targetWidth;
                        float scaleY = (float) height / targetHeight;

                        float targetCenterX = locationPhoto[0] + targetWidth / 2;
                        float targetCenterY = locationPhoto[1] + targetHeight / 2;

                        float translationX = left + width / 2 - targetCenterX;
                        float translationY = top + height / 2 - targetCenterY;
                        photoView.setTranslationX(translationX);
                        photoView.setTranslationY(translationY);

                        photoView.setScaleX(scaleX);
                        photoView.setScaleY(scaleY);
                        photoView.performEnterAnimation(scaleX, scaleY);
                        for (DragPhotoView mPhotoView : mPhotoViews) {
                            mPhotoView.setMinScale(scaleX);
                        }
                    }
                });
    }

    private void setPhotoViewAndViewPager() {
        for (int i = 0; i < mPhotoViews.length; i++) {
            mPhotoViews[i] = new DragPhotoView(this);
            mPhotoViews[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            showImage(imageUrls[i], mPhotoViews[i]);
            mPhotoViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishWithAnimation();
                }
            });
            final int index = i;
            mPhotoViews[i].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Dialog dialog = new AlertDialog.Builder(ImageShowActivity.this)
                            .setTitle("长按Dialog").setMessage("这是第" + index + "个位置的图片")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.show();
                    return true;
                }
            });
            mPhotoViews[i].setOnDragListener(new DragPhotoView.OnDragListener() {
                @Override
                public void onDrag(DragPhotoView view, float moveX, float moveY) {
                    if (!isShowStatusBar) {
                        setIsShowStatusBar(true);
                    }
                }
            });
            mPhotoViews[i].setOnTapListener(new DragPhotoView.OnTapListener() {
                @Override
                public void onTap(DragPhotoView view) {
                    if (isShowStatusBar) {
                        setIsShowStatusBar(false);
                    }
                }
            });

            mPhotoViews[i].setOnExitListener(new DragPhotoView.OnExitListener() {
                @Override
                public void onExit(DragPhotoView view, float x, float y, float w, float h, int maxTranslateY) {
                    performExitAnimation(view, x, y, w, h);
//                    finish();
//                    overridePendingTransition(0, 0);
                }
            });
        }
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imageUrls.length;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(mPhotoViews[position]);
                return mPhotoViews[position];
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mPhotoViews[position]);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
        mViewPager.setCurrentItem(currentPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void performExitAnimation(final DragPhotoView view, float x, float y, float w, float h) {
        mPhotoViews[currentPosition].performExitAnimation(this, imageBeans.get(currentPosition).left, imageBeans.get(currentPosition).top,
                imageBeans.get(currentPosition).width, imageBeans.get(currentPosition).height);
    }

    private void finishWithAnimation() {
        if (!isShowStatusBar) {
            setIsShowStatusBar(true);
        }
        mPhotoViews[currentPosition].finishWithAnimation(this, imageBeans.get(currentPosition).left, imageBeans.get(currentPosition).top,
                imageBeans.get(currentPosition).width, imageBeans.get(currentPosition).height);
    }

    @Override
    public void onBackPressed() {
        finishWithAnimation();
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
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setAdjustViewBounds(true);
        Glide.with(this).load(url)
                .dontAnimate()
                .placeholder(R.mipmap.ic_loding)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(R.mipmap.ic_loding_error)
                .into(imageView);
//        Picasso.with(this).load(url)
//                .error(R.mipmap.ic_loding_error)
//                .into(imageView);
    }

    public int getStatusHeight() {
        int statusHeight = 0;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 不会导致Activity重新排版的全屏
     */
    /*
    关键是，在做了该Activity的全屏设置的前提下，还要在onCreate()方法中加入如下语句：
    //将window扩展至全屏，并且不会覆盖状态栏
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    //避免在状态栏的显示状态发生变化时重新布局
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
     */
    private void setIsShowStatusBar(boolean show) {
        isShowStatusBar = show;
        if (show) {
            //显示
            WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(attrs);
        } else {
            //隐藏
            WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(attrs);
        }
    }

    private static class ImageBean implements Parcelable {
        int top;
        int left;
        int width;
        int height;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.top);
            dest.writeInt(this.left);
            dest.writeInt(this.width);
            dest.writeInt(this.height);
        }

        private ImageBean() {
        }

        private ImageBean(Parcel in) {
            this.top = in.readInt();
            this.left = in.readInt();
            this.width = in.readInt();
            this.height = in.readInt();
        }

        public static final Creator<ImageBean> CREATOR = new Creator<ImageBean>() {
            @Override
            public ImageBean createFromParcel(Parcel source) {
                return new ImageBean(source);
            }

            @Override
            public ImageBean[] newArray(int size) {
                return new ImageBean[size];
            }
        };
    }


    /**
     * Author :  suzeyu
     * Time   :  2016-12-26  上午1:41
     * ClassDescription : 对多点触控场景时, {@link ViewPager#onInterceptTouchEvent(MotionEvent)}中
     * pointerIndex = -1. 发生IllegalArgumentException: pointerIndex out of range 处理
     */
    private class FixMultiViewPager extends ViewPager {
        private final String TAG = FixMultiViewPager.class.getSimpleName();

        public FixMultiViewPager(Context context) {
            super(context);
        }

        public FixMultiViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            try {
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "ACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "ACTION_UP");
                        break;
                }
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException ex) {
                Log.w(TAG, "onInterceptTouchEvent() ", ex);
                ex.printStackTrace();
            }
            return false;
        }

    }

    //设置不能在3秒内连续点击两次返回按钮
    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 3000) {
                mExitTime = System.currentTimeMillis();
            } else {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
