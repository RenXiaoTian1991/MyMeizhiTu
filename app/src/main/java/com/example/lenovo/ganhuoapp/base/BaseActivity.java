package com.example.lenovo.ganhuoapp.base;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lenovo.ganhuoapp.Bean.MeiTuData;
import com.example.lenovo.ganhuoapp.R;
import com.example.lenovo.ganhuoapp.engine.MySingleTon;
import com.example.lenovo.ganhuoapp.utils.GsonUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

/**
 * Created by lenovo on 2015/11/5.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public Toolbar toolbar;
    public AlertDialog mAlertDialog;
    public ProgressDialog dialog;
    public ImageLoader imageLoader;
    public RequestQueue requestQueue;
    public DisplayImageOptions options;
    public FrameLayout fram;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        initParent();
        initView();
        setListeners();
    }


    public void showDialog(){
//        mAlertDialog.show();
//        Window window = mAlertDialog.getWindow();
//        window.setGravity(Gravity.CENTER);
//        //window.setWindowAnimations(R.style.dialogAnimation);
//        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog, new RelativeLayout(this), false);
//        window.setContentView(contentView);
        dialog = new ProgressDialog(this);
        //dialog.setTitle("加载中");
        dialog.setMessage("加载中");
        dialog.show();

    }

    public void dismissDialog(){
//        mAlertDialog.dismiss();
        dialog.dismiss();
    }

    public void initDataNormal(String url){
//        if(!dialog.isShowing()){
//            showDialog();
//        }
        JsonObjectRequest request = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        initDataOnSucess(jsonObject.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String str = volleyError.getMessage();
                initDataOnError(str);
            }
        });
        requestQueue.add(request);
    }

    /**
     * 请求服务器数据成功
     * @param result
     */
    protected abstract void initDataOnSucess(String result);

    /**
     * 请求服务器数据失败
     * @param error
     */
    protected abstract void initDataOnError(String error);

    private void initParent() {
        mAlertDialog = new AlertDialog.Builder(this).create();
        initDialog(mAlertDialog);
        imageLoader = MySingleTon.getmInstance(this).getImageLoader();
        requestQueue = MySingleTon.getmInstance(this).getRequestQueue();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fram = (FrameLayout) findViewById(R.id.content);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        View contentView = View.inflate(this,setContent(),null);
        fram.addView(contentView, params);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.menu_header)
                .showImageOnFail(R.drawable.ic_favorite)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if(mAlertDialog.isShowing()){
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initDialog(AlertDialog mAlertDialog) {
        mAlertDialog.setCanceledOnTouchOutside(false);
        mAlertDialog.setCancelable(false);
    }

    //设置内容布局
    protected abstract int setContent();
    // 初始化控件
    protected abstract void initView();
    //设置监听器
    protected abstract void setListeners();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
