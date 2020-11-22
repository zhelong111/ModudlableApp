/*
 * {EasyGank}  Copyright (C) {2015}  {CaMnter}
 *
 * This program comes with ABSOLUTELY NO WARRANTY; for details type `toast w'.
 * This is free software, and you are welcome to redistribute it
 * under certain conditions; type `toast c' for details.
 *
 * The hypothetical commands `toast w' and `toast c' should toast the appropriate
 * parts of the General Public License.  Of course, your program's commands
 * might be different; for a GUI interface, you would use an "about box".
 *
 * You should also get your employer (if you work as a programmer) or school,
 * if any, to sign a "copyright disclaimer" for the program, if necessary.
 * For more information on this, and how to apply and follow the GNU GPL, see
 * <http://www.gnu.org/licenses/>.
 *
 * The GNU General Public License does not permit incorporating your program
 * into proprietary programs.  If your program is a subroutine library, you
 * may consider it more useful to permit linking proprietary applications with
 * the library.  If this is what you want to do, use the GNU Lesser General
 * Public License instead of this License.  But first, please read
 * <http://www.gnu.org/philosophy/why-not-lgpl.html>.
 */

package ui.widget;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.DrawableRes;
import com.bruce.modulableapp.commonlib.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;


/**
 * Description：GlideUtils
 * Time：2016-01-04 22:19
 */
public class GlideUtils {

    private static final String TAG = "GlideUtils";

    /**
     * glide加载图片
     *
     * @param view view
     * @param url  url 本地或网络图片地址
     */
    public static void display(ImageView view, String url) {
        displayUrl(view, url, R.color.bg_gray);
    }

    public static void displayWithoutCache(ImageView view, String url) {
        displayUrlWithoutCache(view, url, R.color.bg_gray);
        //        displayUrlWithoutCache(view, url, R.drawable.iv_loading);
    }


    public static void preloadImage(Context context, String url) {
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).preload();
    }

    /**
     * ImageView 为Wrap_content时,加载url的默认大小
     *
     * @param view
     * @param url
     */
    public static void displayDefaultSize(final ImageView view, String url) {
        // 不能崩
        if (view == null) {
            Log.e("GlideUtils", "GlideUtils -> display -> imageView is null");
            return;
        }
        Context context = view.getContext();
        // View你还活着吗？
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }

        try {
            Glide.with(context)
                    .load(url)
                    .placeholder(R.color.bg_gray)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .into(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 预加载图片
     *
     * @param view
     * @param url
     */
    public static void displayUrlWithPreload(Context context, ImageView view, String url) {
        // 不能崩
        if (view == null) {
            Log.e("GlideUtils", "GlideUtils imageView is null");
            return;
        }

        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(view);

    }


    public static void display(final ImageView view, String url, float sizeMultiplier) {
        // 不能崩
        if (view == null) {
            Log.e("GlideUtils", "GlideUtils -> display -> imageView is null");
            return;
        }
        Context context = view.getContext();
        // View你还活着吗？
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }

        try {
            Glide.with(context)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.color.bg_gray)
                    .crossFade()
                    .centerCrop()
                    .sizeMultiplier(sizeMultiplier)
                    .into(view)
                    .getSize(new SizeReadyCallback() {
                        @Override
                        public void onSizeReady(int width, int height) {
                            if (!view.isShown()) {
                                view.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displayWithoutCenterCrop(final ImageView view, String url) {
        // 不能崩
        if (view == null) {
            Log.e("GlideUtils", "GlideUtils -> display -> imageView is null");
            return;
        }
        Context context = view.getContext();
        // View你还活着吗？
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }

        try {
            Glide.with(context)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.color.bg_gray)
                    .crossFade()
                    .into(view)
                    .getSize(new SizeReadyCallback() {
                        @Override
                        public void onSizeReady(int width, int height) {
                            if (!view.isShown()) {
                                view.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * glide加载图片
     *
     * @param view         view
     * @param url          url
     * @param defaultImage defaultImage
     */
    private static void displayUrl(final ImageView view, String url, @DrawableRes int defaultImage) {
        // 不能崩
        if (view == null) {
            Log.e("GlideUtils", "GlideUtils -> display -> imageView is null");
            return;
        }
        Context context = view.getContext();
        // View你还活着吗？
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }

        try {
            Glide.with(context)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    //                    .dontAnimate()
                    // .placeholder(defaultImage)
                    .crossFade()
                    //                    .centerCrop()  //统一在Imageview里面设置
                    .into(view);
            //                    .getSize(new SizeReadyCallback() {
            //                        @Override
            //                        public void onSizeReady(int redPacketWidth, int redPacketHeight) {
            //                            if (!view.isShown()) {
            //                                view.setVisibility(View.VISIBLE);
            //                            }
            //                        }
            //                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void displayUrlWithoutCache(final ImageView view, String url, @DrawableRes int defaultImage) {
        // 不能崩
        if (view == null) {
            Log.e("GlideUtils", "GlideUtils -> display -> imageView is null");
            return;
        }
        Context context = view.getContext();
        // View你还活着吗？
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }

        try {
            Glide.with(context)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(defaultImage)
                    .crossFade()
                    .into(view)
                    .getSize((width, height) -> {
                        if (!view.isShown()) {
                            view.setVisibility(View.VISIBLE);
                        }
                    })
            ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displayNative(final ImageView view, @DrawableRes int resId) {
        // 不能崩
        if (view == null) {
            Log.e("GlideUtils", "GlideUtils -> display -> imageView is null");
            return;
        }
        Context context = view.getContext();
        // View你还活着吗？
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }

        try {
            Glide.with(context)
                    .load(resId)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .centerCrop()
                    .into(view)
                    .getSize(new SizeReadyCallback() {
                        @Override
                        public void onSizeReady(int width, int height) {
                            if (!view.isShown()) {
                                view.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void displayCircleHeader(final ImageView view, @DrawableRes int res) {
        // 不能崩
        if (view == null) {
            Log.e("GlideUtils", "GlideUtils -> display -> imageView is null");
            return;
        }
        Context context = view.getContext();
        // View你还活着吗？
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }

        try {
            Glide.with(context)
                    .load(res)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.color.bg_gray)
                    .bitmapTransform(new GlideCircleTransform(context))
                    .crossFade()
                    .into(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displayCircleHeader(ImageView view, String url) {
        // 不能崩
        if (view == null) {
            Log.e("GlideUtils", "GlideUtils -> display -> imageView is null");
            return;
        }
        Context context = view.getContext();
        // View你还活着吗？
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }

        try {
            Glide.with(context)
                    .load(url)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.color.bg_gray)
                    .bitmapTransform(new GlideCircleTransform(context))
                    .crossFade()
                    .into(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void display(Context context, ImageView iv, String url) {
        Glide.with(context).load(url)
                .crossFade(400)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv);
    }

    public static void display(Context context, ImageView iv, String url, float sizeMultiplier) {
        Glide.with(context).load(url)
                .crossFade()
                .sizeMultiplier(sizeMultiplier)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv);
    }


    public static void loadGif(ImageView view, @DrawableRes int res) {
        // 不能崩
        if (view == null) {
            Log.e("GlideUtils", "GlideUtils -> display -> imageView is null");
            return;
        }
        Context context = view.getContext();
        // View你还活着吗？
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }

        try {
            Glide.with(context)
                    .load(res)
                    .asGif()
                    .into(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
