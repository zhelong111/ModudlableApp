package ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import com.bruce.modulableapp.commonlib.R;


import java.util.List;

import util.DensityUtil;
import util.LogUtil;
import util.MultiResolutionUtil;
import util.UIUtils;

/**
 * Created by Bruce.Zhou on 2018/1/5.
 */

public class RadarView6 extends SurfaceView implements Runnable, View.OnTouchListener {
    private Paint paint;
    private SurfaceHolder holder;
    private boolean isDrawing;
    private boolean canDraw = true;
    private int maxRadius;
    private int bgColor;
    private int lineColor;
    private int textSize;
    private int textColor;
    private PointF[][] points = new PointF[5][6];
    private PointF center;

    private static int frameLineMoveSpeed = 4;
    private float topMoveX;
    private float topMoveY;
    private float topRightMoveX;
    private float topRightMoveY;
    private float rightBottomMoveX;
    private float rightBottomMoveY;
    private float leftBottomMoveX;
    private float leftBottomMoveY;
    private float topLeftMoveX;
    private float topLeftMoveY;
    private float bottomMoveX;
    private float bottomMoveY;

    private int alpha = 21;

    private static float angelRate;

    //    private static final String[] titles = new String[] {
    //            "甜美", "时尚", "帅气", "典雅", "摩登", "文雅", "浪漫", "自然"
    //    };
    private String[] titles = new String[]{
            "甜美名媛", "干练女强人", "率真女孩", "端庄贵妇", "时尚达人", "Other"
    };

    private float[] percentValues; // length must 8
    private float[] percentValues2; // length must 8
    private PointF[] percentPoints = new PointF[6];
//    private PointF[] percentPoints2 = new PointF[5];
    private PointF indicatorPointTop;
    private PointF indicatorPointTopRight;
    private PointF indicatorPointRight;
    private PointF indicatorPointBottomRight;
    private PointF indicatorPointBottom;
    private PointF indicatorPointBottomLeft;
    private PointF indicatorPointLeft;
    private PointF indicatorPointTopLeft;
    private Path indicatorPath;
    private Path indicatorPath2;
    private Path anglePath;
    private static int INDICATOR_SPEED = 6;
    private static int INDICATOR_SPEED2 = 4;
    private static final int SIDE_MOVE_SPEED = 6;

    private boolean indicatorTopFinish;
    private boolean indicatorTopRightFinish;
    private boolean indicatorRightFinish;
    private boolean indicatorBottomRightFinish;
    private boolean indicatorBottomFinish;
    private boolean indicatorBottomLeftFinish;
    private boolean indicatorLeftFinish;
    private boolean indicatorTopLeftFinish;
    private int externalStyleColor;
    private int innerStyleColor;
    private float titleShaderX1;
    private float titleShaderX2;
    private RectF[] titleRects; // 标题位置区域
    private int styleIndex = 1; // 1~5从上倒下，顺时针方向
    LinearGradient mShader; // 文字shader
    private String currTitle;
    private float titleScale = 1;
    private int titleAlpha = 200;
    private boolean canSelectTitle = false;
    private Handler mHandler;
    private long timeOnClickedMillis;
    private float cos18 = (float) Math.cos(Math.toRadians(18));
    private float sin18 = (float) Math.sin(Math.toRadians(18));
    private float cos54 = (float) Math.cos(Math.toRadians(54));
    private float sin54 = (float) Math.sin(Math.toRadians(54));
    private float cos30 = (float) Math.cos(Math.toRadians(30));
    private float sin30 = (float) Math.sin(Math.toRadians(30));

    // newpart
    private int elegentStyleIndex = 0; // 0 ~ 4 // 雅风格
    private float elegentStyleValue = 0.86f; // 雅风格程度值
//    private PointF elegentEndPoint; // 绘制途中雅风格指向性顶点
//    private PointF elegentSecondPoint; // 绘制途中雅风格三角形除了圆点和指向性顶点之外的那个点
    private PointF targetElegentEndPoint; // 目标点：雅风格指向性顶点
    private PointF targetElegentSecondPoint; // 目标点：绘制途中雅风格三角形除了圆点和指向性顶点
    private static final int TEXT_RED = Color.parseColor("#b30030");
    private static final int TEXT_BLUE = Color.parseColor("#00baff");

