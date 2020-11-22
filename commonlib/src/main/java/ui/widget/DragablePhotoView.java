package ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 可拖动的背景和用户照片合成
 * Created by Bruce.Zhou on 2018/4/20.
 */

public class DragablePhotoView extends View
//        implements View.OnTouchListener, GestureDetector.OnGestureListener
{
    private Paint paint;
//    private GestureDetector gestureDetector;
    private float userPhotoX;
    private float userPhotoY;
    private Bitmap photoFrameBitmap, userPhotoBitmap;
    private Bitmap currDragBitmap; // 当前正在拖动的图片
    private float photoScaleFactor = 0.8f; // 人物照片相对draggablePhotoView的缩放比例
    private int userPhotoWidth;
    private int userPhotoHeight;
    private Bitmap originalUserBitmap; // 保存最原始的userBitmap

//    public interface OnMyTouchListener {
//        void onDown();
//    }
//
//    private OnMyTouchListener onMyTouchListener;
//
//    public void setOnMyTouchListener(OnMyTouchListener listener) {
//        onMyTouchListener = listener;
//    }

    public DragablePhotoView(Context context) {
        super(context);
        init();
    }

    public DragablePhotoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragablePhotoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        paint.setColor(Color.DKGRAY);
        paint.setStyle(Paint.Style.STROKE);
//        gestureDetector = new GestureDetector(getContext(), this);
//        setOnTouchListener(this);
    }

    /**
     *
     * @param drawableResId
     * @param userBitmap
     */
    public void setPhotoFrame(int drawableResId, Bitmap userBitmap) {
        if (photoFrameBitmap != null && !photoFrameBitmap.isRecycled()) {
            photoFrameBitmap.recycle();
        }
        photoFrameBitmap = BitmapFactory.decodeResource(getResources(), drawableResId);
        userPhotoBitmap = userBitmap;
        originalUserBitmap = userBitmap;
        if (getWidth() != 0 && getHeight() != 0 && userPhotoBitmap != null) {
            photoFrameBitmap = Bitmap.createScaledBitmap(photoFrameBitmap, getWidth(), getHeight(), true);
            userPhotoWidth = (int) (userPhotoBitmap.getWidth() * photoScaleFactor);
            userPhotoHeight = (int) (userPhotoBitmap.getHeight() * photoScaleFactor);
            userPhotoBitmap = Bitmap.createScaledBitmap(userPhotoBitmap, userPhotoWidth
                    , userPhotoHeight, true);
        }
        invalidate();
    }

    private boolean isOriginalShow = false;

    public void resetUserPhoto(Bitmap userPhotoBitmap) {
//        this.userPhotoBitmap = Bitmap.createScaledBitmap(userPhotoBitmap, getWidth()
//                , getHeight(), true);
        this.userPhotoBitmap = userPhotoBitmap;
//        this.userPhotoY = this.userPhotoX = 0;
        isOriginalShow = true;
        invalidate();
    }

    public void setOriginalShow(boolean originalShowing) {
        this.isOriginalShow = originalShowing;
    }

    public int getUserPhotoWidth() {
        return userPhotoWidth;
    }

    public int getUserPhotoHeight() {
        return userPhotoHeight;
    }

    public int getOriginalUserPhotoWidth() {
        if (originalUserBitmap != null) {
            return originalUserBitmap.getWidth();
        }
        return 1;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (width <= 0 || height <= 0) {
            //            redPacketWidth = (int) TypedValue.applyDimension(
            //                    TypedValue.COMPLEX_UNIT_DIP, 600, getResources().getDisplayMetrics());
            //            heightSize = widthSize;
            width = getContext().getResources().getDisplayMetrics().widthPixels;
            height = getContext().getResources().getDisplayMetrics().heightPixels;
        }
        int widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        setMeasuredDimension(widthSpec, heightSpec);
    }

    private boolean isFirstInit = true;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (isFirstInit) {
            isFirstInit = false;
            if (photoFrameBitmap != null) {
                photoFrameBitmap = Bitmap.createScaledBitmap(photoFrameBitmap, getWidth(), getHeight(), true);
            }
            if (userPhotoBitmap != null) {
                userPhotoWidth = (int) (userPhotoBitmap.getWidth() * photoScaleFactor);
                userPhotoHeight = (int) (userPhotoBitmap.getHeight() * photoScaleFactor);
                userPhotoBitmap = Bitmap.createScaledBitmap(userPhotoBitmap, userPhotoWidth
                        , userPhotoHeight, true);
//                userPhotoBitmap = Bitmap.createScaledBitmap(userPhotoBitmap, (int) (userPhotoBitmap.getWidth() * photoScaleFactor)
//                        , (int) (userPhotoBitmap.getHeight() * photoScaleFactor), true);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //        super.onDraw(canvas);
        if (!isOriginalShow) {
            if (photoFrameBitmap != null) {
                drawBitmap(canvas, photoFrameBitmap, 0, 0);
            }
        }
        if (userPhotoBitmap != null) {
            if (isOriginalShow) {
                drawBitmap(canvas, userPhotoBitmap, (getWidth() - userPhotoBitmap.getWidth())/2,
                        (getHeight() - userPhotoBitmap.getHeight())/2);
            } else {
                drawBitmap(canvas, userPhotoBitmap, userPhotoX, userPhotoY);
            }
        }
        // 绘制边框
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    }

    private void drawBitmap(Canvas canvas, Bitmap bitmap, float x, float y) {
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, x, y, paint);
        }
    }

