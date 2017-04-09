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
            info.images[0] = "http://www.sinaimg.cn/dy/slidenews/21_img/2011_28/2236_417251_177498.jpg";
            info.images[1] = "http://ws1.cdn.caijing.com.cn/2014-04-21/114115643.jpg";
            info.images[2] = "http://tx2.cdn.caijing.com.cn/2014-03-20/114024238.jpg";
            info.images[3] = "http://attach.bbs.miui.com/forum/201305/30/180120x6yc1619nqyqsu69.jpg";
            info.images[4] = "http://image.tianjimedia.com/uploadImages/2014/016/0EK18083OW6A.jpg";
            info.images[5] = "http://img.hb.aicdn.com/a9ab7c21de0bd30fe705f3a665219b37636e80031396e-fgw184_fw658";
            info.images[6] = "http://p0.so.qhmsg.com/t018224136263626b7f.jpg";
            infos.add(info);
        }

        recyclerview.setAdapter(new DetailAdapter(this, infos));
    }
}
