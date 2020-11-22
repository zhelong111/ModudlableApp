package util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * Created by Bruce.Zhou on 2018/1/11.
 */

public class MultiResolutionUtil {

    private static int BASE_DPI = 160; // mdpi

    /**
     * 适配不同分辨率设备
     * @param  view
     */
    public static void adapter(View view) {
        if (view == null) {
            return;
        }
        Context context = view.getContext();
        int left = getScaledSize(context, view.getPaddingLeft());
        int top = getScaledSize(context, view.getPaddingTop());
        int right = getScaledSize(context, view.getPaddingRight());
        int bottom = getScaledSize(context, view.getPaddingBottom());
        view.setPadding(left, top, right, bottom);
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp != null) {
            if (lp.width > 0) {
                lp.width = getScaledSize(context, lp.width);
            }
            if (lp.height > 0) {
                lp.height = getScaledSize(context, lp.height);
            }

            if (lp instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) lp;
                int topMargin = getScaledSize(context, marginLayoutParams.topMargin);
                int leftMargin = getScaledSize(context, marginLayoutParams.leftMargin);
                int bottomMargin = getScaledSize(context, marginLayoutParams.bottomMargin);
                int rightMargin = getScaledSize(context, marginLayoutParams.rightMargin);
                marginLayoutParams.topMargin = topMargin;
                marginLayoutParams.leftMargin = leftMargin;
                marginLayoutParams.bottomMargin = bottomMargin;
                marginLayoutParams.rightMargin = rightMargin;
            }
            view.setLayoutParams(lp);
        }
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            textView.setTextSize(getFontScaledSize(context, textView.getTextSize()));
            textView.setLineSpacing(textView.getLineSpacingExtra(), textView.getLineSpacingMultiplier() );
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textView.setLetterSpacing(textView.getLetterSpacing());
            }
        } else if (view instanceof Button) {
            Button btn = (Button) view;
            btn.setTextSize(getFontScaledSize(context, btn.getTextSize()));
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View childView = (((ViewGroup) view).getChildAt(i));
                adapter(childView);
            }
        }
    }

    public static int getScaledSize(Context context, float originalSize) {
        float scale = getScaleFactor(context);
        return (int) (originalSize * scale);
    }

    private static float getScaleFactor(Context context) {
        return BASE_DPI*1.0f / context.getResources().getDisplayMetrics().densityDpi;
    }

    private static int getScaledSizeOfRightBottom(float originalSize) {
        return (int) ((1.0f/2f) * originalSize);
    }

    public static float getFontScaledSize(Context context, float originalSize) {
        return DensityUtil.px2sp(context, originalSize) * getScaleFactor(context);
//        return DensityUtil.px2sp(context, originalSize) * (1.0f/2.182f);
    }

    /**
     * 当前适配布局的屏幕dpi
     * @param baseDpi
     */
    public static void setBaseDpi(int baseDpi) {
        BASE_DPI = baseDpi;
    }

    public static void adapterActivity(Activity context) {
        View rootView = context.getWindow().getDecorView().findViewById(android.R.id.content);
        MultiResolutionUtil.adapter(rootView);
    }

}