    public interface OnTitleSelectListener {
        void onTitleSelected(int styleIndex);
    }

    private OnTitleSelectListener onTitleSelectListener;

    public void setOnTitleSelectListener(OnTitleSelectListener onTitleSelectListener) {
        this.onTitleSelectListener = onTitleSelectListener;
    }

    public void setCanSelectTitle(boolean canSelectTitle) {
        this.canSelectTitle = canSelectTitle;
    }

    public void setPercentValues(float[] percentValues, float[] percentValues2) {
        this.percentValues = percentValues;
        this.percentValues2 = percentValues2;
        setCurrStyleIndex(percentValues);
        initData();
    }

    // newpart
    public void setPercentValues(float[] percentValues, int elegentStyleIndex) {
        this.elegentStyleIndex = elegentStyleIndex;
        this.percentValues = percentValues;
        setCurrStyleIndex(percentValues);
        initData();
    }

    public RadarView6(Context context) {
        super(context);
        init(context, null);
    }

    public RadarView6(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RadarView6(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

//    public void setRadarTitle(List<RadarTitleBean.ListBean> radarTitle) {
//        if (radarTitle != null && radarTitle.size() > 0) {
//            //            mRadarArray = new String[radarTitle.size()];
//            for (int i = 0; i < radarTitle.size(); i++) {
//                if (i < titles.length) {
//                    titles[i] = radarTitle.get(i).getStyleName();
//                }
//            }
//        }
//    }

    private void setCurrStyleIndex(float[] percentValues) {
        if (percentValues != null) {
            float maxValue = percentValues[0];
            for (int i = 0; i < percentValues.length; i++) {
                if (i >= titles.length) {
                    break;
                }
                if (maxValue < percentValues[i]) {
                    maxValue = percentValues[i];
                    styleIndex = i + 1;
                }
            }
            currTitle = titles[styleIndex - 1];
        }
    }

    /**
     * 设置选中哪个title
     *
     * @param styleIndex 1~5 从上向下，顺时针
     */
    public void setSelectedTitle(int styleIndex) {
        if (styleIndex <= 0 || styleIndex > 5) {
            return;
        }
        this.styleIndex = styleIndex;
        currTitle = titles[styleIndex - 1];
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSize <= 0 || heightSize <= 0) {
            widthSize = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 600, getResources().getDisplayMetrics());
            //            widthSize = ((Activity)getContext()).getResources().getDisplayMetrics().widthPixels;
            heightSize = widthSize;
            //            heightSize = ((Activity)getContext()).getResources().getDisplayMetrics().heightPixels;
        }
        int widthSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        setMeasuredDimension(widthSpec, heightSpec);
    }

