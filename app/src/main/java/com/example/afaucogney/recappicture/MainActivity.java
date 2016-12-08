package com.example.afaucogney.recappicture;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.fernandocejas.frodo.annotation.RxLogObservable;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    ImageView iv;
    FloatingActionButton fab;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        iv = (ImageView) findViewById(R.id.iv);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            pb.setVisibility(View.VISIBLE);
            fab.setEnabled(false);
            dothejob();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void dothejob() {
        getSub().subscribe(new Subscriber<Bitmap>() {
            @Override
            public void onCompleted() {
                Log.v("rx", "completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.v("rx", e.toString());
                e.printStackTrace();
            }

            @Override
            public void onNext(Bitmap bitmap) {
                iv.setImageBitmap(bitmap);
            }
        });
    }

    @RxLogObservable
    private Observable<Bitmap> getSub() {
        Observable<Bitmap> label = new ShareContentManager(this).getPhotoLabelObservable(StubUrlProvider.getMainPhotoUrl()).observeOn(Schedulers.computation());
        Observable<List<Bitmap>> photos = new ShareContentManager(this).getUserPhotosObservable(StubUrlProvider.getUserPhotoListUrls(20)).observeOn(Schedulers.computation());
        Observable<Bitmap> logo = new ShareContentManager(this).getPhotoLabelObservable(StubUrlProvider.getLogoUrl()).observeOn(Schedulers.computation());
        Observable<List<String>> tags = new ShareContentManager(this).getTags(StubUrlProvider.getTags()).observeOn(Schedulers.computation());
        return Observable
                .zip(
                        label,
                        photos,
                        logo,
                        tags,
                        (bitmap, bitmaps, mzulogo, tgs) -> new RxCanvas(this, 0, bitmap, bitmaps, mzulogo, tgs).getBitmap())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(() -> {
                    fab.setEnabled(true);
                    pb.setVisibility(View.INVISIBLE);
                });


    }


}
