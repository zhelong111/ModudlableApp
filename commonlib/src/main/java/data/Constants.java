package data;

import android.os.Environment;

/**
 * Created by Yuaihen on 2017/12/7.
 * 常量类
 */

public class Constants {


    private Constants() {
    }

    public static final String VERSION = "version";
    public static final String MAC = "mac";
    public static final String TOKEN = "token";
    public static final String USER_ID = "userId";
    public static final String CONSUMER_ID = "consumerId";
    public static final String ID = "id";
    public static final String IDS = "ids";
    public static final String TEST_CONSUMER_ID = "testConsumerId";
    public static final String USER_FLAG = "userFlag";   //1:新用户 2:老用户
    public static final String STYLE_ID = "styleId";    //用户风格
    public static final String USE_NO = "useNo"; // 使用次数
    public static final String QR_CODE_URL = "qrcode";
    public static final String PAY_QR_CODE_URL = "pay_qrcode";//付款码
    public static final String SEARCH_IMAGE_FOR_IMAGE = "searchImageForImage";
    public static final String SHOP_BUY_CONFIG_INFO = "shopBuyConfigInfo";
    public static final String SHOP_MALE_CONFIG_INFO = "shopMaleConfigInfo";
    public static final String ZQ_SUPPORT = "zq_support";
    public static String CLIP_BITMAP = "clipedBitmap";//拍照完成裁剪后的bitmap
    public static String USER_PHOTO = "userPhoto";//拍照完成裁剪后的bitmap
    public static String FACE_PHOTO = "faceImg";//未经美颜的脸部原图
    public static String USER_SIZE = "userSize";//用户体型size
    public static final String BODY_LINE_PARAM = "bodyLineParam";   //身线图信息保存(节点 参数)
    public static final String VIDEO_CACHE_DIRECTORY = Environment.getExternalStorageDirectory().toString() + "/YiMirror/VideoCache/";
    public static final String LOG_CATCH_DIRECTORY = Environment.getExternalStorageDirectory().toString() + "/YiMirror/CrashReport/log/";
    public static final String TO_DO_FLAG = "to_do_flag";
    public static final String PHOTO_NO = "photoNo"; // 扫脸照片ID
    public static final String LOGIN_PHOTO_NO = "loginPhotoNo"; // 扫脸照片ID
    public static final String MEM_PHOTO_NO = "memPhotoNo"; // 试穿留影照片ID
    public static final String GENDER = "gender";
    public static final String SECOND_GENDER = "secondGender";
    public static final String VIEW_CODE = "viewCode";
    public static final String FACE_STR = "faceStr";
    public static final String OCCASIONIDS = "occasionIds";
    public static final String OCCASION_ID = "occasionId";
    public static String COLOR_LABEL_IDS = "labelIds";   //Color标签名称
    public static final String CLOTHING_CODE = "clothingCode";
    public static final String CLOTHING_CODE_STR = "clothingCodeStr";
    public static final String TODAY_HAS_USE = "todayHasUse";
    public static final String CURRENT_PAGE = "currentPage";
    //    红包活动
    public static final String RED_PACKET_SUPPORT = "redPacketSupport";
    public static final String RED_PACKET_NUM_FLAG = "redPacketNumFlag";
    public static final String RED_PACKET_YET_RECEIVE = "redPacketYetReceive";
    public static final String RED_PACKET_QR_CODE = "redPacketQrCode";
    public static final String RED_PACKET_AMOUNT = "redPacketAmount";
    public static final int SUPPORT_RED_PACKET = 1;
    public static final int RED_PACKET_NOT_EMPTY = 2;
    public static final String BACK_TO_BANNER_COUNT = "back_to_banner_count";
    public static final String BRAND_NAME = "brand_name";
    public static final String WELCOME = "welcome";
    public static final String DATA = "data";
    public static final String IS_OPEN_QRCODE = "isOpenQrcode";
    public static final String IS_TEST = "isTest";
    public static final int WOMAN = 2;
    public static final int MAN = 1;
    public static final String LABEL_NAME = "labelName";
    public static final String MINI_PROGREAM = "miniProgram";
    public static final String WEAR_ROOM_QRCODE = "wear_room_qrcode";
    public static final String COLOR = "color";
    public static final String SIZE = "size";
    public static final String URL = "url";
    public static final String WEIGHT = "weight";
    public static final String HEIGHT = "height";
    public static int DETAIL_FROM_WHERE = -1;//从哪个页面跳转到的详情
    public static final int FROM_OCCASION = 1;
    public static final int FROM_LABEL = 2;
    public static final int FROM_COLOR_REPORT = 3;
    public static final int FROM_VIEW_MORE = 5;
    public static int WEAR_ROOM_NUMBER = 0;
    public static int FROM_RESTART_OR_BODY_LINE = 0;//是点击的重新拍照还是测身线
    public static final int IMAGE_RESTART_TEST = 0; //重新拍照进行形象报告评测
    public static final int IMAGE_BODY_LINE_TEST = 1;//测身线
    public static final String FLAG = "flag";//是否重新测评 0：否 1：是
    public static final String SKIN_TEST = "checkSkin";//需要做肤质检测
    public static final String VERSIONS = "versions";//3代表展览版本
    public static final int IS_RESTART_TEST = 1;//重新评测形象报告
    public static final int NOT_RESTART_TEST = 0;//不是重新评测，只获取身线数据
    public static final int GET_REPORT_FAIL = 1;
    public static final int GET_REPORT_SUCCESS = 2;
    public static final String TO_IMAGE_REPORT = "toImageReport";//UserGuideView跳转形象报告
    public static final int TO_IMAGE_REPORT_FLAG = 10101;
    public static final String RECOMMEND_AMOUNT = "recommendAmount";
    public static final String DAILY_RECOMMEND = "每日推荐";
    public static final String SMART_RECOMMEND = "智能精选";
    public static final String OCCASION_RECOMMEND = "场合推荐";
    public static final String SETTING = "系统设置";
    public static final String SHOPPING_CART = "购物袋";
    public static final String APP_CACHE = "appCache";
    public static final String DATA_STR = "dateStr";//历史报告日期
    public static final String SEARCH_LIST = "searche_list";//以图搜图的List
    public static final String TITLE = "title";
    public static final int HISTORY_REQUEST_CODE = 1010;//主页跳转形象报告历史记录Request Code
    public static final String HISTORY_RETURN_DATA = "reportDataStr";//形象报告历史记录返回DataStr
    public static final String REPORT_RESTART_DATA = "report_restart";//重测形象报告
    public static final int REPORT_RESTART_REQUEST_CODE = 1011;//重测形象报告Request Code
    public static final String IMAGE_TEST_FROM = "from";//拍照页面区分是测身线还是重新评测
    public static final String CODE = "code";//商品详情页面需要的参数
    public static final String TYPE = "type";//商品详情页面需要的参数
    public static final String COLLOCATION_ID = "collocationId";//商品详情页面需要的参数
    public static final String PRICE = "price";//商品详情页面需要的参数
    public static final String DAYS = "days";//获取当前日
    public static final String SHOPPING_CART_NUM = "shoppingCartNum";//购物车内数量
    public static final String ALREADY_SHOW_TIP = "alreadyShowTip";//是否已经显示过提示
    public static final String SHOP_SUPPORT_PAY = "shopSupportPay";//店铺是否支持支付
    public static final String POSITION = "position";
    public static final int NO_DATA_SWITCH_TIME = 6000;//智能精选无数据切换到形象报告时间
    public static final String USER_INFO = "userInfo";
    public static final long BODY_SAMPLE_TIME = 4000L;//身线拍照示例3D图停留时间
    public static boolean ALREADY_SHOW_EMPTY = false;//智能精选已经显示过退出按钮
    public static boolean ALREADY_SHOW_EDIT_TIP = false;//编辑廓形已经显示过tip
    public static final int BANNER_MODE = 0;//轮播图模式
    public static final int VIDEO_MODE = 1;//视频轮播模式
    public static final int LOCK_MODE = 2;//锁屏模式
    public static final int EXTERNAL_CAMERA = 1; //有外接摄像头
    public static final int NOT_EXTERNAL_CAMERA = 0; //没有外接摄像头
    public static final int VIDEO_TIP_TIME = 20 * 1000;//视频提示页面停留时间
    public static final long STAND_BY_TIME = 5 * 60 * 1000;//首页每5分钟轮播一次待机音频
    public static final long SCAN_END_TIME = 2 * 1000;//扫完脸之后显示提示停留时长 2秒

}
