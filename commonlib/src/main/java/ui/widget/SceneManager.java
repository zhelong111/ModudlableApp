package ui.widget;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bruce.modulableapp.commonlib.R;

/**
 * Created by Bruce.Zhou on 2016/2/28.
 */
public class SceneManager {
    public static final String EXTRA_TRANSITION = "EXTRA_TRANSITION";
    public static final String TRANSITION_FADE_FAST = "FADE_FAST";
    public static final String TRANSITION_FADE_SLOW = "FADE_SLOW";
    public static final String TRANSITION_SLIDE_RIGHT = "SLIDE_RIGHT";
    public static final String TRANSITION_SLIDE_BOTTOM = "SLIDE_BOTTOM";
    public static final String TRANSITION_EXPLODE = "EXPLODE";
    public static final String TRANSITION_EXPLODE_BOUNCE = "EXPLODE_BOUNCE";

    public static void toScene(Context context, Class<? extends Activity> target, Bundle data) {
        if (context == null || ((Activity) context).isFinishing()) {
            return;
        }

        Intent intent = new Intent();
        intent.setClass(context, target);
        if (data != null) {
            intent.putExtras(data);
        }
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }


    public static void startSlideRightTransition(Activity from, Class<? extends Activity> target, Bundle data) {
        Intent intent = new Intent(from, target);
        if (data != null) {
            intent.putExtras(data);
        }
        intent.putExtra(SceneManager.EXTRA_TRANSITION, SceneManager.TRANSITION_SLIDE_RIGHT);
        startActivityWithOptions(from, intent);
    }

    public static void startSlideBottomTransition(Activity from, Class<? extends Activity> target, Bundle data) {
        Intent intent = new Intent(from, target);
        if (data != null) {
            intent.putExtras(data);
        }
        intent.putExtra(SceneManager.EXTRA_TRANSITION, SceneManager.TRANSITION_SLIDE_BOTTOM);
        startActivityWithOptions(from, intent);
    }

    public static void startFadeInSlowTransition(Activity from, Class<? extends Activity> target, Bundle data) {
        Intent intent = new Intent(from, target);
        if (data != null) {
            intent.putExtras(data);
        }
        intent.putExtra(SceneManager.EXTRA_TRANSITION, SceneManager.TRANSITION_FADE_SLOW);
        startActivityWithOptions(from, intent);
    }

    private static void startActivityWithOptions(Activity from, Intent intent) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions transitionActivity = null;
            transitionActivity = ActivityOptions.makeSceneTransitionAnimation(from);
            from.startActivity(intent, transitionActivity.toBundle());
        } else {
            from.startActivity(intent);
        }
    }

    public static void startScaleTransition(Activity from, Class<? extends Activity> target, View sharedElement, Bundle data) {
        Intent intent = new Intent();
        ActivityOptions options = null;
        if (data != null) {
            intent.putExtras(data);
        }
        intent.setClass(from, target);
        if (sharedElement != null && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            sharedElement.setTransitionName("sharedElementName");
            options = ActivityOptions.makeSceneTransitionAnimation(from, sharedElement, "sharedElementName");
            from.startActivity(intent, options.toBundle());
        } else {
            from.startActivity(intent);
            from.overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
        }
    }

    public static void toSceneWithScale(Activity from, Class<? extends Activity> target, View v, Bundle data) {
        Intent intent = new Intent();
        ActivityOptions options = null;
        if (data != null) {
            intent.putExtras(data);
        }
        intent.setClass(from, target);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            options = ActivityOptions.makeScaleUpAnimation(v, 0, 0, 0, 0);
            from.startActivity(intent, options.toBundle());
        } else {
            from.startActivity(intent);
        }
    }

    public static void backHome(Activity context) {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(home);
    }

    public static void goBack(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.push_up_in, R.anim.push_down_out);
    }
}
