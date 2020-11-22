package exception;

import android.os.Environment;

import java.io.File;

import data.Constants;

/**
 * Created by Yuaihen.
 * on 2018/11/22
 */
public class SdcardConfig {

    private static SdcardConfig sSdcardConfig;

    public static synchronized SdcardConfig getInstance() {
        if (sSdcardConfig == null) {
            sSdcardConfig = new SdcardConfig();
        }
        return sSdcardConfig;
    }

    /**
     * sd卡初始化
     */
    public void initSdcard() {
        if (!hasSDCard()) {
            return;
        }
        File logFile = new File(Constants.LOG_CATCH_DIRECTORY);
        if (!logFile.exists()) {
            logFile.mkdirs();
        }
    }

    /**
     * 判断是否存在SDCard
     *
     * @return
     */
    public boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }
}
