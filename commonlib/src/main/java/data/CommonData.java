package data;

import android.graphics.Bitmap;

/**
 * Created by Bruce on 2018/1/6.
 */

public class CommonData {
    public static String scanUserPhotoStr = null;

    public static float subDx = 0; // 需要减去的坐标
    public static float subDY = 0;
    public static float bodyScaleRate = 1; // 中途缩放
    public static float addDx = 0; // 需要加上的坐标
    public static float addDy = 0;
    public static float finalBodyScaleRate = 1;
    public static boolean isFullBody = false;
    public static Bitmap faceBitmap;

    // 保存人脸和对应的body截图信息
//    public static Map<Bitmap, BodyBitmapInfo> faceToBodyMap = new HashMap<>();
}
