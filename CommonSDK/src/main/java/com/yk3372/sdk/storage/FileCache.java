package com.yk3372.sdk.storage;

import android.content.Context;
import android.os.Environment;

import com.yk3372.sdk.base.BaseApplication;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by yukai on 16-2-22.
 */
public class FileCache {

    private final static String AppNameDir = "/demo/";

    /**
     * init should adjust permission for M
     */
    public static void init() {
        mkDirs(getAppCacheDirectory());
        mkDirs(getImageCacheDirectory());
    }

    public static void mkDirs(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static String getRootDirectory() {
        Context context = BaseApplication.getInstance();
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return Environment.getExternalStorageDirectory().getPath();
        } else {
            return context.getCacheDir().toString();
        }
    }

    public static String getAppCacheDirectory() {
        return getRootDirectory() + AppNameDir;
    }

    public static String getImageCacheDirectory() {
        return getAppCacheDirectory() + "image_cache/";
    }

    public static void clear(String diskCacheDirectory) {
        File outFile = new File(diskCacheDirectory);
        outFile.mkdirs();
        boolean isDiskCacheEnabled = outFile.exists();
        if (isDiskCacheEnabled) {
            File cachedFiles[] = (new File(diskCacheDirectory)).listFiles();
            if (cachedFiles == null)
                return;
            File afile[];
            int j = (afile = cachedFiles).length;
            for (int i = 0; i < j; i++) {
                File f = afile[i];
                f.delete();
            }

        }
    }

    /**
     * 清除SD卡图片(3天前的图片)
     */
    public static void autoClearSDCardImage() {
        clearSDCardImage(3);
    }

    private static void deleteFile(File f) {
        f.delete();
    }

    /**
     * @param daysBefore 清理daysBefore天前的图片缓存
     */
    public static void clearSDCardImage(int daysBefore) {
        String imageCachePath = getImageCacheDirectory();

        Calendar time = Calendar.getInstance();
        time.add(Calendar.DAY_OF_MONTH, -daysBefore);

        long someDaysBefore = time.getTimeInMillis();

        File[] cachedFiles = new File(imageCachePath).listFiles();
        if (cachedFiles == null) {
            return;
        }
        for (File f : cachedFiles) {
            if (f.lastModified() < someDaysBefore) {
                deleteFile(f);
            }
        }

    }

    // 获取当前缓存大小
    public static String getCurrentCacheSize(Context context) {
        StringBuilder sb = new StringBuilder(6);
        String imageCachePath = getImageCacheDirectory();
        double size = getFileSize(new File(imageCachePath));
//      String webViewCachePath = context.getCacheDir().getAbsolutePath() + "/" + "webviewCache";
//      size+=getFileSize(new File(webViewCachePath));
        DecimalFormat df = new DecimalFormat();
        df.applyPattern("0.00");
        if (size < (1024 * 1024)) {
            sb.append(df.format(size / (1024))).append("KB");
        } else if (size < (1024 * 1024 * 1024)) {
            sb.append(df.format(size / (1024 * 1024))).append("MB");
        } else {
            sb.append(df.format(size / (1024 * 1024 * 1024))).append("GB");
        }
        return sb.toString();
    }

    // 递归
    public static long getFileSize(File f)// 取得文件夹大小
    {
        long size = 0;
        File flist[] = f.listFiles();
        if (flist == null) {
            return 0;
        }
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

//    @SuppressWarnings("unchecked")
//    public static ArrayList<ClassState> unserializeClassStateList(String classId) {
//        Object o = FileUtils.unserializeObject(getAppCacheDirectory() + classId + "class_state.dat");
//        return (ArrayList<ClassState>) o;
//    }
//
//    public static void serializeClassStateList(String classId, ArrayList<ClassState> itemAds) {
//        FileUtils.serializeObject(getAppCacheDirectory() + classId + "class_state.dat", itemAds);
//    }
}
