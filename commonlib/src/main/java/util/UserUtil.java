package util;


import data.Constants;

/**
 * Created by Yuaihen on 2017/12/7.
 */

public class UserUtil {

    //设置Token
    public static void setToken(String value) {
        CacheUtils.setString(Constants.TOKEN, value);
    }

    public static String getToken() {
        return CacheUtils.getString(Constants.TOKEN);
    }

    //设置用户Id
    public static void setUserId(String userId) {
        CacheUtils.setString(Constants.USER_ID, userId);
    }

    public static String getUserId() {
        return CacheUtils.getString(Constants.USER_ID);
    }

    //设置用户风格
    public static void setStyleId(int value) {
        CacheUtils.setInt(Constants.STYLE_ID, value);
    }

    public static int getStyleId() {
        return CacheUtils.getInt(Constants.STYLE_ID, -1);
    }

    //设置新老用户标签 (flag=1新用户，flag=2老用户)
    public static void setUserFlag(int value) {
        CacheUtils.setInt(Constants.USER_FLAG, value);
    }

    public static int getUserFlag() {
        return CacheUtils.getInt(Constants.USER_FLAG, -1);
    }

    public static void setQrCodeUrl(String qrcodePath) {
        CacheUtils.setString(Constants.QR_CODE_URL, qrcodePath);
    }

    public static String getQrCodeUrl() {
        return CacheUtils.getString(Constants.QR_CODE_URL);
    }

    public static void setPayQrCodeUrl(String qrcodePath) {
        CacheUtils.setString(Constants.PAY_QR_CODE_URL, qrcodePath);
    }

    public static String getPayQrCodeUrl() {
        return CacheUtils.getString(Constants.PAY_QR_CODE_URL);
    }

    public static void setBeanByGson(String key, Object obj) {
        CacheUtils.setBean(key, obj);
    }

    public static <T> T getBeanByGson(String key, Class<T> clazz) {
        return CacheUtils.getBean(key, clazz);
    }

    public static void setImageForBase64(String key, String imageUrl) {
        CacheUtils.setString(key, imageUrl);
    }

    public static void setPhotoNo(String photoNo) {
        CacheUtils.setString(Constants.PHOTO_NO, photoNo);
    }

    public static String getPhotoNo() {
        return CacheUtils.getString(Constants.PHOTO_NO);
    }

    public static void setLoginPhotoNo(String photoNo) {
        CacheUtils.setString(Constants.LOGIN_PHOTO_NO, photoNo);
    }

    public static String getLoginPhotoNo() {
        return CacheUtils.getString(Constants.LOGIN_PHOTO_NO);
    }

    public static void setMemPhotoNo(String photoNo) {
        CacheUtils.setString(Constants.MEM_PHOTO_NO, photoNo);
    }

    public static String getMemPhotoNo() {
        return CacheUtils.getString(Constants.MEM_PHOTO_NO);
    }

    public static void setGender(int gender) {
        CacheUtils.setInt(Constants.GENDER, gender);
    }

    public static int getGender() {
        return CacheUtils.getInt(Constants.GENDER);
    }

    /**
     * 身线测试用的性别
     */
    public static void setSecondGender(int gender) {
        CacheUtils.setInt(Constants.SECOND_GENDER, gender);
    }

    public static int getSecondGender() {
        return CacheUtils.getInt(Constants.SECOND_GENDER);
    }

    /**
     * 店铺是否支持购买
     * 1支持 2不支持
     */
    public static void setShopBuyConfigInfo(int buyConfig) {
        CacheUtils.setInt(Constants.SHOP_BUY_CONFIG_INFO, buyConfig);
    }

    public static int getShopBuyConfigInfo() {
        return CacheUtils.getInt(Constants.SHOP_BUY_CONFIG_INFO);
    }

    /**
     * 是否支持店铺识别男性
     * 1.支持 2.不支持
     */
    public static void setShopMaleSupport(int maleSupport) {
        CacheUtils.setInt(Constants.SHOP_MALE_CONFIG_INFO, maleSupport);
    }

