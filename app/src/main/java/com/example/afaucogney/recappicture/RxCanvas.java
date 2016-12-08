package com.example.afaucogney.recappicture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;

import java.util.List;

/**
 * Created by afaucogney on 10/11/2016.
 */

public class RxCanvas {

    ///////////////////////////////////////////////////////////////////////////
    // CONST
    ///////////////////////////////////////////////////////////////////////////

    public static final int FACEBOOK_SHARE = 1;
    public static final int TWITTER_PREVIEW_SHARE = 2;
    public static final int TWITTER_EXPANDED_SHARE = 3;
    public static final int INSTAGRAM_SHARE = 4;
    public static final int MARGE = 10;
    public static final int SIZE_UNIT = 40;

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    Context context;
    Canvas canvas;
    int type;
    Paint paint;
    Bitmap result;

    ///////////////////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    ///////////////////////////////////////////////////////////////////////////

    public RxCanvas(Context context, int type) {
        this.context = context;
        this.canvas = createEmptyCanvas(getSocialNetworkOptimalPictureSize(FACEBOOK_SHARE).width(), getSocialNetworkOptimalPictureSize(FACEBOOK_SHARE).height());
        this.result = Bitmap.createBitmap(getSocialNetworkOptimalPictureSize(FACEBOOK_SHARE).width(), getSocialNetworkOptimalPictureSize(FACEBOOK_SHARE).height(), Bitmap.Config.RGB_565);
        this.canvas.setBitmap(this.result);
        this.type = type;
        this.paint = new Paint();
    }

    public RxCanvas(Context context, int i, Bitmap bitmap, List<Bitmap> bitmaps, Bitmap logo, List<String> tags) {
        this(context, i);
        drawBackgroundPicture(bitmap);
        drawUsersPicture(bitmaps);
        drawWineName("Mon gros pinard qui tache");
        drawStar(5);
        drawLogo(logo);
        drawTags(tags);
    }

    ///////////////////////////////////////////////////////////////////////////
    // ACCESSORS
    ///////////////////////////////////////////////////////////////////////////

    public Bitmap getBitmap() {
        return result;
    }

    ///////////////////////////////////////////////////////////////////////////
    // CREATION
    ///////////////////////////////////////////////////////////////////////////

    private static Canvas createEmptyCanvas(int width, int height) {
        // Create a bitmap for the part of the screen that needs updating.
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setDensity(DisplayMetrics.DENSITY_DEFAULT);
        return new Canvas(bitmap);
    }


