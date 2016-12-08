package com.example.afaucogney.recappicture;


import android.graphics.Bitmap;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.Target;
import com.fernandocejas.frodo.annotation.RxLogObservable;

import java.io.File;

import rx.Observable;

public class RxGlide {

    @RxLogObservable(RxLogObservable.Scope.EVERYTHING)
    public static Observable<File> downloadFile(RequestManager rm, String url) {
        return downloadFile(rm, url, Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
    }

    @RxLogObservable(RxLogObservable.Scope.EVERYTHING)
    public static Observable<File> downloadFile(RequestManager rm, String url, int width, int height) {
        return Observable.defer(() -> {
            try {
                return Observable.just(rm.load(url).downloadOnly(width, height).get());
            } catch (Exception e) {
                return Observable.error(e);
            }
        });
    }
    @RxLogObservable(RxLogObservable.Scope.EVERYTHING)
    public static Observable<Bitmap> downloadBitmap(RequestManager rm, String url) {
        return downloadBitmap(rm, url, Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
    }
    @RxLogObservable(RxLogObservable.Scope.EVERYTHING)
    public static Observable<Bitmap> downloadBitmap(RequestManager rm, String url, int width, int height) {
        return Observable.defer(() -> {
            try {
                return Observable.just(rm.load(url).asBitmap().into(width, height).get());
            } catch (Exception e) {
                return Observable.error(e);
            }
        });
    }

}