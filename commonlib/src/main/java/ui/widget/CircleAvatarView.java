package ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import util.PictureUtils;


/**
 * Created by Bruce.Zhou on 2018/6/19 13:35.
 * Email: 907160968@qq.com
 */

public class CircleAvatarView extends SurfaceView implements Runnable {

    private Paint paint;
    private SurfaceHolder holder;
    private int basicColor;
    private boolean isDrawing;
    private float startAngle = 0;
    private float sweepAngle = 120;
    private int width;
    private int height;
    private RectF arcRect;
    private static final int arcWidth = 8;
    private Bitmap bitmap;
    private boolean drawArc = true;
    private boolean beLen = true;
    private static int runSpeed = 8;
    private int firstArcSweepAngle = 0; // 初始角度

    private int state;
    public static final int STATE_IDLE = 0;
    public static final int STATE_INIT = 1;
    public static final int STATE_RUN = 2;
    //        private static int initArcColor = Color.parseColor("#FFB90F3F");

    public void setState(int state) {
        this.state = state;
        switch (state) {
            case STATE_INIT:
                firstArcSweepAngle = 0;
                break;
            case STATE_IDLE:
                break;
            case STATE_RUN:
                startAngle = 0;
                sweepAngle = 120;
                break;
            default:
                break;
        }
    }

    public CircleAvatarView(Context context) {
        super(context);
        init();
    }

    public CircleAvatarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleAvatarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        basicColor = Color.parseColor("#f63854");
        //        basicColor = Color.parseColor("#FFB90F3F");
        paint.setStrokeWidth(arcWidth);
        paint.setTextSize(40);
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(basicColor);
        paint.setDither(true);
        setZOrderOnTop(true);
        holder = getHolder();
        holder.setFormat(PixelFormat.TRANSLUCENT);
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (isDrawing == false) {
                    isDrawing = true;
                    new Thread(CircleAvatarView.this).start();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                isDrawing = false;
            }
        });
    }

    public void setBitmap(Bitmap src) {
        bitmap = src;
        if (width > 0) {
            if (bitmap != null) {
                bitmap = Bitmap.createScaledBitmap(bitmap, width - 2 * arcWidth, height - 2 * arcWidth, true);
                bitmap = PictureUtils.createCircleImage(bitmap, width - 2 * arcWidth);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSize <= 0 || heightSize <= 0) {
            widthSize = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
            heightSize = widthSize;
        }
        int widthSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        setMeasuredDimension(widthSpec, heightSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        arcRect = new RectF(arcWidth, arcWidth, width - arcWidth, height - arcWidth);
        setBitmap(bitmap);
    }

    @Override
    public void run() {
        while (isDrawing) {
            draw();
            logic();
        }
    }

    private void logic() {
        switch (state) {
            case STATE_RUN:
                logicRun();
                break;
            case STATE_INIT:
                logicInit();
                break;
            default:
                break;
        }
    }

    private void logicRun() {
        startAngle += runSpeed;
        //        if (startAngle <= -360) {
        //            startAngle = 0;
        //        }
        //        if (beLen) {
        //            sweepAngle+=2;
        //            if (sweepAngle >= 160) {
        //                beLen = false;
        //            }
        //        } else {
        //            sweepAngle-=2;
        //            if (sweepAngle <= 40) {
        //                beLen = true;
        //            }
        //        }
    }

    private void logicInit() {
        if (firstArcSweepAngle < 360) {
            firstArcSweepAngle += 8;
        }
    }

    private void draw() {
        Canvas canvas = holder.lockCanvas();
        if (canvas == null) {
            return;
        }
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, arcWidth, arcWidth, paint);
        }
        switch (state) {
            case STATE_RUN:
                //                paint.setColor(Color.WHITE);
                //                canvas.drawCircle(redPacketWidth/2, redPacketHeight/2, (redPacketWidth - 2*arcWidth)/2.0f, paint);
                if (arcRect != null && drawArc) {
                    paint.setColor(basicColor);
                    canvas.drawArc(arcRect, startAngle, sweepAngle
                            , false, paint);
                }
                break;
            case STATE_INIT:
                paint.setColor(basicColor);
                canvas.drawArc(arcRect, 0, firstArcSweepAngle
                        , false, paint);
                break;
            case STATE_IDLE:
                break;
            default:
                break;
        }
        if (holder != null) {
            try {
                holder.unlockCanvasAndPost(canvas);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void startRun() {
        drawArc = true;
        startAngle = 0;
    }

    public void stopRun() {
        drawArc = false;
    }

    public void destory() {
        isDrawing = false;
    }
}
