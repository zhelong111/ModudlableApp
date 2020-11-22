package ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 可拖动的,并返回拖动坐标
 * Created by Bruce.Zhou on 2018/4/20.
 */

public class DragView extends View implements View.OnTouchListener, GestureDetector.OnGestureListener {
    private Paint paint;
    private GestureDetector gestureDetector;
    private float touchX;
    private float touchY;
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1;
    private DragablePhotoView  dragablePhotoView;

    public void setDragablePhotoView(DragablePhotoView dragablePhotoView) {
        this.dragablePhotoView = dragablePhotoView;
    }

    public interface OnMyTouchListener {
        void onDown(float downX, float downY);
        void onMove(float dx, float dy);
        void onUp();
        void onScale(float scaleFactor);
    }

    private OnMyTouchListener onMyTouchListener;

    public void setOnMyTouchListener(OnMyTouchListener listener) {
        onMyTouchListener = listener;
    }

    public DragView(Context context) {
        super(context);
        init();
    }

    public DragView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        paint.setColor(Color.DKGRAY);
        paint.setStyle(Paint.Style.STROKE);
        gestureDetector = new GestureDetector(getContext(), this);
        mScaleDetector = new ScaleGestureDetector(getContext(), new SimpleScaleListenerImpl());
        setOnTouchListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (width <= 0 || height <= 0) {
            width = getContext().getResources().getDisplayMetrics().widthPixels;
            height = getContext().getResources().getDisplayMetrics().heightPixels;
        }
        int widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        setMeasuredDimension(widthSpec, heightSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        touchX = e.getX();
        touchY = e.getY();
        if (onMyTouchListener != null) {
            onMyTouchListener.onDown(touchX, touchY);
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //        LogUtil.d("photo", "dx=" + distanceX + ", dy=" + distanceY);
//        touchX -= distanceX;
//        touchY -= distanceY;
//        // 范围
//        if (touchX < -getWidth() / 2) {
//            touchX = -getWidth() / 2;
//        }
//        if (touchX > getWidth() / 2) {
//            touchX = getWidth() / 2;
//        }
//        if (touchY < -getHeight() / 2) {
//            touchY = -getHeight() / 2;
//        }
//        if (touchY > getHeight() / 2) {
//            touchY = getHeight() / 2;
//        }
//        invalidate();

        if (onMyTouchListener != null) {
            onMyTouchListener.onMove(distanceX, distanceY);
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (onMyTouchListener != null) {
                    onMyTouchListener.onUp();
                }
                break;
            default:
                break;
        }
        mScaleDetector.onTouchEvent(event);
        return gestureDetector.onTouchEvent(event);
    }

    public Bitmap getViewBitmap() {
        setDrawingCacheEnabled(true);
        Bitmap bitmap = getDrawingCache();
        Bitmap retBitmap = Bitmap.createBitmap(bitmap);
        setDrawingCacheEnabled(false);
        return retBitmap;
    }

    //缩放
    private class SimpleScaleListenerImpl extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            //缩放倍数范围
            float smallScale = 0.21f * dragablePhotoView.getOriginalUserPhotoWidth() / dragablePhotoView.getUserPhotoWidth();
            float bigScale = 1.22f * dragablePhotoView.getOriginalUserPhotoWidth() / dragablePhotoView.getUserPhotoWidth();
//            mScaleFactor = Math.max(0.21f, Math.min(mScaleFactor, 1.22f));
            mScaleFactor = Math.max(smallScale, Math.min(mScaleFactor, bigScale));
            onMyTouchListener.onScale(mScaleFactor);
            return true;
        }
    }


}
