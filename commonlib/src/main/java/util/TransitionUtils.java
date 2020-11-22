package util;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;


/**
 * 转场动画
 */
public class TransitionUtils {

    private static final int sShortDuration = 500;
    private static final int sLongDuration = 1000;
    private static final int sVeryLong = 1200;


    public static void startActivityWithSharedElements(Activity activity, Intent intent, View view, String tag) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation
                            (activity, view, tag)//Tag.AVATER
                            .toBundle());
        } else {
            activity.startActivity(intent);
        }
    }

    public static void startActivityWithSharedElements() {
        //        Intent i = new Intent(getContext(), GoodsDetailActivity.class);
        //        Activity activity = (Activity) getContext();
        //        ActivityOptionsCompat optionsCompat =
        //                ActivityOptionsCompat.makeSceneTransitionAnimation(activity, ivRecommendGoods, "sharedElementName");
        //        activity.startActivity(i, optionsCompat.toBundle());

    }


    /**
     * 不携带参数的跳转
     * startMDActivity
     *
     * @param context     上下文(即当前视图所在的Activity或者View)
     * @param distination 目标页面(活动)
     */
    public static void startMDActivity(Activity context, Class<? extends Activity> distination) {
        Intent intent = new Intent(context, distination);
        //如果APP运行的目标机型的SDK版本>=21(LOLLIPOP),则执行爆炸转场动画
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 包含新API的代码块(兼容Android5.0以上的手机)
            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context).toBundle());
        } else {
            // 包含旧的API的代码块(兼容Android5.0以下的手机)
            context.startActivity(intent);
        }
    }

    public static void startMDActivityWithString(Activity context, Class<? extends Activity> distination, String name, String data) {
        //如果APP运行的目标机型的SDK版本>=21(LOLLIPOP),则执行爆炸转场动画
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(context, distination);
            intent.putExtra("name", data);
            // 包含新API的代码块(兼容Android5.0以上的手机)
            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context).toBundle());
        } else {
            // 包含旧的API的代码块(兼容Android5.0以下的手机)
            //            IntentUtils.skipAnotherActivity(context, distination);
            Intent intent = new Intent(context, distination);
            intent.putExtra("name", data);
            context.startActivity(intent);
        }
    }


    /**
     * 专门为头条跳转头条详情封装的一个带参数的跳转方法
     *
     * @param context     上下文
     * @param distination 目标页面
     * @param name1       参数名1
     * @param data1       数据1
     * @param name2       参数名2
     * @param data2       数据2
     */
    public static void startMDActivityWithStrings(Activity context, Class<? extends Activity> distination, String name1, String data1, String name2, int data2) {
        Intent intent = new Intent(context, distination);
        intent.putExtra("name1", data1);
        intent.putExtra("name2", data2);
        //如果APP运行的目标机型的SDK版本>=21(LOLLIPOP),则执行爆炸转场动画
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 包含新API的代码块(兼容Android5.0以上的手机)
            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context).toBundle());
        } else {
            // 包含旧的API的代码块(兼容Android5.0以下的手机)
            context.startActivity(intent);
        }
    }


    public static void startMDActivityWithStringArrary(Activity context, Class<? extends Activity> distination, String name, String[] data) {
        Intent intent = new Intent(context, distination);
        intent.putExtra("name", data);
        //如果APP运行的目标机型的SDK版本>=21(LOLLIPOP),则执行爆炸转场动画
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 包含新API的代码块(兼容Android5.0以上的手机)
            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context).toBundle());
        } else {
            // 包含旧的API的代码块(兼容Android5.0以下的手机)
            context.startActivity(intent);
        }
    }


    public static void startMDActivityWithInt(Activity context, Class<? extends Activity> distination, String name, int data) {
        Intent intent = new Intent(context, distination);
        intent.putExtra("name", data);
        //如果APP运行的目标机型的SDK版本>=21(LOLLIPOP),则执行爆炸转场动画
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 包含新API的代码块(兼容Android5.0以上的手机)
            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(context).toBundle());
        } else {
            // 包含旧的API的代码块(兼容Android5.0以下的手机)
            context.startActivity(intent);
        }
    }


    /**
     * 封装了一个转场分解动画***********************************************************************
     *
     * @param context 上下文
     */
    public static void explodeAinimation(Activity context) {
        //进入退出效果 注意这里 创建的效果对象是 Explode()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context.getWindow().setEnterTransition(new Explode().setDuration(sVeryLong));//合成
            context.getWindow().setExitTransition(new Explode().setDuration(sVeryLong));//分解
        }
    }

    /**
     * 封装了一个转场淡入淡出动画***********************************************************************
     *
     * @param context 上下文
     */
    public static void fadeAinimation(Activity context) {
        //进入退出效果 注意这里 创建的效果对象是 Fade()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context.getWindow().setEnterTransition(new Fade().setDuration(sShortDuration));//淡入
            context.getWindow().setExitTransition(new Fade().setDuration(sShortDuration));//淡出
        }
    }


    /**
     * 封装了一个转场滑入滑出动画***********************************************************************
     *
     * @param context 上下文
     */
    public static void slideAinimation(Activity context) {
        //进入退出效果 注意这里 创建的效果对象是 Slide()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context.getWindow().setEnterTransition(new Slide().setDuration(sShortDuration));//滑入
            context.getWindow().setExitTransition(new Slide().setDuration(sShortDuration));//滑出
        }
    }


    /**
     * @param activity    当前页上下文
     * @param distination 目标页面的Activity类
     * @param view        共享元素
     */
    public static void sharedElementsAinimation(Activity activity, Class<? extends Activity> distination, View view, String TAG) {
        //进入退出效果 注意这里 创建的效果对象是 Slide()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.startActivity(new Intent(activity, distination),
                    ActivityOptions.makeSceneTransitionAnimation
                            (activity, view, TAG)
                            .toBundle());

        }
    }


}