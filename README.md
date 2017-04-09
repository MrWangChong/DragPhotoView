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

图片控件取自Google工作人员的开源控件[PhotoView](https://github.com/chrisbanes/PhotoView)

参考计算和处理方法取自Wing神的[DragPhotoView](https://github.com/githubwing/DragPhotoView)  

PhotoView2.0.0在我的编译器上使用不了，强迫症使用最新的我就把源码考了过来，总共就只有5个类6个接口

Wing神没有处理ViewPager的横向滑动冲突以及多图片返回事件，于是我做了一点修改，用到了项目中。

图片加载使用的Google推荐的[Glide](https://github.com/bumptech/glide)
