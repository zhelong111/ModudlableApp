package ui.tip;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bruce.modulableapp.commonlib.R;

/**
 * Created by Administrator on 2016/3/6.
 */
public class TipUtil {
    private static Toast toast;
    private static TextView mTvMsg;
    private static Toast sBigToast;

    public static void show(final Activity context, final String msg) {
        if (context == null || context.isFinishing()) {
            return;
        }
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void show(Context context, final String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        }
        toast.setText(msg);
        toast.show();
    }

    /**
     * 将Toast封装在一个方法中，在其它地方使用时直接输入要弹出的内容即可
     */
    public static void showBigMsg(Context context, String message) {
        if (sBigToast == null) {
            View view = View.inflate(context, R.layout.layout_toast, null); //加載layout下的布局
            mTvMsg = view.findViewById(R.id.tv_msg);
            if (!TextUtils.isEmpty(message)) {
                mTvMsg.setText(message);
            }

            sBigToast = new Toast(context);
            sBigToast.setGravity(Gravity.CENTER, 0, 0);//setGravity用来设置Toast显示的位置，相当于xml中的android:gravity或android:layout_gravity
            //            sBigToast.setDuration(Toast.LENGTH_LONG);//setDuration方法：设置持续时间，以毫秒为单位。该方法是设置补间动画时间长度的主要方法
            sBigToast.setView(view);
        }

        if (!TextUtils.isEmpty(message)) {
            mTvMsg.setText(message);
            sBigToast.show();
        }
    }

    public static void showBigMsg(Context context, String message, boolean longTime) {
        if (sBigToast == null) {
            View view = View.inflate(context, R.layout.layout_toast, null); //加載layout下的布局
            mTvMsg = view.findViewById(R.id.tv_msg);
            if (!TextUtils.isEmpty(message)) {
                mTvMsg.setText(message);
            }

            sBigToast = new Toast(context);
            sBigToast.setGravity(Gravity.CENTER, 0, 0);//setGravity用来设置Toast显示的位置，相当于xml中的android:gravity或android:layout_gravity
            //            sBigToast.setDuration(Toast.LENGTH_LONG);//setDuration方法：设置持续时间，以毫秒为单位。该方法是设置补间动画时间长度的主要方法
            sBigToast.setView(view);
            if (longTime) {
                sBigToast.setDuration(Toast.LENGTH_LONG);
            }
        }

        if (!TextUtils.isEmpty(message)) {
            mTvMsg.setText(message);
            sBigToast.show();
        }
    }

}