//    @Override
//    public boolean onDown(MotionEvent e) {
//        if (onMyTouchListener != null) {
//            onMyTouchListener.onDown();
//        }
//        return true;
//    }
//
//    @Override
//    public void onShowPress(MotionEvent e) {
//
//    }
//
//    @Override
//    public boolean onSingleTapUp(MotionEvent e) {
//        return false;
//    }


//    private boolean isResultPage;
//
//    public void setIsResultPage(boolean isResultPage) {
//        this.isResultPage = isResultPage;
//    }
//
//    @Override
//    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//        if (isResultPage) {
//            return true;
//        }
//        //        LogUtil.d("photo", "dx=" + distanceX + ", dy=" + distanceY);
//        userPhotoX -= distanceX;
//        userPhotoY -= distanceY;
//        // 范围
//        if (userPhotoX < -getWidth() / 2) {
//            userPhotoX = -getWidth() / 2;
//        }
//        if (userPhotoX > getWidth() / 2) {
//            userPhotoX = getWidth() / 2;
//        }
//        if (userPhotoY < -getHeight() / 2) {
//            userPhotoY = -getHeight() / 2;
//        }
//        if (userPhotoY > getHeight() / 2) {
//            userPhotoY = getHeight() / 2;
//        }
//        invalidate();
//        return false;
//    }
//
//    @Override
//    public void onLongPress(MotionEvent e) {
//
//    }
//
//    @Override
//    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//        return false;
//    }
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        return gestureDetector.onTouchEvent(event);
//    }

    public Bitmap getViewBitmap() {
        setDrawingCacheEnabled(true);
        Bitmap bitmap = getDrawingCache();
        Bitmap retBitmap = Bitmap.createBitmap(bitmap);
        setDrawingCacheEnabled(false);
//        bitmap.recycle();
        return retBitmap;
    }

    /**
     * 接收外部传入touch坐标的方法
     * @param downX
     * @param downY
     */
    public void onTouchDown(float downX, float downY) {
        if (userPhotoBitmap != null) {
            if (downX > userPhotoX && downX < userPhotoX + userPhotoBitmap.getWidth()
                    && downY > userPhotoY && downY < userPhotoY + userPhotoBitmap.getHeight()) {
                // 当前拖动的是userPhotoBitmap
                currDragBitmap = userPhotoBitmap;
            }
        }
    }

    /**
     * 接收外部传入移动距离的方法
     * @param dx
     * @param dy
     */
    public void onTouchMove(float dx, float dy) {
        if (currDragBitmap != null) {
            if (currDragBitmap == userPhotoBitmap) {
                userPhotoX -= dx;
                userPhotoY -= dy;
                if (userPhotoX < -userPhotoBitmap.getWidth() * 0.6f) {
                    userPhotoX = -userPhotoBitmap.getWidth() * 0.6f;
                }
                if (userPhotoY < -userPhotoBitmap.getHeight() * 0.6f) {
                    userPhotoY = -userPhotoBitmap.getHeight() * 0.6f;
                }
                if (userPhotoX  >= getMeasuredWidth() - userPhotoBitmap.getWidth() * 0.7f) {
                    userPhotoX = getMeasuredWidth() - userPhotoBitmap.getWidth() * 0.7f;
                }
                if (userPhotoY  >= getMeasuredHeight() - userPhotoBitmap.getHeight() * 0.7f) {
                    userPhotoY = getMeasuredHeight() - userPhotoBitmap.getHeight() * 0.7f;
                }
                invalidate();
            }
        }
    }

    public void onScale(float scaleFactor) {
        if (userPhotoBitmap != null) {
//            Matrix matrix = new Matrix();
//            matrix.postScale(scaleFactor ,scaleFactor);

            int dstWidth = (int) (userPhotoWidth * scaleFactor);
            int dstHeight = (int) (userPhotoHeight * scaleFactor);
            if (userPhotoBitmap != null & !userPhotoBitmap.isRecycled()) {
                userPhotoBitmap.recycle();
                userPhotoBitmap = null;
            }
            userPhotoBitmap = Bitmap.createScaledBitmap(originalUserBitmap, dstWidth, dstHeight, true);

            photoScaleFactor = dstWidth * 1f / originalUserBitmap.getWidth();

            invalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (photoFrameBitmap != null && !photoFrameBitmap.isRecycled()) {
            photoFrameBitmap.recycle();
        }
        if (userPhotoBitmap != null && !userPhotoBitmap.isRecycled()) {
            userPhotoBitmap.recycle();
        }
        if (originalUserBitmap != null && !originalUserBitmap.isRecycled()) {
            originalUserBitmap.recycle();
        }
    }
}