    public void drawBackgroundPicture(Bitmap backgroundBitmap) {
        Rect bitmqpRect = new Rect(0, 0, backgroundBitmap.getWidth(), backgroundBitmap.getHeight());
        Rect canvasRect = getSocialNetworkOptimalPictureSize(FACEBOOK_SHARE);

        double bitmapRatio = bitmqpRect.width() / bitmqpRect.height();
        double canvasRatio = canvasRect.width() / canvasRect.height();

        if (bitmapRatio >= canvasRatio) {
            cropWidth(canvas, backgroundBitmap, canvasRatio, paint);
        } else {
            cropHeight(canvas, backgroundBitmap, canvasRatio, paint);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // LOGO
    ///////////////////////////////////////////////////////////////////////////

    private void drawLogo(Bitmap bitmap) {
        Rect logoRect = new Rect(0, 0, SIZE_UNIT, SIZE_UNIT);
        Point logoPoint = new Point(MARGE + SIZE_UNIT / 2, MARGE + SIZE_UNIT / 2);
        drawCemterBitmap(bitmap, logoPoint, logoRect);
    }

    ///////////////////////////////////////////////////////////////////////////
    // AVATARS
    ///////////////////////////////////////////////////////////////////////////

    public void drawUsersPicture(List<Bitmap> userPictures) {
        Rect bitmapRect = new Rect(0, 0, SIZE_UNIT, SIZE_UNIT);
        Rect userPhotoRect = new Rect(0, 0, SIZE_UNIT, canvas.getHeight() - (2 * MARGE));

        int subHeight = userPhotoRect.height() / (userPictures.size() + 1);

        for (int i = 0; i < userPictures.size(); i++) {
            Point p = new Point(canvas.getWidth() - MARGE - SIZE_UNIT / 2, MARGE + (i + 1) * subHeight);
            drawCemterBitmap(getCroppedBitmap(userPictures.get(i)), p, bitmapRect);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // TITLE
    ///////////////////////////////////////////////////////////////////////////

    public void drawWineName(String label) {
        Rect titleRect = new Rect(MARGE + SIZE_UNIT, MARGE, canvas.getWidth() - MARGE - SIZE_UNIT, MARGE + SIZE_UNIT);
        Paint p = new Paint(paint);
        p.setColor(ContextCompat.getColor(context, R.color.colorAccent));
        p.setStrokeWidth(2);
        p.setTextSize(20);
        drawDebugRect(titleRect);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        PointF po = getTextCenterToDraw(label, titleRect, p);
        canvas.drawText(label, po.x, po.y, p);
    }

    ///////////////////////////////////////////////////////////////////////////
    // STARS
    ///////////////////////////////////////////////////////////////////////////

    public void drawStar(int level) {
        int starCount = 6;

        Rect starsRect = new Rect(MARGE + SIZE_UNIT, canvas.getHeight() - MARGE - SIZE_UNIT, canvas.getWidth() - MARGE - SIZE_UNIT, canvas.getHeight() - MARGE);
        drawDebugRect(starsRect);
        int starWidth = Math.min(starsRect.height(), starsRect.width() / starCount);

        Rect starRect = new Rect(0, 0, starWidth, starWidth);

        int remainingFullWidth = starsRect.width() - starCount * starWidth;
        int widthSwitch = remainingFullWidth / (starCount + 1);
        int halfWidth = starRect.width() / 2;

        Paint pStar = new Paint(paint);

        for (int i = 0; i < 6; i++) {
            Point p = new Point(starsRect.left + widthSwitch + (widthSwitch + starRect.width()) * (i), starsRect.top);
            if (i < level) {
                pStar.setColor(ContextCompat.getColor(context, R.color.colorAccent));
            } else {
                pStar.setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            }
            drawStar(p, starRect, true, pStar);
        }
    }

    public void drawStar(Point p, Rect starRect, boolean filled, Paint paint) {
        Path star = createStarBySyze(starRect.height(), 5);
        canvas.save();
        canvas.translate(p.x, p.y);
        canvas.drawPath(star, paint);
        canvas.restore();
    }


    private Path createStarBySyze(float width, int steps) {
        float halfWidth = width / 2.0F;
        float bigRadius = halfWidth;
        float radius = halfWidth / 2.0F;
        float degreesPerStep = (float) Math.toRadians(360.0F / (float) steps);
        float halfDegreesPerStep = degreesPerStep / 2.0F;
        Path ret = new Path();
        ret.setFillType(Path.FillType.EVEN_ODD);
        float max = (float) (2.0F * Math.PI);
        ret.moveTo(width, halfWidth);
        for (double step = 0; step < max; step += degreesPerStep) {
            ret.lineTo((float) (halfWidth + bigRadius * Math.cos(step)), (float) (halfWidth + bigRadius * Math.sin(step)));
            ret.lineTo((float) (halfWidth + radius * Math.cos(step + halfDegreesPerStep)), (float) (halfWidth + radius * Math.sin(step + halfDegreesPerStep)));
        }
        ret.close();
        return ret;
    }

    ///////////////////////////////////////////////////////////////////////////
    // TAGS
    ///////////////////////////////////////////////////////////////////////////


    private void drawTag(Canvas canvas, Rect rect, String tag, Paint paint) {
//        canvas.drawText(tag, rect.left + MARGE, rect.centerY(), paint);
        PointF p = getTextCenterToDraw(tag, rect, paint);

        drawStartHalfCircle(canvas, rect, R.color.colorAccent);
        drawFilledRect(canvas, rect, R.color.colorPrimary);
        drawEndHalfCircle(canvas, rect, R.color.colorAccent);

        Paint pt = new Paint();
        pt.setStyle(Paint.Style.FILL_AND_STROKE);
        pt.setColor(ContextCompat.getColor(context, android.R.color.white));

        Rect rect1 = getMidleRectLessExtremityHalfSquare(rect);
        canvas.drawText(tag, rect1.centerX(), rect1.centerY(), pt);
    }

    public void drawTags(List<String> tags) {
        Rect tqgsREct = new Rect(0 + 2 * SIZE_UNIT, 0 + 2 * SIZE_UNIT, canvas.getWidth() - 2 * SIZE_UNIT, canvas.getHeight() - 2 * SIZE_UNIT);
        Paint p = new Paint(paint);
        p.setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));

        int height = tqgsREct.height() / tags.size();

        for (int i = 0; i < tags.size(); i++) {
            Rect tagRect = new Rect(tqgsREct.left, tqgsREct.top + (i * height), tqgsREct.right, tqgsREct.top + ((i + 1) * height));

            drawTag(canvas, tagRect, tags.get(i), p);

        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // CANVAS HELPERS
    ///////////////////////////////////////////////////////////////////////////

    private void drawCemterBitmap(Bitmap b, Point center, Rect rect) {
        Rect bRect = new Rect(0, 0, b.getWidth(), b.getHeight());
        canvas.save();

        canvas.translate(center.x - rect.width() / 2, center.y - rect.height() / 2);
        canvas.drawBitmap(b, bRect, rect, paint);
        canvas.restore();
    }

    private void drawStartHalfCircle(Canvas canvas, Rect rect, int colorRes) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(colorRes);
        canvas.clipRect(getStartHalfSquare(rect));

        Rect fr = getStartSquare(rect);
        canvas.drawCircle(fr.centerX(), fr.centerY(), fr.width() / 2, paint);
    }

    private void drawEndHalfCircle(Canvas canvas, Rect rect, int colorRes) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(colorRes);
        canvas.clipRect(getEndHalfSquare(rect));

        Rect er = getEndHalfSquare(rect);
        canvas.drawCircle(er.centerX(), er.centerY(), er.width() / 2, paint);
    }

    public void drawFilledRect(Canvas canvas, Rect rect, int colorRes) {
        Paint p = new Paint();
        p.setColor(ContextCompat.getColor(context, colorRes));
        p.setStyle(Paint.Style.FILL);
        canvas.drawRect(rect, p);
    }

    public void drawDebugRect(Rect rect) {
        Paint p = new Paint();
        p.setColor(ContextCompat.getColor(context, R.color.colorAccent));
        p.setStrokeWidth(2);
        p.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rect, p);
    }

    private static void cropHeight(Canvas canvas, Bitmap bitmap, double backgroundImageRatio, Paint paint) {
        int height = (int) (bitmap.getHeight() / backgroundImageRatio);
        int remainingHeight = bitmap.getHeight() - height;
        Rect src = new Rect(0, remainingHeight / 2, bitmap.getWidth(), height);
        Rect dst = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.drawBitmap(bitmap, src, dst, paint);
    }

    private static void cropWidth(Canvas canvas, Bitmap bitmap, double backgroundRation, Paint paint) {
        Rect src = new Rect(0, 0, (int) (bitmap.getWidth() / backgroundRation), bitmap.getHeight());
        Rect dst = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.drawBitmap(bitmap, src, dst, paint);
    }


    private void drawCenter(String text, Rect r, Paint paint) {
//        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.setTextAlign(Paint.Align.LEFT);
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f + r.left;
        float y = cHeight / 2f + r.height() / 2f + r.bottom;
        canvas.drawText(text, x, y, paint);
    }

    ///////////////////////////////////////////////////////////////////////////
    // MATHS HELPERS
    ///////////////////////////////////////////////////////////////////////////

    private Rect getStartSquare(Rect rect) {
        if (rect.width() > rect.height()) {
            return new Rect(rect.left, rect.top, rect.left + rect.height(), rect.bottom);
        } else if (rect.width() < rect.height()) {
            return new Rect(rect.left, rect.top, rect.right, rect.top + rect.width());
        } else {
            return rect;
        }
    }

    private Rect getEndSquare(Rect rect) {
        if (rect.width() > rect.height()) {
            return new Rect(rect.right - rect.height(), rect.top, rect.right, rect.bottom);
        } else if (rect.width() < rect.height()) {
            return new Rect(rect.left, rect.bottom - rect.width(), rect.right, rect.bottom);
        } else {
            return rect;
        }
    }

    private Rect getStartHalfSquare(Rect rect) {
        if (rect.width() > rect.height()) {
            return new Rect(rect.left, rect.top, rect.left + rect.height() / 2, rect.bottom);
        } else if (rect.width() < rect.height()) {
            return new Rect(rect.left, rect.top, rect.right, rect.top + rect.width() / 2);
        } else {
            return rect;
        }
    }

    private Rect getEndHalfSquare(Rect rect) {
        if (rect.width() > rect.height()) {
            return new Rect(rect.right - rect.height() / 2, rect.top, rect.right, rect.bottom);
        } else if (rect.width() < rect.height()) {
            return new Rect(rect.left, rect.bottom - rect.width() / 2, rect.right, rect.bottom);
        } else {
            return rect;
        }
    }

    private Rect getMidleRectLessExtremityHalfSquare(Rect rect) {
        if (rect.width() > rect.height()) {
            int widthReduction = rect.hashCode() / 2;
            return new Rect(rect.left + widthReduction, rect.top, rect.right - widthReduction, rect.bottom);
        } else if (rect.width() < rect.height()) {
            int heightReduction = rect.width() / 2;
            return new Rect(rect.left, rect.top + heightReduction, rect.right, rect.bottom - heightReduction);
        } else {
            return rect;
        }
    }

    private Rect getHorizontalSubRect(Rect mainRect, int subRectCount, int id) {
        int widthSwitch = mainRect.width() / subRectCount;
        return new Rect(id * widthSwitch, mainRect.top, (id + 1) * widthSwitch, mainRect.bottom);
    }


    ///////////////////////////////////////////////////////////////////////////
    // HELPERS
    ///////////////////////////////////////////////////////////////////////////

    private static Rect getSocialNetworkOptimalPictureSize(int socialNetworkId) {
        switch (socialNetworkId) {
            case FACEBOOK_SHARE:
                return new Rect(0, 0, 1200, 630);
            case TWITTER_PREVIEW_SHARE:
                return new Rect(0, 0, 440, 220);
            case TWITTER_EXPANDED_SHARE:
                return new Rect(0, 0, 440, 440);
            default:
                return new Rect(0, 0, 1200, 630);
        }
    }


    private static PointF getTextCenterToDraw(String text, Rect rItem, Paint paint) {
        Rect textBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);
        float x = rItem.centerX() - (textBounds.width() * 0.5f);
        float y = rItem.centerY() + (textBounds.height() * 0.5f);
        return new PointF(x, y);
    }

    public static float getTextHeight(String text, Paint paint) {
        Rect textBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);
        return textBounds.height();
    }


    public static float getTextWidth(String text, Paint paint) {
        Rect textBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);
        return textBounds.width();
    }

    ///////////////////////////////////////////////////////////////////////////
    // BITMAP HELPER
    ///////////////////////////////////////////////////////////////////////////

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

}
