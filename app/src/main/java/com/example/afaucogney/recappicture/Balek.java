package com.example.afaucogney.recappicture;

import android.util.Log;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by afaucogney on 10/11/2016.
 */

public class Balek {

    int a = 5;

    public Balek() {
    }


    public void doTheJob() {
        Observable
                .just(a)
                .flatMap(a ->
                        getB(a).flatMap(b ->
                                getC(a, b).flatMap(c ->
                                        Observable.fromCallable(() -> finalize(a, b, c)))
                        ))
                .subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.v("completed", "");
            }

            @Override
            public void onError(Throwable e) {
                Log.v("error", "");
            }

            @Override
            public void onNext(Integer integer) {
                Log.v("PROUT", "int :" + integer);
            }
        });
    }


    Observable<Integer> getB(int a) {
        Log.v("PROUT", "a :" + a);
        return Observable.just(a * 10);
    }

    Observable<Integer> getC(int a, int b) {
        Log.v("PROUT", "a :" + a);
        Log.v("PROUT", "B :" + b);
        return Observable.just(a + b);
    }

    Observable<Integer> getD(int a, int b, int c) {
        Log.v("PROUT", "a :" + a);
        Log.v("PROUT", "B :" + b);
        Log.v("PROUT", "c :" + c);
        return Observable.just(a + b + c);
    }

    int finalize(int a, int b, int c) {
        Log.v("PROUT", "a :" + a);
        Log.v("PROUT", "b :" + b);
        Log.v("PROUT", "c :" + c);
        return Integer.parseInt("" + a + b + c);
    }
}
