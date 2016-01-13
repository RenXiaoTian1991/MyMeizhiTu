package com.example.lenovo.ganhuoapp.engine;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by lenovo on 2015/11/5.
 */
public class MySingleTon {
    private RequestQueue mRequestQueue;
    private Context mContext;
    private static MySingleTon mInstance;

    private ImageLoader imageLoader;

    public ImageLoader getImageLoader(){
        if(imageLoader == null){
            imageLoader = ImageLoader.getInstance();
        }
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(mContext);
        imageLoader.init(configuration);
        return imageLoader;
    }

    public MySingleTon(Context context) {
        this.mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static MySingleTon getmInstance(Context context){
        if (mInstance == null){
            synchronized (MySingleTon.class){
                if(mInstance == null){
                    mInstance = new MySingleTon(context);
                }
            }
        }
        return mInstance;
    }
    public RequestQueue getRequestQueue(){

        if(mRequestQueue == null){
            synchronized (RequestQueue.class){
                if(mRequestQueue == null){
                    mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
                }
            }
        }
        return mRequestQueue;
    }
}
