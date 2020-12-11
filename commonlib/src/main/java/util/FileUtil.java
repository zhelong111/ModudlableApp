package util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {
    public static String fileToString(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                byte[] buffer = new byte[(int) file.length()];
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                bis.read(buffer);
                bis.close();
                String data = Base64.encode(buffer);
                return data;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static byte[] fileToBytes(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                byte[] buffer = new byte[(int) file.length()];
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                bis.read(buffer);
                bis.close();
                return buffer;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static boolean deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }


    //file：要删除的文件夹的所在位置
    public static void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f);
            }
            //            file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 生成缩略图路径
     *
     * @return
     */
    public static String getThumbnailPath() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "mnote_thumb");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            } else {
                mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "mnote_thumb");
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "thumb_" + timeStamp + ".jpg");
        return mediaFile.getAbsolutePath();
    }

    public static boolean isFileExsit(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        return new File(filePath).exists();
    }

    public static String saveBitmap(Bitmap bitmap, String path, int qauntity) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, qauntity, out);
            out.flush();
            out.close();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    public static String saveBitmap(Context context, Bitmap bitmap) {
        return saveBitmap(bitmap, getTempPicPath(context), 100);
    }


    public static String getTempPicPath(Context context) {
        //		File picFolder= GlobalConfig.context.getCacheDir();
        File picFolder = context.getCacheDir();
        if (!picFolder.exists()) {
            picFolder.mkdirs();
        }
        String filePath = picFolder.getAbsolutePath() + "/tempPic.jpg";
        return filePath;
    }

    public static String getTempPicPath(Context context, String fileNameWithSuffix) {
        //		File picFolder= GlobalConfig.context.getCacheDir();
        File picFolder = context.getCacheDir();
        if (!picFolder.exists()) {
            picFolder.mkdirs();
        }
        String filePath = picFolder.getAbsolutePath() + "/" + fileNameWithSuffix;
        return filePath;
    }

    public static String getAppDir() {
        File file = new File(Environment.getExternalStorageDirectory(), "mirrordata");
        if (!file.exists()) {
            file.mkdir();
        }
        return file.getAbsolutePath();
    }


}
