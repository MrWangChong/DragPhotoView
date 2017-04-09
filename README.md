### 图片展示界面

[![](https://jitpack.io/v/MrWangChong/DragPhotoView.svg)](https://jitpack.io/#MrWangChong/DragPhotoView)
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
	        compile 'com.github.MrWangChong:DragPhotoView:1.0.3'
	}
```

图片控件使用的[PhotoView](https://github.com/chrisbanes/PhotoView)


在Wing神的[DragPhotoView](https://github.com/githubwing/DragPhotoView)  基础上修改而来，里面使用到了[PhotoView](https://github.com/chrisbanes/PhotoView)
但是PhotoView 2.0.0在我的编译器上使用不了，强迫症使用最新的我就把源码考了过来


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