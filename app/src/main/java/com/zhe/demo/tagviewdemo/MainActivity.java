package com.zhe.demo.tagviewdemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zhe.library.tagview.CutImageUtil;
import com.zhe.library.tagview.TagsLayout;

public class MainActivity extends AppCompatActivity {

    private TagsLayout tagslayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.tagslayout = (TagsLayout) findViewById(R.id.tags_layout);
        CutImageUtil cutImageUtil = new CutImageUtil();
        Bitmap bitmap = cutImageUtil.getLocalBitmap("/storage/emulated/0/tencent/MicroMsg/WeiXin/1488803680472.jpg");

    }
}
