package ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by Bruce.Zhou on 2018/1/5.
 */

public class ScanView extends SurfaceView implements Runnable {
    private Paint paint;
    private SurfaceHolder holder;
    private boolean isDrawing;
    private float currY;
    private boolean isUpScan;
    private Bitmap scannerUp;
    private Bitmap scannerDown;
    private boolean canDraw;
    private int width;
    private int height;

    public ScanView(Context context) {
        super(context);
        init();
    }

    public ScanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScanView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int redPacketWidth = (int) (MeasureSpec.getSize(heightMeasureSpec) * 3.0f/4);
//        int widthSpec = MeasureSpec.makeMeasureSpec(redPacketWidth, MeasureSpec.EXACTLY);
//        setMeasuredDimension(widthSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
        initBitmap();
    }

    private void init() {
        paint = new Paint();
        paint.setStrokeWidth(4);
        paint.setTextSize(40);
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);
        paint.setDither(true);
        setZOrderOnTop(true);
        holder = getHolder();
        holder.setFormat(PixelFormat.TRANSLUCENT);
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (isDrawing == false) {
                    isDrawing = true;
                    new Thread(ScanView.this).start();
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

    private void initBitmap() {
//        scannerUp = BitmapUtils.loadBitmapFromRaw(getContext(), R.drawable.big_scanner_up);
//        scannerUp = Bitmap.createScaledBitmap(scannerUp, width, scannerUp.getHeight(), false);
//        scannerDown = BitmapUtils.loadBitmapFromRaw(getContext(), R.drawable.big_scanner_down);
//        scannerDown = Bitmap.createScaledBitmap(scannerDown, width, scannerDown.getHeight(), false);
    }

    @Override
    public void run() {
        while (isDrawing) {
            if (canDraw) {
                draw();
            }
        }
    }

    private void draw() {
        Canvas canvas = holder.lockCanvas();
        if(canvas == null) {
            return;
        }
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        if (scannerDown != null && scannerUp != null) {
            if (!isUpScan) {
                canvas.drawBitmap(scannerDown, 0, currY, paint);
                currY+=15;
                if (currY >= height) {
                    isUpScan = true;
                }
            } else {
                canvas.drawBitmap(scannerUp, 0, currY, paint);
                currY-=15;
                if (currY <= 0) {
                    isUpScan = false;
                }
            }
        }
        if (holder != null) {
            try {
                holder.unlockCanvasAndPost(canvas);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        canDraw = true;
        isUpScan = false;
        currY = 0;
        setVisibility(View.VISIBLE);
    }

    public void stop() {
        isDrawing = false;
        canDraw = false;
        setVisibility(View.INVISIBLE);
    }

    public void destroy() {
        canDraw = false;
        isDrawing = false;
        if (scannerUp != null && !scannerUp.isRecycled()) {
            scannerUp.recycle();
        }
        if (scannerDown != null && !scannerDown.isRecycled()) {
            scannerDown.recycle();
        }
    }
}
