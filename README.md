### 图片展示界面

[![](https://jitpack.io/v/MrWangChong/DragPhotoView.svg)](https://jitpack.io/#MrWangChong/DragPhotoView)

## [下载APK体验](https://raw.githubusercontent.com/MrWangChong/DragPhotoView/master/apk/app-debug.apk)

### 引入
Step 1. Add the JitPack repository to your build file Add it in your root build.gradle at the end of repositories:
```gradle
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2. Add the dependency
```gradle
	dependencies {
	       compile 'com.github.MrWangChong:DragPhotoView:1.1.0'
	}
```


这是从Wing神的[DragPhotoView](https://github.com/githubwing/DragPhotoView)  基础上修改而来的，其中使用到了[PhotoView](https://github.com/chrisbanes/PhotoView)
但是PhotoView 2.0.0在我的编译器上使用不了，强迫症要使用最新的我就把源码考了过来


图片加载使用了Google推荐的[Glide](https://github.com/bumptech/glide)

### 效果图

![image](https://github.com/MrWangChong/DragPhotoView/blob/master/image/imageshow.gif)

使用就参照ImageShowActivity就行

在我的适配器中声明了一个ImageView[] imageViews，如果是单张图片就不需要，如果是多张图片就要用数组把列表中的图片保存起来。
```java
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
```

### ImageShowActivity

```java
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
```
这是ImageShowActivity里面的两个静态函数，跳转进来直接调用就行

因为点击返回键会有个结束动画，所以需要屏蔽多次点击返回键的事件,间隔时间最好是 动画时间3000秒
```java

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
```
一般展示图片都需要全屏,使用这个方法不会导致Activity重新排版
```java
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

```
```java
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
                .placeholder(R.mipmap.ic_loding_error)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(R.mipmap.ic_loding_error)
                .into(imageView);
//        Picasso.with(this).load(url)
//                .error(R.mipmap.ic_loding_error)
//                .into(imageView);
    }
```
还需要把Activity的主题设置为透明
manifest配置
```
    <!-- 显示图片界面 -->
    <activity
        android:name=".activity.ImageShowActivity"
        android:screenOrientation="portrait"
        android:theme="@style/translucent" />
```
styles配置
```
<resources>
      <style name="translucent" parent="Theme.AppCompat.Light.NoActionBar">
        <item name="android:windowBackground">@android:color/transparent</item>
        <item name="android:windowIsTranslucent">true</item>
    </style>
</resources>
```

总而言之，如果没有特殊需求，就把ImageShowActivity拷贝过来使用就行了
