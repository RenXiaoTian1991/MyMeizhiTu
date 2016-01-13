package com.example.lenovo.ganhuoapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.ganhuoapp.Bean.MeiTuData;
import com.example.lenovo.ganhuoapp.adapter.RecycleAdapter;
import com.example.lenovo.ganhuoapp.base.BaseActivity;
import com.example.lenovo.ganhuoapp.engine.SpacesItemDecoration;
import com.example.lenovo.ganhuoapp.hostprtol.HostAddress;
import com.example.lenovo.ganhuoapp.utils.FileUtils;
import com.example.lenovo.ganhuoapp.utils.GsonUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.Buffer;
import java.util.ArrayList;

public class MainActivity extends BaseActivity implements RecycleAdapter.OnItemClick{

    private boolean mIsFirstTimeTouchBottom =false;
    private boolean isHiden = false;
    private MeiTuData mData;//大数据
    private static int PRELOAD_SIZE = 6;
    private int lastVisibleItem,firstVisibleItem;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<MeiTuData.ImgSource> arr = new ArrayList<MeiTuData.ImgSource>();//内部包含的所有的图片数据
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ListView listView;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager layoutManager;
    private int page = 1;
    private static final int STATE_REFRESH = 0;
    private static final int STATE_FIRST = 1;
    private static final int STATE_LOAD_MORE = 2;
    private int STATE = STATE_FIRST;
    RecycleAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar();
        showDialog();
        String cache = readCache(page);
        if(cache !=null){
            Toast.makeText(this,"复用了缓存",Toast.LENGTH_SHORT).show();
            initDataOnSucess(cache);
        }else{
            initDataNormal(HostAddress.main+page);
        }
    }

    private String readCache(int i) {
//        BufferedReader reader = null;
//        FileReader fr = null;
//        File file = new File(getCacheDir(),"home"+i);
//        if(!file.exists()){
//            return null;
//        }
//        try {
//             fr = new FileReader(file);
//            reader = new BufferedReader(fr);
//            long out = Long.parseLong(reader.readLine());
//            if(System.currentTimeMillis()>out){
//                file.delete();
//                return null;
//            }else{
//                String str = null;
//                StringWriter sw = new StringWriter();
//                while ((str = reader.readLine())!=null){
//                    sw.write(str);
//                }
//                Log.i("abc",sw.toString());
//                return sw.toString();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally{
//            try {
//                fr.close();
//                reader.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        File dir=FileUtils.getCacheDir(this);// 获取缓存所在的文件夹
        File file = new File(dir, "home" + i);
        try {
            FileReader fr=new FileReader(file);
            BufferedReader br=new BufferedReader(fr);
            long outOfDate = Long.parseLong(br.readLine());
            if(System.currentTimeMillis()>outOfDate){
                Toast.makeText(this,"缓存超时",Toast.LENGTH_SHORT).show();
                return null;
            }else{
                String str=null;
                StringWriter sw=new StringWriter();
                while((str=br.readLine())!=null){

                    sw.write(str);
                }
                return sw.toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void initDataOnSucess(String result) {
        if(dialog.isShowing()){
            dismissDialog();
        }
        saveCache(result, page);
        mData = GsonUtils.json2bean(result, MeiTuData.class);
        if(STATE == STATE_REFRESH){
            STATE = STATE_FIRST;
            if(swipeRefreshLayout.isRefreshing()){
                swipeRefreshLayout.setRefreshing(false);
            }
            arr.addAll(0,mData.getResults());
//            for(MeiTuData.ImgSource data:mData.getResults()){
//                arr.add(0,data);
//            }
            adapter.notifyDataSetChanged();
        }else if(STATE == STATE_FIRST){
            if(mData !=null && !mData.getError().equals(false)){
                arr = mData.getResults();
                if(adapter ==null){
                    adapter = new RecycleAdapter(arr,this,imageLoader,options);
                    adapter.setLitener(this);
                }
                layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                SpacesItemDecoration decoration=new SpacesItemDecoration(10);
                recyclerView.addItemDecoration(decoration);
            }
        }else if(STATE == STATE_LOAD_MORE){
            arr.addAll(mData.getResults());
            if(swipeRefreshLayout.isRefreshing() == true){
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    @Override
    protected void initDataOnError(String error) {
        Toast.makeText(this,"请求数据失败",Toast.LENGTH_SHORT).show();
        if(!dialog.isShowing()){
            dismissDialog();
        }
        if(swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void initToolBar(){
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerlayout);
        toolbar.setTitle("妹子图片");
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, toolbar, R.string.open, R.string.close);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        getSupportActionBar().setHomeButtonEnabled(false);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        Toast.makeText(MainActivity.this, "action_settings", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_share:
//                        Intent intent = new Intent(Intent.ACTION_SEND);
//                        intent.setType("text/plain");
//                        intent.putExtra(Intent.EXTRA_TEXT, "推荐你看一下");
//                        startActivity(Intent.createChooser(intent,"选择分享"));
                        Toast.makeText(MainActivity.this, "action_settings", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                        intent.putExtra(Intent.EXTRA_TEXT, "I have successfully share my message through my app");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(Intent.createChooser(intent, getTitle()));
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }
    @Override
    protected int setContent() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipe_container);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerlayout);
        listView= (ListView) findViewById(R.id.left_drawer);
        //listview设置headview
        View view = LayoutInflater.from(this).inflate(R.layout.head_view,null);
        listView.addHeaderView(view);
        MyAdapter adapter = new MyAdapter(this,R.layout.item);
        recyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        //设置数据源
        adapter.add(new Str("第一个选项"));
        adapter.add(new Str("第二个选项"));
        adapter.add(new Str("第三个选项"));
        adapter.add(new Str("第四个选项"));
        listView.setAdapter(adapter);
    }

    private void saveCache(String json,int i){
//        File cache = FileUtils.getCacheDir(this);
//        File file = new File(cache,"home"+i);
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(file);
//            String str = System.currentTimeMillis()+1000*100+"\n\r";
//            fos.write(str.getBytes());
//            fos.write(json.getBytes());
//            Toast.makeText(this,"写了数据",Toast.LENGTH_SHORT).show();
//            fos.flush();
//            fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally{
//        }
        BufferedWriter bw = null;
        try {
            File dir=FileUtils.getCacheDir(this);
            //在第一行写一个过期时间
            File file = new File(dir, "home" + i); // /mnt/sdcard/googlePlay/cache/home_0
            FileWriter fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            bw.write(System.currentTimeMillis() + 1000 * 60 + "");
            bw.newLine();// 换行
            bw.write(json);// 把整个json文件保存起来
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void setListeners() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                  new Thread(new Runnable() {
                      @Override
                      public void run() {
                          try {
                              Thread.sleep(2000);
                              MainActivity.this.runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      Toast.makeText(MainActivity.this, "刷新成功",
                                              Toast.LENGTH_SHORT).show();
                                      swipeRefreshLayout.setRefreshing(false);
                                  }
                              });
                          } catch (InterruptedException e) {
                              e.printStackTrace();
                          }
                      }
                  }).start();

//                /**
//                 * 上啦刷新数据
//                 */
//                STATE = STATE_REFRESH;
//                page++;
//                initDataNormal(HostAddress.main+page);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int totalCount = layoutManager.getItemCount();
                Log.e("总的数目", totalCount + "");
                Log.e("滚动的状态", newState + "");
                //这个就是判断当前滑动停止了，并且获取当前屏幕最后一个可见的条目是第几个，当前屏幕数据已经显示完毕的时候就去加载数据
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == layoutManager.getItemCount()) {
                    //回调加载更多
                    swipeRefreshLayout.setRefreshing(true);
                    STATE = STATE_LOAD_MORE;
                    page++;
                    String cache = readCache(page);
                    if(cache !=null){
                        Toast.makeText(MainActivity.this,"第"+page+"复用缓存",Toast.LENGTH_SHORT).show();
                        initDataOnSucess(cache);
                    }else{
                        initDataNormal(HostAddress.main + page);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                int[] a = layoutManager.findLastCompletelyVisibleItemPositions(new int[2]);
//                if (a[1] >= layoutManager.getItemCount() - 2) {
//                    Toast.makeText(MainActivity.this, "滑动到底部",
//                            Toast.LENGTH_SHORT).show();
//                }
                StaggeredGridLayoutManager staggeredGridLayoutManager = ((StaggeredGridLayoutManager) layoutManager);
                //因为StaggeredGridLayoutManager的特殊性可能导致最后显示的item存在多个，所以这里取到的是一个数组
                //得到这个数组后再取到数组中position值最大的那个就是最后显示的position值了
                int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                lastVisibleItem = findMax(lastPositions);
                firstVisibleItem = staggeredGridLayoutManager.findFirstVisibleItemPositions(lastPositions)[0];
            }
        });
    }

    //找到数组中的最大值

    private int findMax(int[] lastPositions) {

        int max = lastPositions[0];
        for (int value : lastPositions) {
            //       int max    = Math.max(lastPositions,value);
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MainActivity.this,DetialActivity.class);
        intent.putExtra("url",arr.get(position).getUrl());
        startActivity(intent);
    }


    class MyAdapter extends ArrayAdapter<Str> {
        private int mResourceId;
        public MyAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
            this.mResourceId = textViewResourceId;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Str user = getItem(position);
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(mResourceId, null);
            TextView nameText = (TextView) view.findViewById(R.id.textView);
            nameText.setText(user.getTxt());
            return view;
        }
    }

    public class Str{
        public String txt;

        public Str(String txt) {
            this.txt = txt;
        }

        public  String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuaaa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST + 1:
                Toast.makeText(this, "删除菜单被点击了", Toast.LENGTH_LONG).show();
                break;
            case Menu.FIRST + 2:
                Toast.makeText(this, "保存菜单被点击了", Toast.LENGTH_LONG).show();
                break;

        }
        return false;
    }

}
