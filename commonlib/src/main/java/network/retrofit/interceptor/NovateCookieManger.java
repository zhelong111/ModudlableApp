package network.retrofit.interceptor;

import org.jetbrains.annotations.NotNull;
import java.util.List;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class NovateCookieManger implements CookieJar {
    private static final String TAG = "NovateCookieManger";

    @NotNull
    @Override
    public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
        return null;
    }

    @Override
    public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {

    }
}