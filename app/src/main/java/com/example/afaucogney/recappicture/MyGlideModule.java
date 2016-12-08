package com.example.afaucogney.recappicture;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by afaucogney on 23/11/2016.
 */

public class MyGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // Apply options to the builder here.
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
//        // register ModelLoaders here.
//        OkHttpClient.Builder client = new OkHttpClient.Builder()
//                .connectTimeout(45, TimeUnit.SECONDS)
//                .readTimeout(45, TimeUnit.SECONDS)
//                .writeTimeout(45, TimeUnit.SECONDS);
//
//        OkHttpUrlLoader.Factory factory = new OkHttpUrlLoader.Factory(client.build());
//
//        glide.register(GlideUrl.class, InputStream.class, factory);
    }
}