package com.example.lenovo.ganhuoapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.lenovo.ganhuoapp.Bean.MeiTuData;
import com.example.lenovo.ganhuoapp.R;
import com.example.lenovo.ganhuoapp.engine.MySingleTon;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.deque.LinkedBlockingDeque;

import java.util.List;

/**
 * Created by lenovo on 2015/11/3.
 */
public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder> {
    private OnItemClick listener;
    private List<MeiTuData.ImgSource> data;
    private Context context;
    private ImageLoader loader;
    private DisplayImageOptions options;

    public RecycleAdapter(List<MeiTuData.ImgSource> data,Context context,ImageLoader loader,DisplayImageOptions options) {
        this.data = data;
        this.context = context;
        this.loader = loader;
        this.options= options;
    }

    public void setLitener(OnItemClick listener){
        this.listener = listener;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycle,parent,false);
        return new MyViewHolder(view);
    }

    public interface OnItemClick{
        public void onItemClick(int position);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });
       loader.displayImage(data.get(position).getUrl(), holder.img, options);
        holder.txt.setText(data.get(position).getWho());

    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout ll;
        private ImageView img;
        private TextView txt;
        public MyViewHolder(View itemView) {
            super(itemView);
            ll = (LinearLayout) itemView.findViewById(R.id.ll_item);
            img = (ImageView) itemView.findViewById(R.id.img);
            txt = (TextView) itemView.findViewById(R.id.txt);
        }
    }
}

