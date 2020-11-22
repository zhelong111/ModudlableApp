package util;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

/**
 * Created by Bruce.Zhou on 2019/4/18 09:22.
 * Email: 907160968@qq.com
 */
public class TextUtil {
    public static void setTextColor(TextView textView, int startIndex, int endIndex, int color) {
        SpannableStringBuilder builder = new SpannableStringBuilder(textView.getText().toString());

        //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        ForegroundColorSpan redSpan = new ForegroundColorSpan(color);
//        ForegroundColorSpan whiteSpan = new ForegroundColorSpan(Color.WHITE);
//        ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.BLUE);
//        ForegroundColorSpan greenSpan = new ForegroundColorSpan(Color.GREEN);
//        ForegroundColorSpan yellowSpan = new ForegroundColorSpan(Color.YELLOW);

        builder.setSpan(redSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        builder.setSpan(whiteSpan, 1, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
//        builder.setSpan(blueSpan, 2, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        builder.setSpan(greenSpan, 3, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        builder.setSpan(yellowSpan, 4,5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(builder);
    }
}
