package org.heaven7.core.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * like android .RatingBar
 * @author heaven7
 */
public class StarRatingBar extends View {

    private Drawable mDefaultStar;
    private Drawable mStar;
    private int mDefaultStarColor;
    private int mStarColor;
    private int mStarNum;
    private float mStarStep;
    private int mStarGap;
    private int mStarSize;
    private float mRating;
    private boolean mIsIndicator;

    private Paint starPaint;

    public StarRatingBar(Context context) {
        this(context, null);
    }

    public StarRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StarRatingBar);
        mDefaultStar = typedArray.getDrawable(R.styleable.StarRatingBar_defaultStar);
        mStar = typedArray.getDrawable(R.styleable.StarRatingBar_star);
        mDefaultStarColor = typedArray.getColor(R.styleable.StarRatingBar_defaultStarColor, Color.GREEN);
        mStarColor = typedArray.getColor(R.styleable.StarRatingBar_starColor, Color.YELLOW);
        mStarNum = typedArray.getInteger(R.styleable.StarRatingBar_starNum, 5);
        mStarStep = typedArray.getFloat(R.styleable.StarRatingBar_starStep,  0.2f);
        mStarGap = typedArray.getDimensionPixelOffset(R.styleable.StarRatingBar_starGap, 10);
        mStarSize = typedArray.getDimensionPixelOffset(R.styleable.StarRatingBar_starSize, 80);
        mRating = typedArray.getFloat(R.styleable.StarRatingBar_rating, 1.5f);
        mIsIndicator = typedArray.getBoolean(R.styleable.StarRatingBar_isIndicator, true);
        typedArray.recycle();

        starPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        starPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        int radius = mStarSize / 2;
        canvas.translate(radius, radius);
        if (mDefaultStar != null) {
            drawStarDrawable(canvas, mDefaultStar, mStarNum);
        } else {
            starPaint.setColor(mDefaultStarColor);
            int gap = 0;
            for (int i = 0; i < mStarNum; i++) {
                drawStar(canvas, starPaint, i * mStarSize + gap, radius);
                gap += mStarGap;
            }
        }

        // 设置可视区域
        int size = Math.round(mRating);
        float decimal = 0;
        // 根据步长获取小数位
        if (size > mRating) {
            decimal = (mRating - size + 1);
            int rate = (int) (decimal / mStarStep);
            decimal = rate * mStarStep;
        }
        int right = (int) (((int)mRating) * (mStarSize + mStarGap) + decimal * mStarSize - radius);
        canvas.clipRect(-radius, -radius, right, mStarSize - radius);
        if (mStar != null) {
            drawStarDrawable(canvas, mStar, size);
        } else {
            starPaint.setColor(mStarColor);
            int gap = 0;
            for (int i = 0; i < size; i++) {
                drawStar(canvas, starPaint, i * mStarSize + gap, radius);
                gap += mStarGap;
            }
        }
        canvas.restore();
    }

    private void drawStarDrawable(Canvas canvas, Drawable starDrawable, int starNum) {
        Bitmap bitmap = ((BitmapDrawable)starDrawable).getBitmap();
        int gap = 0;
        int radius = mStarSize / 2;
        for (int i = 0; i < starNum; i++) {
            Rect desRect = new Rect(i * mStarSize - radius + gap , -radius, (i+1) * mStarSize - radius + gap, mStarSize - radius);
            canvas.drawBitmap(bitmap, null, desRect, starPaint);
            gap += mStarGap;
        }
    }

    private void drawStar(Canvas canvas, Paint paint, int startX, int radius) {
        Point[] points = new Point[5];
        for (int i = 0; i < 5; i++) {
            points[i] = new Point();
            points[i].x = startX + (int) (radius * Math.cos(Math.toRadians(72 * i - 18)));
            points[i].y = (int) (radius * Math.sin(Math.toRadians(72 * i - 18)));
        }
        Path path = new Path();
        path.moveTo(points[0].x, points[0].y);
        int i = 2;
        while (i != 5) {
            if (i >= 5) {
                i %= 5;
            }
            path.lineTo(points[i].x, points[i].y);
            i += 2;
        }
        path.close();
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsIndicator) {
            return super.onTouchEvent(event);
        }
        float focusX = event.getX();
        int num = mStarNum;
        while (num > 0){
            int x = (mStarSize + mStarGap) * (num - 1);
            if(focusX > x){
                setRating(num);
                break;
            }
            num--;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize;
        int heightSize;

        widthSize = getPaddingLeft() + getPaddingRight();
        if (mStarNum > 0) {
            widthSize += mStarNum * mStarSize + (mStarNum - 1) * mStarGap;
        }
        heightSize = getPaddingTop() + getPaddingBottom() + mStarSize;
        setMeasuredDimension(widthSize, heightSize);
    }

    public void setRating(float rating){
        if(this.mRating != rating){
            this.mRating = rating;
            invalidate();
        }
    }

    public int getNumStars() {
        return mStarNum;
    }

    public float getRating() {
        return mRating;
    }
    public int getIntRating() {
        return (int) mRating;
    }

    /*
     * <style name="ratingBar_common_small" parent="@android:style/Widget.RatingBar">
     *         <item name="android:progressDrawable">@drawable/ratingstars_small</item>
     *         <item name="android:layout_width">wrap_content</item>
     *         <item name="android:layout_height">wrap_content</item>
     *         <item name="android:minHeight">13dp</item>
     *         <item name="android:maxHeight">13dp</item>
     *         <item name="android:isIndicator">false</item>
     *         <item name="android:max">5</item>
     *         <item name="android:numStars">5</item>
     *         <item name="android:stepSize">1</item>
     *     </style>
     *
     *     <style name="ratingBar_common_middle" parent="ratingBar_common_small">
     *         <item name="android:progressDrawable">@drawable/ratingstars</item>
     *         <item name="android:minHeight">24dp</item>
     *         <item name="android:maxHeight">24dp</item>
     *     </style>
     */
}