    public static int getShopMaleSupport() {
        return CacheUtils.getInt(Constants.SHOP_MALE_CONFIG_INFO);
    }

    /**
     * 是否支持中秋活动
     */
    public static void setZqSupport(int zqSupport) {
        CacheUtils.setInt(Constants.ZQ_SUPPORT, zqSupport);
    }

    public static int getZpSupport() {
        return CacheUtils.getInt(Constants.ZQ_SUPPORT, -1);
    }


    /**
     * 用户是否是当天第一次进入APP
     *
     * @param flag 1是未进入  0进入过
     */
    public static void setTodayHasUse(int flag) {
        CacheUtils.setInt(Constants.TODAY_HAS_USE, flag);
    }

    public static int getTodayHasUse() {
        return CacheUtils.getInt(Constants.TODAY_HAS_USE, 1);
    }


    /**
     * 是否支持春节红包活动
     */
    public static void setRedPacketSupport(int redPacketSupport) {
        CacheUtils.setInt(Constants.RED_PACKET_SUPPORT, redPacketSupport);
    }

    public static int getRedPacketSupport() {
        return CacheUtils.getInt(Constants.RED_PACKET_SUPPORT, -1);
    }

    /**
     * 红包是否达到上限
     */
    public static void setRedPacketNumFlag(int redPacketNumFlag) {
        CacheUtils.setInt(Constants.RED_PACKET_NUM_FLAG, redPacketNumFlag);
    }

    public static int getRedPacketNumFlag() {
        return CacheUtils.getInt(Constants.RED_PACKET_NUM_FLAG);
    }

    /**
     * 是否已经领取过红包
     */
    public static void setReceiveRedPacketFlag(int receiveRedPacketFlag) {
        CacheUtils.setInt(Constants.RED_PACKET_YET_RECEIVE, receiveRedPacketFlag);
    }

    public static int getReceiveRedPacketFlag() {
        return CacheUtils.getInt(Constants.RED_PACKET_YET_RECEIVE, -1);
    }

    /**
     * 二维码
     */
    public static void setRedPacketQrCode(String qrCode) {
        CacheUtils.setString(Constants.RED_PACKET_QR_CODE, qrCode);
    }

    public static String getRedPacketQrCode() {
        return CacheUtils.getString(Constants.RED_PACKET_QR_CODE);
    }

    /**
     * 设置红包金额
     */
    public static void setRedPacketAmount(float redPacketAmount) {
        CacheUtils.setFloat(Constants.RED_PACKET_AMOUNT, redPacketAmount);
    }

    public static float getRedPacketAmount() {
        return CacheUtils.getFloat(Constants.RED_PACKET_AMOUNT, 0.0f);
    }

    /**
     * 设置品牌名
     */
    public static void setBrandName(String brandName) {
        CacheUtils.setString(Constants.BRAND_NAME, brandName);
    }

    public static String getBrandName() {
        return CacheUtils.getString(Constants.BRAND_NAME);
    }

    /**
     * 设置欢迎语句
     */
    public static void setWelcome(String welcome) {
        CacheUtils.setString(Constants.WELCOME, welcome);
    }

    public static String getWelcome() {
        return CacheUtils.getString(Constants.WELCOME);
    }

    /**
     * 是否显示形象报告里面的二维码
     * 是否开启分享二维码 0关闭 1开启
     */
    public static void setIsOpenQrode(int isOpenQrcode) {
        CacheUtils.setInt(Constants.IS_OPEN_QRCODE, isOpenQrcode);
    }

    public static int getIsOpenQrcode() {
        return CacheUtils.getInt(Constants.IS_OPEN_QRCODE, 0);
    }

    /**
     * 未经裁剪的用户原图
     */
    public static void setUserPhoto(String originPhoto) {
        CacheUtils.setString(Constants.USER_PHOTO, originPhoto);
    }

