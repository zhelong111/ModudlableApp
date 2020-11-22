package util;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import ui.anim.Rotate3dAnimation;


/**
 * Created by Bruce on 2017/8/11.
 */

public class AnimUtil {

    private static final String TAG = "AnimUtil";

    public static void moveUpWithFade(View view) {
        ObjectAnimator moveUp = ObjectAnimator.ofFloat(view, "translationY", UIUtils.dip2px(view.getContext(), 100), 0);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0.2f, 1);
        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(moveUp, fadeIn);
        animSet.setDuration(1000);
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.start();
    }

    public static void scaleUp(final View view) {
        if (view != null) {
            view.setScaleX(0);
            view.setScaleY(0);
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0, 1f);
            AnimatorSet animSet = new AnimatorSet();
            //            animSet.setTarget(view);
            animSet.setDuration(1100);
            //            animSet.setInterpolator(new OvershootInterpolator());
            animSet.playTogether(scaleX, scaleY);
            animSet.setStartDelay(120);
            animSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    view.setVisibility(View.VISIBLE);
                }
            });
            animSet.start();
        }
    }

    public static void scaleToDismiss(final View view) {
        if (view != null) {
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f);
                    ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f);
                    ObjectAnimator fadeout = ObjectAnimator.ofFloat(view, "alpha", 1, 0.1f);
                    AnimatorSet animSet = new AnimatorSet();
                    animSet.setTarget(view);
                    animSet.setDuration(650);
                    animSet.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            view.setVisibility(View.GONE);
                        }
                    });
                    animSet.setInterpolator(new AccelerateInterpolator());
                    animSet.playTogether(scaleX, scaleY, fadeout);
                    animSet.start();
                }
            }, 320);
        }
    }


