package util;

import android.app.NotificationManager;
import android.content.Context;

/**
 * Created by Bruce on 2017/7/21.
 */

public class NotificationUtil {

    //两个参数，第一个为一个图标的ID,第二个为一个字符串对应的ID
    public static void startNM(Context context, String fromUserName)
    {
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//        builder.setContentTitle(context.getString(R.string.notification)).setContentText(
//                context.getString(R.string.someone_requested_to_chat_with_you, fromUserName))
//                .setSmallIcon(R.mipmap.ic_launcher).setAutoCancel(true);
////                .setLights(0xffff3322, 300, 300);
//        builder.setVibrate(new long[]{1000, 2000, 1000, 2000, 1000}); //需要真机测试
////        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/"+R.raw.video_call);
//        //从铃声管理器
//        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        builder.setSound(sound);
////        Intent intent = new Intent(context, ReceiveCallActivity.class);
//        Intent intent = new Intent(context, AnchorMainActivity.class);
//        Bundle data = new Bundle();
//        data.putString("fromUserName", fromUserName); // 即hxId
//        intent.putExtras(data);
//        builder.setContentIntent(PendingIntent.getActivity(context, 0, intent, 0));
//        notificationManager.notify(102, builder.build());
    }

    public static void stopNM(Context context, int strID)
    {
        //获取到系统服务中的通知服务NOTIFICATION_SERVICE
        NotificationManager nManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (strID == -1) {
            nManager.cancelAll();
        } else {
            nManager.cancel(strID);
        }
    }
}
