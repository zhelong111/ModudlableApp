package network.httpclient.constants;

import util.AppUtil;

public class NetConstants {

    //            public static String BASE_URL = "http://10.1.21.245:9008/";// 张鹏程
    //    public static String BASE_URL = "http://10.1.21.247:9008/";   //伍巍巍
    //    public static String BASE_URL = "http://10.1.30.243:9008/"; // 陈锦发
    //    public static String BASE_URL = "http://10.1.20.170:9008/";//黄军霞
          public static String BASE_URL = "http://10.1.30.6:9008/"; // ToB测试
    //      public static String BASE_URL = "http://apitob.xxlimageim.com/"; // ToB线上
//        public static String BASE_URL = "http://api.prev.xxlimageim.com/";//ToB预发布

    public static void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    static {
        if (AppUtil.isRelease()) {
            BASE_URL = "http://apitob.xxlimageim.com/";
        }
    }

    /**
     * 实际URL = BASE_URL + URL.xxx
     */
    public interface URL {

        /**
         * 获取Toke
         **/
        String TOKEN = "api/getToken";

        /**
         * 根据photoNo查询扫脸照片分析结果
         */
        String GET_ANALYSIS_PHOTO_RESULT = "api/getConsumerResult";

        /**
         * 新的上传扫脸的照片
         */
        String UPLOAD_SCAN_PHOTO = "api/bUploadPhoto";

        String UPLOAD_SCAN_PHOTO_NEW = "api/bUploadPhotoNew";

        /**
         * 外接摄像头上传照片
         */
        String UPLOAD_SCAN_PHOTO_EXTERNAL_CAMERA = "api/queryUserRecord";

        /**
         * 推荐的服装总数量
         */
        String GET_CLOTHING_COUNT = "api/getRecommendedAmount";
        /**
         * 获取小程序二维码
         */
        String GET_QRCODE = "api/getMiniProgramUrl";

        /**
         * 获取雷达图八种描述文字
         */
        String GET_RADAR_DESC = "api/getShopDefineStyleList";

        /**
         * 页面停留时间统计
         */
        String REPORT_PAGE_STOP_TIME = "api/reportPageStop";

        /**
         * 事件统计
         */
        String REPORT_EVENT = "api/reportEvent";

        /**
         * 事件统计上传数据
         */
        String REPOER_EVENT_STR = "api/reportEventTime";

        /**
         * 上报app信息
         */
        String UPLOAD_APP_INFO = "api/reportAppVersion";

        /**
         * 获取店铺配置信息
         * 是否显示购买区域
         */
        String GET_SHOP_CONFIG = "api/getShopConfigInfo";

        /**
         * 获取广告轮播图
         */
        String GET_AD_INFO = "api/getAdInfoNew";

        /**
         * 获取时尚趋势某个主题下面的图片列表
         */
        String GET_THEME_ITEMS = "api/getRecommendContentList";

        /**
         * 查询红包二维码扫描状态
         */
        String SCAN_REDPKG_STATUS = "api/getConsumerScanQrCodeStatus";

        /**
         * 获取更多
         */
        String GET_MORE_OUT = "api/findOutMore";

        /**
         * 智能推荐
         */
        String GET_RECOMMEND_DATA = "api/intelligentRecommendation";

        /**
         * 形象评测
         */
        String GET_IMAGE_REPORT = "api/imageEvaluationNew";

        /**
         * 打开试衣间
         */
        String GET_WEAR_ROOM = "api/fittingRoomList";

        /**
         * 加入试衣间
         */
        String ADD_WEAR_ROOM = "api/addFitting";

        /**
         * 获取商品详情
         */
        String GET_GOODS_DETAIL = "api/commodityDetailsNew";

        /**
         * 获取分类标签
         */
        String GET_LABEL = "api/tagSelection";

        /**
         * 提交测试报告
         */
        //        String UPLOAD_TEST_REPORT = "api/addTestReportInfoNew";
        String UPLOAD_TEST_REPORT = "api/addTestReport";

        /**
         * 删除试衣间商品
         */
        String DELETE_WEAR_ROOM_GOODS = "api/delfittingData";

        /**
         * 历史点击照片列表
         */
        String GET_HISTORY_CLICK_CLOTH = "api/clothingBrowsingRecord";

        /**
         * 获取留影照片列表
         */
        String GET_MEM_PHOTO_LIST = "api/getImagePhotoListNew";

        /**
         * 查询去掉背景的图片
         */
        String GET_REMOVE_BG_PHOTO = "api/getBodySplitStatus";

        /**
         * 上传去掉图片背景合成后的图片
         */
        String UPLOAD_MIX_PHOTO = "api/uploadBodyComposeImage";

        /**
         * 上传形象日记照片
         */
        String UPLOAD_IMAGE_PHOTO = "api/uploadImagePhoto";

        /**
         * 删除形象日记照片
         */
        String DELETE_IMAGE_PHOTO = "api/deleteImagePhoto";

        /**
         * 获取形象报告
         */
        String GET_IMAGE_REPORT_NEW = "api/testReportNew";

        /**
         * 获取身线数据
         */
        String GET_BODY_LINE_DATA = "api/bodyLineAnalysisNew";

        /**
         * 获取推荐服装
         */
        String GET_RECOMMEND_WITH_BODY = "api/recommendCommodities";

        /**
         * 穿搭指南获取推荐服装
         */
        String GET_DRESS_GUIDE_CLOTH = "api/wearGuideNew";

        /**
         * 智能精选
         */
        String GET_INTELLIGENT_CHOOSE = "api/smartChoice";

        /**
         * 以图搜图
         */
        String SEARCH_BY_IMAGE = "api/searchByImage";

        /**
         * 根据场合ID获取该场合下数据
         */
        String GET_OCCASION_LIST = "api/getOccasionList";


    }
}