//    /**
//     * 点击放大图片动画效果
//     * 遍历ViewGroup
//     */
//
//    public static void resetScale(RecyclerView parent, int currIndex) {
//        for (int i = 0; i < parent.getChildCount(); i++) {
//
//            if (i == currIndex) {
//                View view = parent.getChildAt(currIndex);
//                AnimatorSet set = new AnimatorSet();
//                set.playTogether(
//                        ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.3f),
//                        ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.3f)
//                );
//                set.setDuration(300).start();
//
//            } else {
//                View view = parent.getChildAt(i);
//                AnimatorSet set = new AnimatorSet();
//                set.playTogether(
//                        ObjectAnimator.ofFloat(view, "scaleX", 1f),
//                        ObjectAnimator.ofFloat(view, "scaleY", 1f)
//                );
//                set.setDuration(300).start();
//            }
//
//        }
//    }


    /**
     * 搭配图从上往下滑动显示出来
     *
     * @param height 最终显示出来的区域的高度dp
     */
    public static void slideShowArea(View v, int height, long durationTime) {
        v.setVisibility(View.VISIBLE);
        ValueAnimator animator = createDropAnimator(v, 0,
                height);
        animator.setDuration(durationTime);
        animator.start();

    }


    /**
     * 动画过程中不断改变高度
     */
    private static ValueAnimator createDropAnimator(final View v, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                int value = (int) arg0.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.height = value;
                v.setLayoutParams(layoutParams);

            }
        });

        return animator;
    }


    /**
     * 从下往上滑动隐藏布局
     */
    public static void slideHideArea(View view, View selectView, long durationTime) {
        int origHeight = view.getHeight();
        ValueAnimator animator = createDropAnimator(view, origHeight, 0);
        animator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                view.setVisibility(View.GONE);
            }

        });
        animator.setDuration(durationTime);
        animator.start();

    }


    public static ObjectAnimator rotateAnim(View view, int duration, boolean clockwise) {
        ObjectAnimator rotate;
        if (clockwise) {
            rotate = ObjectAnimator.ofFloat(view, "rotation", 0, 360);
        } else {
            rotate = ObjectAnimator.ofFloat(view, "rotation", 0, -360);
        }
        rotate.setDuration(duration);
        rotate.setRepeatCount(ValueAnimator.INFINITE);
        rotate.setRepeatMode(ValueAnimator.INFINITE);
        //旋转动画不停顿
        rotate.setInterpolator(new LinearInterpolator());
        rotate.start();
        return rotate;
        //        rotate.cancel();
    }

    public static void scaleBlowUp(View view) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.5f, 0.7f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.5f, 0.7f);
        set.playTogether(scaleX, scaleY);
        set.setInterpolator(new LinearInterpolator());
        set.setDuration(1000);
        set.start();
    }

    public static void bannerScaleBlowUp(View view, int duration) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.3f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.3f, 1f);
        scaleX.setRepeatCount(ValueAnimator.INFINITE);
        scaleX.setRepeatMode(ValueAnimator.RESTART);
        scaleY.setRepeatCount(ValueAnimator.INFINITE);
        scaleY.setRepeatMode(ValueAnimator.RESTART);
        set.playTogether(scaleX, scaleY);
        set.setInterpolator(new OvershootInterpolator());
        set.setDuration(duration);
        set.start();
    }


    public static AnimatorSet scaleShowRedPacketTip(View view) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.3f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.3f, 1f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0.3f, 1f);
        set.playTogether(scaleX, scaleY, alpha);
        set.setDuration(400);
        set.setInterpolator(new LinearInterpolator());
        set.start();
        return set;
    }

    public static void startRotation(ImageView image, float start, float end) {
        // 计算中心点
        final float centerX = image.getWidth() / 2.0f;
        final float centerY = image.getHeight() / 2.0f;
        //        Log.d(TAG, "centerX=" + centerX + ", centerY=" + centerY);
        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        //Z轴的缩放为0
        Rotate3dAnimation rotation = new Rotate3dAnimation(start, end, centerX, centerY, -50f, true);
        rotation.setDuration(800);
        rotation.setFillAfter(true);
        rotation.setStartOffset(0);
        //rotation.setInterpolator(new AccelerateInterpolator());
        //匀速旋转
        rotation.setInterpolator(new LinearInterpolator());
        //设置监听
        //        startNext = new StartNextRotate();
        //        rotation.setAnimationListener(startNext);
        image.startAnimation(rotation);
    }

    /**
     * Y轴平移渐变动画
     */
    public static android.animation.AnimatorSet translateYWithAlphaAnim(View view, int duration, float translateValue) {
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
        android.animation.AnimatorSet set = new android.animation.AnimatorSet();
        android.animation.ObjectAnimator translate = android.animation.ObjectAnimator.ofFloat(view, "translationY", translateValue, 0);
        android.animation.ObjectAnimator alpha = android.animation.ObjectAnimator.ofFloat(view, "alpha", 0, 1);
        set.playTogether(translate, alpha);
        set.setInterpolator(new DecelerateInterpolator());
        set.setDuration(duration);
        set.start();
        return set;
    }


    /**
     * X轴平移渐变平移动画
     */
    public static android.animation.AnimatorSet translateXWithAlphaAnim(View view, int duration, float translateValue) {
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
        android.animation.AnimatorSet set = new android.animation.AnimatorSet();
        android.animation.ObjectAnimator translate = android.animation.ObjectAnimator.ofFloat(view, "translationX", translateValue, 0);
        android.animation.ObjectAnimator alpha = android.animation.ObjectAnimator.ofFloat(view, "alpha", 0, 1);
        set.playTogether(translate, alpha);
        set.setInterpolator(new DecelerateInterpolator());
        set.setDuration(duration);
        set.start();
        return set;
    }

    public static AnimatorSet bodySampleAnim(View view) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1, 0);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1, 0);
        ObjectAnimator translateX = ObjectAnimator.ofFloat(view, "translationX", 430);
        ObjectAnimator translateY = ObjectAnimator.ofFloat(view, "translationY", 750);
        set.playTogether(scaleX, scaleY, translateX, translateY);
        set.setDuration(800);
        set.setInterpolator(new LinearInterpolator());
        set.start();
        return set;
    }


}
