package util;

import java.util.Arrays;

/**
 * Created by Bruce.Zhou on 2019/4/10 10:11.
 * Email: 907160968@qq.com
 */
public class ComputeUtil {
    /**
     * 将返回的雷达图数值按一定比例重新计算  避免雷达图区域太小
     *
     * @param styleScoreArray 雷达图的数值  男性5个 女性8个
     * @return 返回重新计算过后的雷达图数值
     */
    public static float[] getOptimizedStyleScores(float[] styleScoreArray, int maxValue) {
        if (styleScoreArray == null || styleScoreArray.length == 0) {
            return null;
        }
        float[] mNewStyleScoreArray = new float[8];
        float[] mCocyScoreArray = new float[styleScoreArray.length];
        System.arraycopy(styleScoreArray, 0, mCocyScoreArray, 0, styleScoreArray.length);
        Arrays.sort(mCocyScoreArray);

        if (styleScoreArray[mCocyScoreArray.length - 1] < maxValue) {
            float factor = maxValue / mCocyScoreArray[mCocyScoreArray.length - 1];
            for (int i = 0; i < styleScoreArray.length; i++) {
                mNewStyleScoreArray[i] = styleScoreArray[i] * factor / 100f;
            }
        } else {
            for (int i = 0; i < styleScoreArray.length; i++) {
                mNewStyleScoreArray[i] = styleScoreArray[i] / 100f;
            }
        }
        return mNewStyleScoreArray;
    }
}
