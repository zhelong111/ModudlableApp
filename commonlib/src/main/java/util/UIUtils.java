package util;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URL;

/**
 * @description UI工具类
 */
public class UIUtils {

    /**
     * dip转换px
     */
    public static int dip2px(Context context, float f) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (f * scale + 0.5f);
    }

    /**
     * dip转换px
     */
    public static int dip2px(Context context, int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal,context.getResources().getDisplayMetrics());
    }

    /**
     * sp转换px
     */
    public static int sp2px(Context context, int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转换dip
     */
    public static int px2dip(Context context, int px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static View inflate(Context context, int resId) {
        return LayoutInflater.from(context).inflate(resId, null);
    }

    /**
     * 获取资源
     */
    public static Resources getResources(Context context) {
        return context.getResources();
    }

    /**
     * 获取文字
     */
    public static String getString(Context context, int resId) {
        return context.getResources().getString(resId);
    }

    /**
     * 获取文字数组
     */
    public static String[] getStringArray(Context context, int resId) {
        return context.getResources().getStringArray(resId);
    }

    /**
     * 获取dimen
     */
    public static int getDimens(Context context, int resId) {
        return getResources(context).getDimensionPixelSize(resId);
    }

    /**
     * 获取drawable
     */
    public static Drawable getDrawable(Context context, int resId) {
        return getResources(context).getDrawable(resId);
    }

    /**
     * 获取颜色
     */
    public static int getColor(Context context, int resId) {
        return getResources(context).getColor(resId);
    }

    /**
     * 获取颜色选择器
     */
    public static ColorStateList getColorStateList(Context context, int resId) {
        return getResources(context).getColorStateList(resId);
    }


    public static void clickLock(final Activity acticity, int id) {
        final View mView = acticity.findViewById(id);
        if (mView == null) {
            return;
        }
        mView.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i >= 0; i--) {
                    try {
                        Thread.sleep(1500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    acticity.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            mView.setEnabled(true);
                        }
                    });
                }
            }

        }).start();
    }

    /**
     * 获取控件宽
     */
    public static int getWidth(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return (view.getMeasuredWidth());
    }

    /**
     * 获取控件高
     */
    public static int getHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return (view.getMeasuredHeight());
    }


    public static void reSetListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition    
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static void setSelocter(final Context context, final ImageView imageView, final String image1, final String image2) {
        final StateListDrawable listDrawable = new StateListDrawable();
        final Handler mHandler = new Handler();
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Drawable nomal = Drawable.createFromStream(new URL(image1).openStream(), "src");
                    Drawable after = Drawable.createFromStream(new URL(image2).openStream(), "src");
                    //Non focused states
                    listDrawable.addState(new int[]{-android.R.attr.state_focused, -android.R.attr.state_selected, -android.R.attr.state_pressed},
                            nomal);
                    listDrawable.addState(new int[]{-android.R.attr.state_focused, android.R.attr.state_selected, -android.R.attr.state_pressed},
                            nomal);
                    //Focused states
                    listDrawable.addState(new int[]{android.R.attr.state_focused, -android.R.attr.state_selected, -android.R.attr.state_pressed},
                            after);
                    listDrawable.addState(new int[]{android.R.attr.state_focused, android.R.attr.state_selected, -android.R.attr.state_pressed},
                            after);
                    //Pressed
                    listDrawable.addState(new int[]{android.R.attr.state_selected, android.R.attr.state_pressed},
                            after);
                    listDrawable.addState(new int[]{android.R.attr.state_pressed},
                            after);
                    mHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            imageView.setBackgroundDrawable(listDrawable);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                super.run();
            }
        };
        thread.start();
    }

    private static int strTime = 0;

    /**
     * 验证码按钮时间跳动
     *
     * @param bt
     * @param context
     */
    public static void TimeDisplay(final TextView bt, final Activity context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 60; i >= 0; i--) {
                    strTime = i;
                    publishProgress(bt, context);
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }).start();
    }

    /**
     * 设置按钮显示的字体
     *
     * @param btGedcode
     * @param mContecxt
     */
    private static void publishProgress(final TextView btGedcode,
                                        final Activity mContecxt) {
        mContecxt.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (strTime == 0) {
                    btGedcode.setClickable(true);
                    // btGedcode.setText("重新获取");
                    btGedcode.setText("重新获取");
                    // btGedcode.setBackgroundResource(R.drawable.btn_verification_code_selector);
                    // btGedcode.setTextColor(Color.parseColor("#b55129"));
                    // btGedcode.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
                } else {
                    btGedcode.setClickable(false);
                    btGedcode.setText(strTime + "s");
                    btGedcode.setBackgroundColor(Color.TRANSPARENT);
                    // btGedcode.setTextColor(Color.parseColor("e5a172"));
                    // btGedcode.setTextSize(NumberUtils.getSp(8));
                }
            }
        });
    }

    public static void replaceTextColor(TextView tv, String text, int start, int end, int color) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        ForegroundColorSpan redSpan = new ForegroundColorSpan(color);
        builder.setSpan(redSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(builder);
    }

    /**
     * 字体宽度
     *
     * @param text
     * @return
     */
    public static float getTextWidth(String text, Paint paint) {
        Rect bound = new Rect();
        paint.getTextBounds(text, 0, text.length(), bound);
        return bound.width();
    }

    /**
     * 字体高度
     *
     * @param text
     * @return
     */
    public static float getTextHeight(String text, Paint paint) {
        Rect bound = new Rect();
        paint.getTextBounds(text, 0, text.length(), bound);
        return bound.height();
    }

    /**
     * 获取图片名称获取图片的资源id的方法
     *
     * @param imageName
     * @return
     */
    public static int getResourceByName(Context ctx, String imageName) {
        int resId = ctx.getResources().getIdentifier(imageName, "drawable", ctx.getPackageName());
        return resId;
    }
}
