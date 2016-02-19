package com.yk3372.sdk.network;

import android.app.ActivityManager;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

/**
 * Created by yukai on 16-2-22.
 */
public class HttpUtils {
    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;

    public static void init(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);

        int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
                .getMemoryClass();
        int cacheSize = 1024 * 1024 * memClass / 8;
        mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache((cacheSize)));
    }

    public static RequestQueue getRequestQueue() {
        if (null != mRequestQueue) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    public static ImageLoader getImageLoader() {
        if (null != mImageLoader) {
            return mImageLoader;
        } else {
            throw new IllegalStateException("ImageLoader not initialized");
        }
    }

    public static String getSync(String url) {
        RequestFuture<String> f = RequestFuture.newFuture();
        StringRequest req = new StringRequest(url, f, f);
        RequestQueue queue = getRequestQueue();
        queue.add(req);

        try {
            String response = f.get();
            return response;
        } catch (Exception e) {
            return "";
        }
    }

    public static void get(String url,
                           Response.Listener<String> listener,
                           Response.ErrorListener errorListener) {
        RequestQueue queue = getRequestQueue();
        queue.add(new StringRequest(url, listener, errorListener));
    }

    public static void post(String url,
                            final Map<String, String> params,
                            Response.Listener<String> listener,
                            Response.ErrorListener errorListener) {
        RequestQueue queue = getRequestQueue();
        StringRequest request = new StringRequest(Request.Method.POST, url, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        /**
         * prevent retry by set retry number to 0
         */
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 0, 1));
        queue.add(request);
    }

    public static void delete(String url,
                           Response.Listener<String> listener,
                           Response.ErrorListener errorListener) {
        RequestQueue queue = getRequestQueue();
        queue.add(new StringRequest(Request.Method.DELETE, url, listener, errorListener));
    }

    public static String getCache(String url) {
        RequestQueue queue = getRequestQueue();
        Cache.Entry cache = queue.getCache().get(url);

        if (null != cache) {
            return new String(cache.data);
        }

        return null;
    }

}