    public static String getUserPhoto() {
        return CacheUtils.getString(Constants.USER_PHOTO);
    }

    /**
     * 裁剪的用户原图
     */
    public static void setClipBitmap(String bitmap) {
        CacheUtils.setString(Constants.CLIP_BITMAP, bitmap);
    }

    public static String getClipBitmap() {
        return CacheUtils.getString(Constants.CLIP_BITMAP);
    }


    /**
     * 未经美颜的脸部原图
     */
    public static void setFaceImg(String faceImg) {
        CacheUtils.setString(Constants.FACE_PHOTO, faceImg);
    }

    public static String getFaceImg() {
        return CacheUtils.getString(Constants.FACE_PHOTO);
    }

    /**
     * 是否做过测试题
     *
     * @param isTest
     */
    public static void setIsTest(int isTest) {
        CacheUtils.setInt(Constants.IS_TEST, isTest);
    }

    public static int getIsTest() {
        return CacheUtils.getInt(Constants.IS_TEST, 0);
    }

    /**
     * 小程序的二维码
     */
    public static void setMiniProgramUrl(String qrcode) {
        CacheUtils.setString(Constants.MINI_PROGREAM, qrcode);
    }

    public static String getMiniProgramUrl() {
        return CacheUtils.getString(Constants.MINI_PROGREAM);
    }

    /**
     * 设置试衣间的二维码
     */
    public static void setWearRoomQrcode(String qrcode) {
        CacheUtils.setString(Constants.WEAR_ROOM_QRCODE, qrcode);
    }

    public static String getWearRoomQrcode() {
        return CacheUtils.getString(Constants.WEAR_ROOM_QRCODE);
    }

    /**
     * 测评的UserId
     * testConsumerId
     */
    public static void setTestConsumerId(String testUserId) {
        CacheUtils.setString(Constants.TEST_CONSUMER_ID, testUserId);
    }

    public static String getTestConsumerId() {
        return CacheUtils.getString(Constants.TEST_CONSUMER_ID);
    }

    /**
     * 设置用户身形 X L XS
     */
    public static void setUserSize(String size) {
        CacheUtils.setString(Constants.USER_SIZE, size);
    }

    public static String getUserSize() {
        return CacheUtils.getString(Constants.USER_SIZE);
    }

    /**
     * 设置推荐服装的数量
     */
    public static void setRecommendAmount(int count) {
        CacheUtils.setInt(Constants.RECOMMEND_AMOUNT, count);
    }

    public static int getRecommendAmount() {
        return CacheUtils.getInt(Constants.RECOMMEND_AMOUNT, 0);
    }

    /**
     * 设置日期-几号
     *
     * @param days
     */
    public static void setCurrentDays(String days) {
        CacheUtils.setString(Constants.DAYS, days);
    }

    public static String getCurrentDays() {
        return CacheUtils.getString(Constants.DAYS);
    }

    public static boolean getAlreadyShowTip() {
        return CacheUtils.getBoolean(Constants.ALREADY_SHOW_TIP);
    }

    public static void setAlreadyShowTip(boolean alreadyShowTip) {
        CacheUtils.setBoolean(Constants.ALREADY_SHOW_TIP, alreadyShowTip);
    }

    /**
     * 店铺是否支持支付功能
     */
    public static void setShopSupportPay(int shopSupportPay) {
        CacheUtils.setInt(Constants.SHOP_SUPPORT_PAY, shopSupportPay);
    }

    public static int getShopSupportPay() {
        return CacheUtils.getInt(Constants.SHOP_SUPPORT_PAY);
    }

    public static void setBackToBannerCount(Long count) {
        CacheUtils.setLong(Constants.BACK_TO_BANNER_COUNT, count);
    }

    public static Long getBackToBannerCount() {
        return CacheUtils.getLong(Constants.BACK_TO_BANNER_COUNT, 0L);
    }
}
