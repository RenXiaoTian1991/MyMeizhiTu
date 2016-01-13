package com.example.lenovo.ganhuoapp;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.example.lenovo.ganhuoapp.base.BaseActivity;
import com.example.lenovo.ganhuoapp.view.TouchImageView;

/**
 * Created by lenovo on 2015/11/5.
 */
public class DetialActivity extends BaseActivity{
    private TouchImageView imageView;
    private String imgUrl;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //imgUrl = intent.getStringExtra("url");
    }

    @Override
    protected void initDataOnSucess(String result) {

    }

    @Override
    protected void initDataOnError(String error) {

    }

    @Override
    protected int setContent() {
        return R.layout.detial;
    }

    @Override
    protected void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置返回键按钮的作用
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        imageView = (TouchImageView) findViewById(R.id.iv_bigimg);
        imgUrl = getIntent().getStringExtra("url");
        Log.i("url",imgUrl);
        imageLoader.displayImage(imgUrl, imageView, options);
    }

    @Override
    protected void setListeners() {

    }
}
