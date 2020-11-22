package util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import data.CommonData;

/**
 * 获得照片中的人脸
 * Created by Bruce.Zhou on 2017/12/21.
 */
public class FaceDetectManager {
    private static FaceDetectManager instance;
    private FaceDetector faceDetector;
    private static final int MAX_FACE_COUNT = 3;

    protected FaceDetectManager() {
    }

    public static FaceDetectManager getInstance() {
        if (instance == null) {
            synchronized (FaceDetectManager.class) {
                if (instance == null) {
                    instance = new FaceDetectManager();
                }
            }
        }
        return instance;
    }

    public Bitmap getBigestFace(Bitmap srcBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        try {
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() / scaleSpeed), (int) (bitmap.getHeight() / scaleSpeed), true);

        faceDetector = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), MAX_FACE_COUNT);
        FaceDetector.Face[] faces = new FaceDetector.Face[MAX_FACE_COUNT];
        int numberOfDetectedFace = faceDetector.findFaces(bitmap, faces);
//        float maxEyeDistance = 0;
        FaceDetector.Face maxFace = null;
//        for (int i = 0; i < numberOfDetectedFace; i++) {
//            FaceDetector.Face face = faces[i];
//            PointF mp = new PointF();
//            face.getMidPoint(mp);
//            float eyeDistance = face.eyesDistance();
//            if (eyeDistance >= maxEyeDistance) {
//                maxEyeDistance = eyeDistance;
//                maxFace = face;
//            }
//        }
        maxFace = getNearestCenterFace(numberOfDetectedFace, faces, bitmap.getWidth());
        if (maxFace != null) {
            PointF mp = new PointF();
            maxFace.getMidPoint(mp);
            mp.x *= scaleSpeed;
            mp.y *= scaleSpeed;
            float eyeDistance = maxFace.eyesDistance() * scaleSpeed;
//            float eyeDistance = maxFace.eyesDistance();
            int left = (int) (mp.x - 2 * eyeDistance);
            int top = (int) (mp.y - 2 * eyeDistance);
            int right = (int) (mp.x + 2 * eyeDistance);
            int bottom = (int) (mp.y + 2 * eyeDistance);
            left = Math.max(left, 0);
            top = Math.max(top, 0);
            right = Math.min(right, srcBitmap.getWidth());
            bottom = Math.min(bottom, srcBitmap.getHeight());
            return Bitmap.createBitmap(srcBitmap, left, top, right - left, bottom - top);
        }
        return null;
    }

    private long startTime = 0;
    private float scaleSpeed = 2.0f; // recommend 2.2 ，值越小速度越慢，识别准确度越高，反之亦然。

    /**
     * 裁切人体
     *
     * @param srcBitmap
     * @return
     */
    public Bitmap clipBody(Context context, Bitmap srcBitmap) {
        startTime = System.currentTimeMillis();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        try {
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtil.d("time used", "time used : " + (System.currentTimeMillis() - startTime));
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() / scaleSpeed), (int) (bitmap.getHeight() / scaleSpeed), true);
        faceDetector = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), MAX_FACE_COUNT);
        FaceDetector.Face[] faces = new FaceDetector.Face[MAX_FACE_COUNT];
        int numberOfDetectedFace = faceDetector.findFaces(bitmap, faces);
        //        Log.e("face", "facenum=" + numberOfDetectedFace);
        //        float maxEyeDistance = 0;
        FaceDetector.Face maxFace = null;
        //        for (int i = 0; i < numberOfDetectedFace; i++) {
        //            FaceDetector.Face face = faces[i];
        //            PointF mp = new PointF();
        //            face.getMidPoint(mp);
        //            float eyeDistance = face.eyesDistance();
        //            if (eyeDistance >= maxEyeDistance) {
        //                maxEyeDistance = eyeDistance;
        //                maxFace = face;
        //            }
        //        }
        maxFace = getNearestCenterFace(numberOfDetectedFace, faces, bitmap.getWidth());
        LogUtil.d("time used", "time used : " + (System.currentTimeMillis() - startTime));
        if (maxFace != null) {
            PointF mp = new PointF();
            maxFace.getMidPoint(mp);
            mp.x *= scaleSpeed;
            mp.y *= scaleSpeed;
            float eyeDistance = maxFace.eyesDistance() * scaleSpeed;
            // ---- 获取人脸 -----
            int left = (int) (mp.x - 2.2 * eyeDistance);
            int top = (int) (mp.y - 2.2 * eyeDistance);
            int right = (int) (mp.x + 2.2 * eyeDistance);
            int bottom = (int) (mp.y + 2.2 * eyeDistance);
            left = Math.max(left, 0);
            top = Math.max(top, 0);
            right = Math.min(right, srcBitmap.getWidth());
            bottom = Math.min(bottom, srcBitmap.getHeight());
            CommonData.faceBitmap = Bitmap.createBitmap(srcBitmap, left, top, right - left, bottom - top);
            LogUtil.d("time used", "time used : " + (System.currentTimeMillis() - startTime));
            // 人体判断
            left = (int) (mp.x - 6.2 * eyeDistance);
            top = (int) (mp.y - 2.77 * eyeDistance);
            right = (int) (mp.x + 6.2 * eyeDistance);
            bottom = (int) (mp.y + 2 * eyeDistance);
            top = Math.max(top, 0);

            //            if (srcBitmap.getHeight() - bottom < 2.7f * (bottom - top)) {
            //                CommonData.isFullBody = false;
            ////                LogUtil.d("body", "!非全身");
            //                return null;
            //            } else {
            ////                LogUtil.d("body", "!全身");
            //                CommonData.isFullBody = true;
            //            }
            CommonData.isFullBody = true;

            LogUtil.d("time used", "time used : " + (System.currentTimeMillis() - startTime));
            // body edge
            left = Math.max(left, 0);
            // top = Math.max(top, 0);
            right = Math.min(right, srcBitmap.getWidth());

//            if (GlobalConfig.IS_NEW_CAMERA) {
                bottom = Math.min((int) ((bottom - top) * 12.5f), srcBitmap.getHeight());
//            } else {
//                bottom = Math.min((int) ((bottom - top) * 8.0f), srcBitmap.getHeight());
//            }

            int tempWidth = (int) ((bottom - top) * 9f / 16);
            //            left = (srcBitmap.getWidth() - tempWidth)/2;
            left = (int) (mp.x - tempWidth / 2f);
            left = Math.max(0, left);
            right = left + tempWidth;
            // 比例为16%9的人体裁切图
//            Bitmap srcBodyBitmap = Bitmap.createBitmap(srcBitmap, left, top, Math.min(srcBitmap.getWidth(), right - left), bottom - top);
            Bitmap srcBodyBitmap = Bitmap.createBitmap(srcBitmap, left, top, Math.min(srcBitmap.getWidth() - left, right - left), bottom - top);
            int dstWidth = right - left;
            int dstHeight = (int) (dstWidth * (1920f / 1080));
            CommonData.subDx = left;
            CommonData.subDY = top;

            int screenWidth = AppUtil.getScreenWH(context)[0];
            int screenHeight = AppUtil.getScreenWH(context)[1];
            LogUtil.d("screenSize:", screenWidth + "," + screenHeight);

            float scaleRate = 1;
            //            if (bottom - top > dstHeight) {
            //                scaleRate = dstHeight*1.0f/(bottom - top);
            //                srcBodyBitmap = Bitmap.createScaledBitmap(srcBodyBitmap, (int) (dstWidth * scaleRate), dstHeight, true);
            //            }
            CommonData.bodyScaleRate = scaleRate;
            LogUtil.d("time used", "time used : " + (System.currentTimeMillis() - startTime));
            //            Bitmap dstBodyBitmap = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ARGB_8888);
            //            Canvas canvas = new Canvas(dstBodyBitmap);
            //            Paint paint = new Paint();
            ////            canvas.drawColor(Color.BLACK);
            //            float drawX = (dstBodyBitmap.getWidth() - srcBodyBitmap.getWidth())/2;
            //            float drawY = (dstBodyBitmap.getHeight() - srcBodyBitmap.getHeight())/2;
            //            canvas.drawBitmap(srcBodyBitmap, drawX, drawY, paint);
            //            CommonData.addDx = drawX;
            //            CommonData.addDy = drawY;
            CommonData.finalBodyScaleRate = 1080f / dstWidth;
            //            return dstBodyBitmap;
            return srcBodyBitmap;
        } else {
            CommonData.faceBitmap = null;
        }
        return null;
    }

    private FaceDetector.Face getNearestCenterFace(int numOfFace, FaceDetector.Face[] faces, int bitmapWidth) {
        if (numOfFace == 0) {
            return null;
        }
        if (numOfFace == 1) {
            return faces[0];
        }
        int nearestIndex = 0;
        float currDistance = Integer.MAX_VALUE;
        for (int i = 0; i < numOfFace; i++) {
            FaceDetector.Face face = faces[i];
            PointF mp = new PointF();
            face.getMidPoint(mp);
            float distance = Math.abs(mp.x - bitmapWidth / 2f);
            if (distance < currDistance) {
                currDistance = distance;
                nearestIndex = i;
            }
        }
        return faces[nearestIndex];
    }

    /**
     * 获取照片中所有人脸
     *
     * @param srcBitmap 照片原图
     * @return
     */
    public List<Bitmap> getAllFaces(Bitmap srcBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        try {
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Bitmap> faceList = new ArrayList<>();
        faceDetector = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), MAX_FACE_COUNT);
        FaceDetector.Face[] faces = new FaceDetector.Face[MAX_FACE_COUNT];
        int numberOfDetectedFace = faceDetector.findFaces(bitmap, faces);
        Log.d("face", "facenum=" + numberOfDetectedFace);
        for (int i = 0; i < numberOfDetectedFace; i++) {
            FaceDetector.Face face = faces[i];
            PointF mp = new PointF();
            face.getMidPoint(mp);
            float eyeDistance = face.eyesDistance();
            int left = (int) (mp.x - 2 * eyeDistance);
            int top = (int) (mp.y - 2 * eyeDistance);
            int right = (int) (mp.x + 2 * eyeDistance);
            int bottom = (int) (mp.y + 2 * eyeDistance);
            left = Math.max(left, 0);
            top = Math.max(top, 0);
            right = Math.min(right, srcBitmap.getWidth());
            bottom = Math.min(bottom, srcBitmap.getHeight());
            Bitmap faceBitmap = Bitmap.createBitmap(srcBitmap, left, top, right - left, bottom - top);
            faceList.add(faceBitmap);
        }
        return faceList;
    } // getAllFaces()


    /**
     * TODO
     * 获得所有人体和人脸的对应信息
     *
     * @param srcBitmap
     * @return
     */
    public Bitmap clipAllBodys(Bitmap srcBitmap) {
        startTime = System.currentTimeMillis();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        try {
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtil.d("time used", "time used : " + (System.currentTimeMillis() - startTime));
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() / scaleSpeed), (int) (bitmap.getHeight() / scaleSpeed), true);
        faceDetector = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), MAX_FACE_COUNT);
        FaceDetector.Face[] faces = new FaceDetector.Face[MAX_FACE_COUNT];
        int numberOfDetectedFace = faceDetector.findFaces(bitmap, faces);
        //        Log.e("face", "facenum=" + numberOfDetectedFace);
        //        float maxEyeDistance = 0;
        FaceDetector.Face maxFace = null;
        maxFace = getNearestCenterFace(numberOfDetectedFace, faces, bitmap.getWidth());
        LogUtil.d("time used", "time used : " + (System.currentTimeMillis() - startTime));
        if (maxFace != null) {
            PointF mp = new PointF();
            maxFace.getMidPoint(mp);
            mp.x *= scaleSpeed;
            mp.y *= scaleSpeed;
            float eyeDistance = maxFace.eyesDistance() * scaleSpeed;
            // ---- 获取人脸 -----
            int left = (int) (mp.x - 2 * eyeDistance);
            int top = (int) (mp.y - 2 * eyeDistance);
            int right = (int) (mp.x + 2 * eyeDistance);
            int bottom = (int) (mp.y + 2 * eyeDistance);
            left = Math.max(left, 0);
            top = Math.max(top, 0);
            right = Math.min(right, srcBitmap.getWidth());
            bottom = Math.min(bottom, srcBitmap.getHeight());
            CommonData.faceBitmap = Bitmap.createBitmap(srcBitmap, left, top, right - left, bottom - top);
            LogUtil.d("time used", "time used : " + (System.currentTimeMillis() - startTime));
            // 人体判断
            left = (int) (mp.x - 7 * eyeDistance);
            top = (int) (mp.y - 2.2 * eyeDistance);
            right = (int) (mp.x + 7 * eyeDistance);
            bottom = (int) (mp.y + 2 * eyeDistance);
            top = Math.max(top, 0);

            if (srcBitmap.getHeight() - bottom < 2.7f * (bottom - top)) {
                CommonData.isFullBody = false;
                //                LogUtil.d("body", "!非全身");
                return null;
            } else {
                //                LogUtil.d("body", "!全身");
                CommonData.isFullBody = true;
            }
            LogUtil.d("time used", "time used : " + (System.currentTimeMillis() - startTime));
            // body edge
            left = Math.max(left, 0);
            //            top = Math.max(top, 0);
            right = Math.min(right, srcBitmap.getWidth());
            bottom = Math.min((int) ((bottom - top) * 7.9f), srcBitmap.getHeight());

            int tempWidth = (int) ((bottom - top) * 9f / 16);
            left = (srcBitmap.getWidth() - tempWidth) / 2;
            right = left + tempWidth;
            // 比例为16%9的人体裁切图
            Bitmap srcBodyBitmap = Bitmap.createBitmap(srcBitmap, left, top, right - left, bottom - top);
            int dstWidth = right - left;
            int dstHeight = (int) (dstWidth * (1920f / 1080));
            CommonData.subDx = left;
            CommonData.subDY = top;

            float scaleRate = 1;
            if (bottom - top > dstHeight) {
                scaleRate = dstHeight * 1.0f / (bottom - top);
                srcBodyBitmap = Bitmap.createScaledBitmap(srcBodyBitmap, (int) (dstWidth * scaleRate), dstHeight, true);
            }
            CommonData.bodyScaleRate = scaleRate;
            LogUtil.d("time used", "time used : " + (System.currentTimeMillis() - startTime));
            //            Bitmap dstBodyBitmap = Bitmap.createBitmap(dstWidth, dstHeight, Bitmap.Config.ARGB_8888);
            //            Canvas canvas = new Canvas(dstBodyBitmap);
            //            Paint paint = new Paint();
            ////            canvas.drawColor(Color.BLACK);
            //            float drawX = (dstBodyBitmap.getWidth() - srcBodyBitmap.getWidth())/2;
            //            float drawY = (dstBodyBitmap.getHeight() - srcBodyBitmap.getHeight())/2;
            //            canvas.drawBitmap(srcBodyBitmap, drawX, drawY, paint);
            //            CommonData.addDx = drawX;
            //            CommonData.addDy = drawY;
            CommonData.finalBodyScaleRate = 1080f / dstWidth;
            //            return dstBodyBitmap;
            return srcBodyBitmap;
        }
        return null;
    }


}
