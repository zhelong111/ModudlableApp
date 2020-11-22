package util;

public class ClickUtil {
    private static final int MIN_DELAY_TIME = 300;  // 两次点击间隔不能少于1000ms
    private static long lastClickTime;

    public static boolean isValidClick() {
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) < MIN_DELAY_TIME) {
            return false;
        }
        lastClickTime = currentClickTime;
        return true;
    }

    public static boolean isValidClick(int time) {
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) < time) {
            return false;
        }
        lastClickTime = currentClickTime;
        return true;
    }
}
