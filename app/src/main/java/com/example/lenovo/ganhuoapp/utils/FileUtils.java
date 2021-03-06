package com.example.lenovo.ganhuoapp.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by lenovo on 2015/11/25.
 */
public class FileUtils {
        public static final String CACHE = "cache";
        public static final String ICON = "icon";
        public static final String ROOT = "GanHuoApp";
        /**
         * 获取图片的缓存的路径
         * @return
         */
        public static File getIconDir(Context context){
            return getDir(context,ICON);

        }
        /**
         * 获取缓存路径
         * @return
         */
        public static File getCacheDir(Context context) {
            return getDir(context,CACHE);
        }
        public static File getDir(Context context,String cache) {
            StringBuilder path = new StringBuilder();
            if (isSDAvailable()) {
                path.append(Environment.getExternalStorageDirectory()
                        .getAbsolutePath());
                path.append(File.separator);// '/'
                path.append(ROOT);// /mnt/sdcard/GooglePlay
                path.append(File.separator);
                path.append(cache);// /mnt/sdcard/GooglePlay/cache

            }else{
                File filesDir = context.getCacheDir();    //  cache  getFileDir file
                path.append(filesDir.getAbsolutePath());// /data/data/com.itheima.googleplay/cache
                path.append(File.separator);///data/data/com.itheima.googleplay/cache/
                path.append(cache);///data/data/com.itheima.googleplay/cache/cache
            }
            File file = new File(path.toString());
            if (!file.exists() || !file.isDirectory()) {
                file.mkdirs();// 创建文件夹
            }
            return file;

        }

        private static boolean isSDAvailable() {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                return true;
            } else {
                return false;
            }
        }
}
