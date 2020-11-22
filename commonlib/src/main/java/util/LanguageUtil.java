package util;

import android.content.Context;
import android.content.res.Configuration;
import java.util.Locale;

/**
 * Created by Bruce.Zhou on 2018/10/30 09:37.
 * Email: 907160968@qq.com
 */
public class LanguageUtil {

    public enum Language {
        ZH(Locale.SIMPLIFIED_CHINESE), EN(Locale.ENGLISH);

        private Locale locale;
        Language(Locale locale) {
            this.locale = locale;
        }

        public Locale getLocale() {
            return this.locale;
        }
    }

    public static void setLanguage(Context context, Language language) {
        Locale.setDefault(language.getLocale());
        Configuration config = context.getResources().getConfiguration();
        config.locale = language.getLocale();
        context.getResources().updateConfiguration(config
                , context.getResources().getDisplayMetrics());
    }
}
