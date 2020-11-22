package util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bruce on 2017/8/7.
 */

public class SoundUtil {
    private static MediaPlayer mediaPlayer;
    private static SoundPool soundPool;

    public static int SOUNDID_001;//身线测试页面中点击【开始】，
    public static int SOUNDID_002;//输入身高体重
    public static int SOUNDID_003;//语音提示：3 2 1
    public static int SOUNDID_004;
    public static int SOUNDID_005;
    public static int SOUNDID_006;//可以编辑身线
    public static int SOUNDID_007;//拍摄正面照
    public static int SOUNDID_008;//拍摄侧面照
    public static int SOUNDID_009;//拍摄成功
    public static int SOUNDID_010;//调整姿势
    public static int SOUNDID_011;//咔嚓

    public static int STREAMID_001;
    public static int currStreamId;
    private static List<Integer> soundIdList;

    public static void loadAll(Context context) {
        if (soundPool != null) {
            return;
        }
        soundPool = new SoundPool(12, AudioManager.STREAM_MUSIC, 1);
        soundIdList = new ArrayList<>();
        //        SOUNDID_001 = soundPool.load(context, R.raw.b_wait_some_mins, 1);
        //        SOUNDID_002 = soundPool.load(context, R.raw.b_input_some_content, 1);
        //        SOUNDID_003 = soundPool.load(context, R.raw.b_three, 1);
        //        SOUNDID_004 = soundPool.load(context, R.raw.b_two, 1);
        //        SOUNDID_005 = soundPool.load(context, R.raw.b_one, 1);
        //        SOUNDID_006 = soundPool.load(context, R.raw.b_can_drag_line, 1);
        //        SOUNDID_007 = soundPool.load(context, R.raw.b_take_front_pic, 1);
        //        SOUNDID_008 = soundPool.load(context, R.raw.b_take_side_pic, 1);
        //        SOUNDID_009 = soundPool.load(context, R.raw.b_take_pic_success, 1);
        //        SOUNDID_010 = soundPool.load(context, R.raw.b_wrong_pose, 1);
        //        SOUNDID_011 = soundPool.load(context, R.raw.shutter, 1);
    }


    //    public static int play(int audioResId) {
    //        int soundId = 0;
    //        switch (audioResId) {
    //            case R.raw.b_wait_some_mins:
    //                soundId = SOUNDID_001;
    //                break;
    //            case R.raw.b_input_some_content:
    //                soundId = SOUNDID_002;
    //                break;
    //            case R.raw.b_one:
    //                soundId = SOUNDID_003;
    //                break;
    //            case R.raw.b_two:
    //                soundId = SOUNDID_004;
    //                break;
    //            case R.raw.b_three:
    //                soundId = SOUNDID_005;
    //                break;
    //            case R.raw.b_can_drag_line:
    //                soundId = SOUNDID_006;
    //                break;
    //            case R.raw.b_take_front_pic:
    //                soundId = SOUNDID_007;
    //                break;
    //            case R.raw.b_take_side_pic:
    //                soundId = SOUNDID_008;
    //                break;
    //            case R.raw.b_take_pic_success:
    //                soundId = SOUNDID_009;
    //                break;
    //            case R.raw.b_wrong_pose:
    //                soundId = SOUNDID_010;
    //                break;
    //            case R.raw.shutter:
    //                soundId = SOUNDID_011;
    //                break;
    //            default:
    //        }
    //        currStreamId = soundPool.play(soundId, 1, 1, 0, 0, 1);
    //        return currStreamId;
    //    }



    public static void stop(int streamId) {
        if (soundPool != null) {
            soundPool.stop(streamId);
        }
    }

    public static void stop() {
        if (soundPool != null) {
            soundPool.stop(currStreamId);
        }
    }

    public static void stopAllSound() {
        if (soundIdList != null) {
            for (int id : soundIdList) {
                stop(id);
            }
            soundIdList.clear();
        }
    }

    public static void destroy() {
        if (soundPool != null) {
            soundPool.release();
        }
        soundIdList.clear();
    }

    public static void playAsyc(Context context, int audioResId, boolean loop) {
        final int soundId = soundPool.load(context, audioResId, 1);
        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
                    currStreamId = soundPool.play(soundId, 1, 1, 0, loop ? 1 : 0, 1);
                    soundIdList.add(currStreamId);
                }
        );
    }

    //    public static void playUrl(String url) {
    //        MediaPlayer mediaPlayer = new MediaPlayer();
    //        try {
    //            mediaPlayer.setDataSource(url);
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //        mediaPlayer.prepareAsync();
    //        mediaPlayer.setOnPreparedListener(MediaPlayer::start);
    //    }

    /**
     * 播放指定文件
     */
    public static void playResId(Context context, int resId) {
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                Log.d("SoundUtil", e.toString());
            }
        }

        mediaPlayer = MediaPlayer.create(context, resId);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(false);
            }
        });
        //        mediaPlayer.setLooping(false);
        mediaPlayer.setOnCompletionListener(MediaPlayer::stop);
        mediaPlayer.start();
    }


    public static void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }
    }

    public static void stopMediaPlayer() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

}
