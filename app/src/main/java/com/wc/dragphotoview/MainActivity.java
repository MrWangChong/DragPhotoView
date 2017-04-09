package com.wc.dragphotoview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        List<ImageInfo> infos = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            ImageInfo info = new ImageInfo();
            info.title = "这是一组美女图片" + "---item=" + i;
            info.images = new String[7];
            info.images[0] = "http://img4.duitang.com/uploads/item/201312/05/20131205171756_cfAFz.thumb.600_0.jpeg";
            info.images[1] = "http://v1.qzone.cc/skin/201601/17/17/21/569b5d317bdcb243.jpg%21600x600.jpg";
            info.images[2] = "http://img1.imgtn.bdimg.com/it/u=3429135274,3155470602&fm=214&gp=0.jpg";
            info.images[3] = "http://f.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=d3d48aea3987e9504242fb6825087f75/86d6277f9e2f07084decaf29ea24b899a901f22a.jpg";
            info.images[4] = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1491733739564&di=0a5eebc3dd3671e8fc56b1c704126b84&imgtype=0&src=http%3A%2F%2Fimg1.7wenta.com%2Fupload%2Fqa_headIcons%2F20140715%2F14054040879164181.jpg";
            info.images[5] = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1491733773628&di=dee740c0b4699a67763e0549ba638452&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fforum%2Fw%253D580%2Fsign%3Dab731cc778f0f736d8fe4c093a54b382%2Fac31272442a7d933c882cecbad4bd11372f001e5.jpg";
            info.images[6] = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1491733836847&di=f09d674b185d7d5954fd7108af6eacd3&imgtype=0&src=http%3A%2F%2Fimg5.duitang.com%2Fuploads%2Fitem%2F201312%2F05%2F20131205172036_TWSmB.jpeg";
            infos.add(info);
        }

        recyclerview.setAdapter(new DetailAdapter(this, infos));
    }
}
