package ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import util.LogUtil;


/**
 * Created by Bruce.Zhou on 2018/5/17.
 */

public class GestureBaseView extends RelativeLayout implements GestureDetector.OnGestureListener {

    private GestureDetector gestureDetector;
    private Scroller scroller;
    private int measuredWidth;
    private int measuredHeight;
    private float scrollRate; // 滚动百分值 0~1,大于=1则不再回到原点
    private boolean isHorizontal = true; // 手势方向
    private boolean scrollable = true; // 是否视图跟随手势移动
    private Direction direction = Direction.NONE;

    public GestureBaseView(Context context) {
        super(context);
        init();
    }

    public GestureBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GestureBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setEnabled(true);
        setClickable(true);
        gestureDetector = new GestureDetector(getContext(), this);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        if (scrollable) {
//                            if (scrollRate < 1) {
                                reset(0, 0);
//                            }
                        }
                        if (myGestureListener != null) {
                            myGestureListener.onTouchUp(scrollRate, direction);
                        }
                        break;
                    default:
                }
                return gestureDetector.onTouchEvent(event);
            }
        });
        scroller = new Scroller(getContext());
    }

    public void setIsHorizontal(boolean isHorizontal) {
        this.isHorizontal = isHorizontal;
    }

    /**
     * 是否视图跟随手势移动
     *
     * @param scrollable
     */
    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }

    private static int MAX_TRICKER_DISTANCE = 500;

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
        if (myGestureListener != null) {
            if (isHorizontal) {
                scrollRate = Math.min(Math.abs(scroller.getCurrX()), MAX_TRICKER_DISTANCE) * 1.0f / MAX_TRICKER_DISTANCE;
            } else {
                scrollRate = Math.min(Math.abs(scroller.getCurrY()), MAX_TRICKER_DISTANCE) * 1.0f / MAX_TRICKER_DISTANCE;
            }

            if (scroller.getCurrX() > 0) {
                direction = Direction.LEFT;
            } else if (scroller.getCurrX() < 0) {
                direction = Direction.RIGHT;
            } else if (scroller.getCurrY() > 0) {
                direction = Direction.UP;
            } else if (scroller.getCurrY() < 0) {
                direction = Direction.DOWN;
            } else {
                direction = Direction.NONE;
            }
            myGestureListener.onScroll(scrollRate, direction);
            LogUtil.d("gesture", "scrollx : " + scroller.getCurrX() + ",scrollY : " + scroller.getCurrY()
                    + ", " + String.format("%.2f", scrollRate) + ", direction=" + direction);
        }
    }


    public interface MyGestureListener {
        void onFlingLeft();

        void onFlingRight();

        void onFlingUp();

        void onFlingDown();

        void onTouchDown();

        void onTouchUp(float rate, Direction direction);

        void onScroll(float rate, Direction direction); // rate 0~1 1表示内容不反弹回原点的临界值
    }

    public enum Direction {
        NONE, LEFT, RIGHT, UP, DOWN
    }

    public static class MyGestureListenerAdapter implements MyGestureListener {
        @Override
        public void onFlingLeft() {
        }

        @Override
        public void onFlingRight() {
        }

        @Override
        public void onFlingUp() {
        }

        @Override
        public void onFlingDown() {
        }

        @Override
        public void onTouchDown() {
        }

        @Override
        public void onTouchUp(float rate, Direction direction) {
        }

        @Override
        public void onScroll(float rate, Direction direction) {
        }
    }

    private MyGestureListener myGestureListener;

    public void setMyGestureListener(MyGestureListener listener) {
        myGestureListener = listener;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        LogUtil.d("gesture", "touch down");
        if (myGestureListener != null) {
            myGestureListener.onTouchDown();
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        LogUtil.d("gesture", "onShowPress..." + e.getAction());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (scrollable) {
            if (isHorizontal) {
                beginScroll((int) ((distanceX - 0.5f) / 2), 0);
            } else {
                beginScroll(0, (int) (distanceY - 0.5f) / 2);
            }
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    protected void beginScroll(int dx, int dy) {
        scroller.startScroll(scroller.getFinalX(), scroller.getFinalY(), dx, dy, 1400);
        invalidate();
    }

    protected void reset(int x, int y) {
        int dx = x - scroller.getFinalX();
        int dy = y - scroller.getFinalY();
        beginScroll(dx, dy);
    }


    private static final int MIN_SPEED = 1000;
    private static final int MIN_SPEED_TOLERONCE = 1000;

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (myGestureListener != null) {
            if (velocityX > MIN_SPEED && Math.abs(velocityY) < MIN_SPEED_TOLERONCE) {
                myGestureListener.onFlingRight();
                //                LogUtil.d("gesture", "fling right");
            } else if (velocityX < -MIN_SPEED && Math.abs(velocityY) < MIN_SPEED_TOLERONCE) {
                myGestureListener.onFlingLeft();
                //                LogUtil.d("gesture", "fling left");
            } else if (velocityY > MIN_SPEED ) {
                //&& Math.abs(velocityX) < MIN_SPEED_TOLERONCE
                myGestureListener.onFlingDown();
                //                LogUtil.d("gesture", "fling down");
            } else if (velocityY < -MIN_SPEED ) {
                myGestureListener.onFlingUp();
//                && Math.abs(velocityX) < MIN_SPEED_TOLERONCE
                //                LogUtil.d("gesture", "fling up");
            }
            //            LogUtil.d("gesture", "onFling " + velocityX + ", " + velocityY);
            //            Log.d("gesture", "fling...");
        }
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        MAX_TRICKER_DISTANCE = (int) (measuredWidth / 5.0f);
        LogUtil.d("gesture", "w=" + measuredWidth + ", h=" + measuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }
}