    private boolean isInited;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!isInited) {
            isInited = true;
            //            setPercentValues(new float[]{0, 0, 0, 0, 0, 0, 0, 0}, null);
            setPercentValues(this.percentValues, this.percentValues2);
            //            mShader = new LinearGradient(titleShaderX1, 0, titleShaderX2, getHeight(),
            //                    new int[] {titleGradientStartColor, Color.RED} , null, Shader.TileMode.REPEAT); // 一个材质,打造出一个线性梯度沿著一条线。
        }
    }

    private void initData() {
        alpha = 21;
        angelRate = (float) (Math.sqrt(2) / 2);
        center = new PointF(getMeasuredWidth() / 2, getMeasuredHeight() / 2); // 圆心
        titleShaderX1 = getMeasuredWidth() / 2;
        titleShaderX2 = getMeasuredWidth() / 3.6f;
        // 从正上方开始顺时针
        points[0][0] = new PointF(center.x, center.y - maxRadius); // top
        points[0][1] = new PointF((center.x + maxRadius * cos30), (center.y - maxRadius * sin30));
        points[0][2] = new PointF((center.x + maxRadius * cos30), (center.y + maxRadius * sin30));
        points[0][3] = new PointF(center.x, center.y + maxRadius);
        points[0][4] = new PointF((center.x - maxRadius * cos30), (center.y + maxRadius * sin30));
        points[0][5] = new PointF((center.x - maxRadius * cos30), (center.y - maxRadius * sin30));

        // 由外向内
        float deltaXY = maxRadius / 5.0f;
        for (int row = 1; row < 5; row++) {
            points[row][0] = new PointF(points[row - 1][0].x, points[row - 1][0].y + deltaXY); // top
            points[row][1] = new PointF((points[row - 1][1].x - deltaXY * cos30)
                    , (points[row - 1][1].y + deltaXY * sin30)); // top right
            points[row][2] = new PointF((points[row - 1][2].x - deltaXY * cos30)
                    , (points[row - 1][2].y - deltaXY * cos30)); // right
            points[row][3] = new PointF(points[row - 1][3].x
                    , points[row - 1][3].y - deltaXY);
            points[row][4] = new PointF((points[row - 1][4].x + deltaXY * cos30)
                    , (points[row - 1][4].y - deltaXY * cos30));
            points[row][5] = new PointF((points[row - 1][5].x + deltaXY * cos30)
                    , (points[row - 1][5].y + deltaXY * cos30));
        }
        // line move xy
        topMoveX = points[0][0].x;
        topMoveY = points[0][0].y;
        topRightMoveX = points[0][1].x;
        topRightMoveY = points[0][1].y;
        rightBottomMoveX = points[0][2].x;
        rightBottomMoveY = points[0][2].y;
        bottomMoveX = points[0][3].x;
        bottomMoveY = points[0][3].y;
        leftBottomMoveX = points[0][4].x;
        leftBottomMoveY = points[0][4].y;
        topLeftMoveX = points[0][5].x;
        topLeftMoveY = points[0][5].y;

        // percent points
        if (percentValues != null) {
            for (int i = 0; i < 5; i++) {
                float percent = percentValues[i];
                float targetX = 0;
                float targetY = 0;
                switch (i) {
                    case 0: // top
                        targetX = center.x;
                        targetY = center.y - maxRadius * percent;
                        break;
                    case 1: // top right
                        targetX = (center.x + maxRadius * percent * cos18);
                        targetY = (center.y - maxRadius * percent * sin18);
                        break;
                    case 2: // right bottom
                        targetX = (center.x + maxRadius * percent * cos54);
                        targetY = (center.y + maxRadius * percent * sin54);
                        break;
                    case 3: // left bottom
                        targetX = (center.x - maxRadius * percent * cos54);
                        targetY = (center.y + maxRadius * percent * sin54);
                        break;
                    case 4: // top left
                        targetX = (center.x - maxRadius * percent * cos18);
                        targetY = (center.y - maxRadius * percent * sin18);
                        break;
                    default:
                }
                percentPoints[i] = new PointF(targetX, targetY);
            }
        }

        // newpart ---------------------------------------------------------------------------------------------------------------
        initAngleShapePoints(elegentStyleIndex);
        // ---------------------------------------------------------------------------------------------------------------

        indicatorPointTop = new PointF(center.x, center.y);
        indicatorPointTopRight = new PointF(center.x, center.y);
        indicatorPointBottomRight = new PointF(center.x, center.y);
        indicatorPointBottomLeft = new PointF(center.x, center.y);
        indicatorPointTopLeft = new PointF(center.x, center.y);

        indicatorTopFinish = false;
        indicatorTopRightFinish = false;
        indicatorBottomRightFinish = false;
        indicatorBottomLeftFinish = false;
        indicatorTopLeftFinish = false;
    } // initData

    private void init(Context context, AttributeSet attrs) {
        //        float speedRate = 480 / context.getResources().getDisplayMetrics().densityDpi;
        //        frameLineMoveSpeed *= speedRate;
        //        INDICATOR_SPEED *= speedRate;
        setClickable(true);
        setEnabled(true);
//        externalStyleColor = Color.argb(150, 255, 0, 0);    //外在
        externalStyleColor = Color.parseColor("#50ff0099");
        innerStyleColor = Color.argb(179, 213, 203, 182);   //内心
        initAttrs(context, attrs);
        indicatorPath = new Path();
//        indicatorPath2 = new Path();
        anglePath = new Path();
        paint = new Paint();
        paint.setStrokeWidth(10);
        paint.setTextSize(textSize);
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);
        paint.setDither(true);
        setZOrderOnTop(false);
        holder = getHolder();
        holder.setFormat(PixelFormat.TRANSLUCENT);
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (isDrawing == false) {
                    isDrawing = true;
                    new Thread(RadarView6.this).start();
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
        setOnTouchListener(this);
        //        mHandler = new Handler() {
        //            @Override
        //            public void handleMessage(Message msg) {
        //                if (System.currentTimeMillis() - timeOnClickedMillis >= 7999) {
        //                    timeOnClickedMillis = System.currentTimeMillis();
        //                    initData();
        //                    if (onRadarClickListener != null) {
        //                        onRadarClickListener.onRadarClicked();
        //                    }
        //                }
        //                sendMessageDelayed(Message.obtain(), 1000);
        //            }
        //        };
        //        mHandler.sendMessageDelayed(Message.obtain(), 8000);
    } // init()

    // newpart
    private void initAngleShapePoints(int elegentStyleIndex) {
        float targetX = 0;
        float targetY = 0;
        float secondX = 0;
        float secondY = 0;
        switch (elegentStyleIndex) {
            case 0: // top
                targetX = center.x;
                targetY = center.y - maxRadius * elegentStyleValue;
                secondX = center.x + maxRadius / 5;
                secondY = center.y - maxRadius * elegentStyleValue * 0.4f;
                break;
            case 1: // top right
                targetX = center.x + maxRadius * elegentStyleValue * cos18;
                targetY = center.y - maxRadius * elegentStyleValue * sin18;
                secondX = center.x + maxRadius / 3.1f;
                secondY = center.y + maxRadius * elegentStyleValue * 0.13f;
                break;
            case 2: // rb
                targetX = center.x + maxRadius * elegentStyleValue * cos54;
                targetY = center.y + maxRadius * elegentStyleValue * sin54;
                secondX = center.x + 5;
                secondY = center.y + maxRadius * elegentStyleValue * 0.44f;
                break;
            case 3: // bl
                targetX = center.x - maxRadius * elegentStyleValue * cos54;
                targetY = center.y + maxRadius * elegentStyleValue * sin54;
                secondX = center.x - maxRadius * 0.36f;
                secondY = center.y + maxRadius * elegentStyleValue * 0.1f;
                break;
            case 4: // lt
                targetX = center.x - maxRadius * elegentStyleValue * cos18;
                targetY = center.y - maxRadius * elegentStyleValue * sin18;
                secondX = center.x - maxRadius / 7.2f;
                secondY = center.y - maxRadius * elegentStyleValue * 0.4f;
                break;
            default:
        }
        targetElegentEndPoint = new PointF(targetX, targetY);
        targetElegentSecondPoint = new PointF(secondX, secondY);
    } // initAngleShapePoints


    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RadarView, 0, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.RadarView_bgColor) {
                bgColor = a.getColor(attr, Color.RED);
            } else if (attr == R.styleable.RadarView_lineColor) {
                lineColor = a.getColor(attr, Color.WHITE);
            } else if (attr == R.styleable.RadarView_maxRadius) {// default is 80 dp
                maxRadius = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics()));
                maxRadius = MultiResolutionUtil.getScaledSize(getContext(), maxRadius);
            } else if (attr == R.styleable.RadarView_textSize) {
                textSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
                textSize = (int) MultiResolutionUtil.getFontScaledSize(getContext(), DensityUtil.sp2px(getContext(), textSize));
            } else if (attr == R.styleable.RadarView_textColor) {
                textColor = a.getColor(attr, Color.parseColor("#cccccc"));
            }
        }
        a.recycle();
    }

    @Override
    public void run() {
        while (isDrawing) {
            if (canDraw) {
                draw();
                if (indicatorTopFinish && indicatorTopRightFinish && indicatorBottomRightFinish
                        && indicatorBottomLeftFinish && indicatorTopLeftFinish) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void resetTitleScale() {
        titleScale = 1;
        titleAlpha = 200;
        currTitle = null;
    }

    private void draw() {
        Canvas canvas = holder.lockCanvas();
        if (canvas == null || points[0][0] == null) {
            return;
        }
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        paint.setColor(bgColor);
        //        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        paint.setStyle(Paint.Style.STROKE);
        // frame lines --------------------------------------
        paint.setColor(lineColor);
        paint.setStrokeWidth(2);
        if (alpha < 255) {
            alpha += 3;
        } else {
            alpha = 255;
        }
        paint.setAlpha(alpha);
//        // top
//        canvas.drawLine(points[0][0].x, points[0][0].y, topMoveX, topMoveY, paint);
//        if (topMoveY - center.y < 0) {
//            topMoveY += frameLineMoveSpeed;
//        } else {
//            topMoveY = center.y;
//            indicatorTopFinish = true;
//        }
//        // top right
//        canvas.drawLine(points[0][1].x, points[0][1].y, topRightMoveX, topRightMoveY, paint);
//        if (topRightMoveY - center.y < 0) {
//            topRightMoveX -= frameLineMoveSpeed * cos18;
//            topRightMoveY += frameLineMoveSpeed * sin18;
//        } else {
//            topRightMoveX = center.x;
//            topRightMoveY = center.y;
//            indicatorTopRightFinish = true;
//        }
//        // bottom right
//        canvas.drawLine(points[0][2].x, points[0][2].y, rightBottomMoveX, rightBottomMoveY, paint);
//        if (rightBottomMoveX - center.x > 0) {
//            rightBottomMoveX -= frameLineMoveSpeed * cos54;
//            rightBottomMoveY -= frameLineMoveSpeed * sin54;
//        } else {
//            rightBottomMoveX = center.x;
//            rightBottomMoveY = center.y;
//            indicatorBottomRightFinish = true;
//        }
//        // left bottom
//        canvas.drawLine(points[0][3].x, points[0][3].y, leftBottomMoveX, leftBottomMoveY, paint);
//        if (leftBottomMoveX - center.x < 0) {
//            leftBottomMoveX += frameLineMoveSpeed * cos54;
//            leftBottomMoveY -= frameLineMoveSpeed * sin54;
//        } else {
//            leftBottomMoveX = center.x;
//            leftBottomMoveY = center.y;
//            indicatorBottomLeftFinish = true;
//        }
//        // top left
//        canvas.drawLine(points[0][4].x, points[0][4].y, topLeftMoveX, topLeftMoveY, paint);
//        if (topLeftMoveX - center.x < 0) {
//            topLeftMoveX += frameLineMoveSpeed * cos18;
//            topLeftMoveY += frameLineMoveSpeed * sin18;
//        } else {
//            topLeftMoveX = center.x;
//            topLeftMoveY = center.y;
//            indicatorTopLeftFinish = true;
//        }
//
//        // ------------- side lines ----------------
//        for (int row = 0; row < 4; row++) {
//            for (int col = 0; col < 5; col++) {
//                if (col < 4) {
//                    canvas.drawLine(points[row][col].x, points[row][col].y, points[row][col + 1].x, points[row][col + 1].y, paint);
//                } else {
//                    canvas.drawLine(points[row][col].x, points[row][col].y, points[row][0].x, points[row][0].y, paint);
//                }
//            }
//        }

        // newpart --------------------------------------------------------------------------------------------------------
        // 绘制第一个圆(从内到外)
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(3);
        // 圆边
        canvas.drawCircle(center.x, center.y, center.y - points[4][0].y + maxRadius/5 * 0.8f, paint);

        // 绘制第二个圆
        paint.setColor(Color.DKGRAY);
        canvas.drawCircle(center.x, center.y, center.y - points[2][0].y + maxRadius/5 * 1.1f, paint);
        // 实心圆
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#8f444444"));
        canvas.drawCircle(center.x, center.y, center.y - points[2][0].y + maxRadius/5 * 1.1f - 2, paint);

        // 绘制第三个圆
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.DKGRAY);
        paint.setStrokeWidth(2.3f);
//        canvas.drawCircle(center.x, center.y, center.y - points[2][0].y + 2.5f * maxRadius/10, paint);
        canvas.drawCircle(center.x, center.y, center.y - points[0][0].y, paint);

//        // 绘制第四个圆
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setColor(Color.parseColor("#6f444444"));
//        paint.setStrokeWidth(2f);
//        canvas.drawCircle(center.x, center.y, center.y - points[0][0].y + maxRadius/14, paint);

        // draw angle
        if (targetElegentEndPoint != null && targetElegentSecondPoint != null) {
            anglePath.reset();
            paint.setColor(Color.parseColor("#8f00b9fe"));
            paint.setStyle(Paint.Style.FILL);
            anglePath.moveTo(center.x, center.y);
            anglePath.lineTo(targetElegentEndPoint.x, targetElegentEndPoint.y);
            anglePath.lineTo(targetElegentSecondPoint.x, targetElegentSecondPoint.y);
            anglePath.close();
            canvas.drawPath(anglePath, paint);
//            if (indicatorBottomLeftFinish) {
//            }
            paint.setColor(Color.parseColor("#00b9fe"));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(3);
            canvas.drawPath(anglePath, paint);
            // circle
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(targetElegentEndPoint.x, targetElegentEndPoint.y, maxRadius/28, paint);
        }
        // --------------------------------------------------------------------------------------------------------

        // title
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(1);
        for (int i = 0; i < 5; i++) {
            float x = points[0][i].x;
            float y = points[0][i].y;
            switch (i) {
                case 0:
                    if (titles[0].length() == 2 || titles[0].length() == 1) {
                        x -= paint.getTextSize() * 1.0f;
                    } else if (titles[0].length() == 3) {
                        x -= paint.getTextSize() * 1.5f;
                    } else if (titles[0].length() == 4) {
                        x -= paint.getTextSize() * 2.0f;
                    } else if (titles[0].length() == 5) {
                        x -= paint.getTextSize() * 2.5f;
                    }
                    y -= paint.getTextSize() * 0.8f;
                    break;
                case 1:
                    if (titles[1].length() == 2 || titles[1].length() == 1) {
                        x += paint.getTextSize() / 2.5;
                    } else if (titles[1].length() == 3) {
                        x += paint.getTextSize() / 2;
                    } else if (titles[1].length() == 4) {
                        x += paint.getTextSize() / 2;
                    } else if (titles[1].length() == 5) {
                        x += paint.getTextSize() / 2;
                    }
                    //                    y -= paint.getTextSize() / 2;
                    y += paint.getTextSize() / 3.5;
                    break;
                case 2:
                    if (titles[2].length() == 2 || titles[2].length() == 1) {
                        x += paint.getTextSize() / 2;
                    } else if (titles[2].length() == 3) {
                        x += paint.getTextSize() / 2;
                    } else if (titles[2].length() == 4) {
                        x += paint.getTextSize() / 2;
                    } else if (titles[2].length() == 5) {
                        x += paint.getTextSize() / 2;
                    }
                    y += paint.getTextSize();
                    break;
                case 3:
                    if (titles[3].length() == 2 || titles[3].length() == 1) {
                        x -= paint.getTextSize() * 2.5f;
                    } else if (titles[3].length() == 3) {
                        x -= paint.getTextSize() * 3.5f;
                    } else if (titles[3].length() == 4) {
                        x -= paint.getTextSize() * 4.0f;
                    } else if (titles[3].length() == 5) {
                        x -= paint.getTextSize() * 5.0f;
                    }
                    y += paint.getTextSize() * 1.5f;
                    break;
                case 4:
                    if (titles[4].length() == 2 || titles[4].length() == 1) {
                        x -= paint.getTextSize() * 3.0f;
                    } else if (titles[4].length() == 3) {
                        x -= paint.getTextSize() * 3.5f;
                    } else if (titles[4].length() == 4) {
                        x -= paint.getTextSize() * 5.0f;
                    } else if (titles[4].length() == 5) {
                        x -= paint.getTextSize() * 6.0f;
                    }
                    //                    y -= paint.getTextSize() / 8.0f;
                    y += paint.getTextSize() / 3.0;
                    break;
                default:
            }
            // newpart
            if (i == styleIndex - 1
//                    && canSelectTitle
                    ) {
//                paint.setColor(Color.RED);
//                // ----------- draw title scale effect --------------
//                if (currTitle != null && titleRects != null && titleRects[styleIndex - 1] != null) {
//                    paint.setTextSize(textSize * titleScale);
//                    titleScale += 0.05f;
//                    paint.setAlpha(titleAlpha);
//                    titleAlpha -= 10;
//                    if (titleAlpha < 0) {
//                        titleAlpha = 0;
//                    }
//                    canvas.drawText(currTitle, titleRects[styleIndex - 1].left, titleRects[styleIndex - 1].bottom, paint);
//                    if (titleScale >= 2f) {
//                        resetTitleScale();
//                    }
//                }
//                paint.setColor(Color.parseColor("#cb541c")); // 红
                paint.setColor(TEXT_RED);
                paint.setTextSize(textSize * 1.4f);
            } else if (i == elegentStyleIndex) {
                paint.setColor(TEXT_BLUE);
                paint.setTextSize(textSize * 1.2f);
            } else {
                //                paint.setShader(mShader);
                paint.setColor(textColor);
                paint.setTextSize(textSize);
            }
            canvas.drawText(titles[i], x, y, paint);
            paint.setShader(null);
            if (titleRects == null) {
                titleRects = new RectF[5];
            }
            if (titleRects[i] == null) {
                titleRects[i] = new RectF(x, y - UIUtils.getTextHeight(titles[i], paint)
                        , x + UIUtils.getTextWidth(titles[i], paint), y);
            }
            //            canvas.drawRect(titleRects[i], paint);
        } // draw titles
        //        paint.setShader(null);
        //        drawIndicator2(canvas);
        if (percentPoints != null && percentPoints[0] != null) {
            paint.setColor(externalStyleColor);
            indicatorPath.reset();
            // top
            if (indicatorPointTop.y > percentPoints[0].y) {
                indicatorPointTop.y -= INDICATOR_SPEED;
            } else {
                indicatorPointTop.y = percentPoints[0].y;
                indicatorTopFinish = true; // newpart
            }
            // top right
            if (indicatorPointTopRight.x < percentPoints[1].x) {
                indicatorPointTopRight.x += INDICATOR_SPEED * cos18;
                indicatorPointTopRight.y -= INDICATOR_SPEED * sin18;
            } else {
                indicatorPointTopRight.x = percentPoints[1].x;
                indicatorPointTopRight.y = percentPoints[1].y;
                indicatorTopRightFinish = true;
            }
            // bottom right
            if (indicatorPointBottomRight.x < percentPoints[2].x) {
                indicatorPointBottomRight.x += INDICATOR_SPEED * cos54;
                indicatorPointBottomRight.y += INDICATOR_SPEED * sin54;
            } else {
                indicatorPointBottomRight.x = percentPoints[2].x;
                indicatorPointBottomRight.y = percentPoints[2].y;
                indicatorBottomRightFinish = true;
            }
            // bottom left
            if (indicatorPointBottomLeft.y < percentPoints[3].y) {
                indicatorPointBottomLeft.x -= INDICATOR_SPEED * cos54;
                indicatorPointBottomLeft.y += INDICATOR_SPEED * sin54;
            } else {
                indicatorPointBottomLeft.x = percentPoints[3].x;
                indicatorPointBottomLeft.y = percentPoints[3].y;
                indicatorBottomLeftFinish = true;
            }
            // top left
            if (indicatorPointTopLeft.y > percentPoints[4].y) {
                indicatorPointTopLeft.x -= INDICATOR_SPEED * cos18;
                indicatorPointTopLeft.y -= INDICATOR_SPEED * sin18;
            } else {
                indicatorPointTopLeft.x = percentPoints[4].x;
                indicatorPointTopLeft.y = percentPoints[4].y;
                indicatorTopLeftFinish = true;
            }
            indicatorPath.moveTo(indicatorPointTop.x, indicatorPointTop.y);
            indicatorPath.lineTo(indicatorPointTopRight.x, indicatorPointTopRight.y);
            indicatorPath.lineTo(indicatorPointBottomRight.x, indicatorPointBottomRight.y);
            indicatorPath.lineTo(indicatorPointBottomLeft.x, indicatorPointBottomLeft.y);
            indicatorPath.lineTo(indicatorPointTopLeft.x, indicatorPointTopLeft.y);
            indicatorPath.close();
            canvas.drawPath(indicatorPath, paint);


            // newpart -------------------------------------------------------------------------------------------
            paint.setColor(Color.parseColor("#ff0045"));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(3);
            canvas.drawPath(indicatorPath, paint);
            // circle
            if (indicatorTopFinish && indicatorTopRightFinish && indicatorBottomRightFinish
                     && indicatorBottomLeftFinish && indicatorTopLeftFinish) {
                paint.setStyle(Paint.Style.FILL);
                PointF cp = null;
                switch (styleIndex - 1) {
                    case 0:
                        cp = indicatorPointTop;
                        break;
                    case 1:
                        cp = indicatorPointTopRight;
                        break;
                    case 2:
                        cp = indicatorPointBottomRight;
                        break;
                    case 3:
                        cp = indicatorPointBottomLeft;
                        break;
                    case 4:
                        cp = indicatorPointTopLeft;
                        break;
                    default:
                        break;
                }
                canvas.drawCircle(cp.x, cp.y, maxRadius/26, paint);
            }
            // -------------------------------------------------------------------------------------------
        } //

        //        paint.setColor(Color.RED);
        //        paint.setStrokeWidth(10);
        //        for (int i = 0; i < 5; i++) {
        //            canvas.drawPoint(points[0][i].x, points[0][i].y, paint);
        //        }
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
        setVisibility(View.VISIBLE);
    }

    public void stop() {
        isDrawing = false;
        canDraw = false;
        //        setVisibility(View.INVISIBLE);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

    public void destroy() {
        //        stop();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

    public interface OnRadarClickListener {
        void onRadarClicked();
    }

    private OnRadarClickListener onRadarClickListener;

    public void setOnRadarClickListener(OnRadarClickListener listener) {
        onRadarClickListener = listener;
    }

    //    @Override
    //    public void onClick(View v) {
    //        initData();
    //        if (onRadarClickListener != null) {
    //            onRadarClickListener.onRadarClicked();
    //        }
    //    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtil.d("radar2", "touch  " + event.getX() + "," + event.getY());
                boolean touchedTitle = false;
                if (titleRects != null && canSelectTitle) {
                    for (int i = 0; i < titleRects.length; i++) {
                        RectF rect = titleRects[i];
                        RectF bigRect = new RectF();
                        bigRect.left = rect.left - rect.width();
                        bigRect.top = rect.top - rect.width();
                        bigRect.right = rect.right + rect.width();
                        bigRect.bottom = rect.bottom + rect.width();
                        if (bigRect.contains(event.getX(), event.getY())) {
                            touchedTitle = true;
                            styleIndex = i + 1;
                            currTitle = titles[i];
                            if (onTitleSelectListener != null) {
                                onTitleSelectListener.onTitleSelected(styleIndex);
                            }
                            break;
                        }
                    }
                }
                if (!touchedTitle) {
                    initData();
                    if (onRadarClickListener != null) {
                        onRadarClickListener.onRadarClicked();
                    }
                    timeOnClickedMillis = System.currentTimeMillis();
                }
                break;
            default:
        }
        return true;
    }

    public void startAnim() {
        initData();
        if (onRadarClickListener != null) {
            onRadarClickListener.onRadarClicked();
        }
    }


    //    @Override
    //    public boolean onTouchEvent(MotionEvent event) {
    //        return false;
    ////        return super.onTouchEvent(event);
    //    }
}
