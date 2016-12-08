package com.example.afaucogney.recappicture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.DisplayMetrics;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fernandocejas.frodo.annotation.RxLogObservable;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by afaucogney on 09/11/2016.
 */

public class ShareContentManager {

    Canvas canvas;
    Context context;
    Bitmap bottleLabel;
    ArrayList<Bitmap> userPictures = new ArrayList<>();


    public ShareContentManager(Context context) {
        this.context = context;
        this.canvas = createEmptyCanvas(new Rect(0, 0, 100, 100));
    }

    private static Canvas createEmptyCanvas(Rect rect) {
        // Create a bitmap for the part of the screen that needs updating.
        Bitmap bitmap = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
        bitmap.setDensity(DisplayMetrics.DENSITY_DEFAULT);
        return new Canvas(bitmap);
    }

    private void drawBackgroundPicture(String backgroundPicturePath) {
//        Rect source = new Rect()

        Glide.with(context)
                .load(backgroundPicturePath)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                    }
                });
    }

    @RxLogObservable(RxLogObservable.Scope.EVERYTHING)
    public Observable<Bitmap> getPhotoLabelObservable(String url) {
        return RxGlide.downloadBitmap(Glide.with(context), url).subscribeOn(Schedulers.io());
    }

    @RxLogObservable(RxLogObservable.Scope.EVERYTHING)
    private Observable<Bitmap> getUserPhotoObservable(String url) {
        return RxGlide.downloadBitmap(Glide.with(context), url).subscribeOn(Schedulers.io());
    }

    @RxLogObservable(RxLogObservable.Scope.EVERYTHING)
    public Observable<List<Bitmap>> getUserPhotosObservable(List<String> urls) {
        return Observable
                .from(urls)
                .flatMap(this::getUserPhotoObservable)
                .toList();
    }


    public Observable<List<String>> getTags(List<String> tags) {
        return Observable.just(tags);
    }
}
